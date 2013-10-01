#!/usr/bin/python
# -*- coding: utf-8 -*-

import math
from random import randint, shuffle


def length(point1, point2):
    return math.sqrt((point1[0] - point2[0]) ** 2 + (point1[1] - point2[1]) ** 2)


def solveIt(inputData):
    # Modify this code to run your optimization algorithm

    # parse the input
    lines = inputData.split('\n')

    nodeCount = int(lines[0])
    print nodeCount
    points = []
    for i in range(1, nodeCount + 1):
        line = lines[i]
        parts = line.split()
        points.append((float(parts[0]), float(parts[1]), i - 1))

    shuffle(points)

    dist = []
    dist_s = []
    # preprocessing distance
    for p1 in points:
        dist.append([])
        for p2 in points:
            dist[-1].append(length(p1, p2))



    # build a trivial solution
    # visit the nodes in the order they appear in the file
    solution = range(0, nodeCount)

    # calculate the length of the tour
    def calc(solution):
        obj = dist[solution[-1]][solution[0]]
        for index in range(0, len(solution) - 1):
            obj += dist[solution[index]][solution[index + 1]]
        return obj

    def greedy():
        ret = [0]
        vis = [0] * nodeCount
        vis[0] = 1

        for i in range(0, nodeCount - 1):
            cur = ret[-1]
            tmp = 0
            tdis = 1e10
            for j in range(0, nodeCount):
                if not vis[j]:
                    if dist[j][cur] < tdis:
                        tdis = dist[j][cur]
                        tmp = j
            vis[tmp] = 1
            ret.append(tmp)
        return ret


    def incr(o):
        ret = [o[0], o[1]]
        obj = 2 * dist[o[0]][o[1]]
        for i in range(2, nodeCount):
            cost = 1e10
            pos = 0
            l = len(ret)
            for j in range(0, l):
                if j + 1 < l:
                    tcost = dist[ret[j]][o[i]] + dist[o[i]][ret[j + 1]] - dist[ret[j]][ret[j + 1]]
                else:
                    tcost = dist[ret[j]][o[i]] + dist[ret[0]][o[i]] - dist[ret[0]][ret[-1]]
                if tcost < cost:
                    cost = tcost
                    pos = j
            obj += cost
            ret.insert(pos + 1, o[i])
        return (obj, ret, o)

    def cross(perm1, perm2):
        l = len(perm1)
        vis = [0] * (l + 1)
        cnt = 0
        bound = l / 2
        for i in perm1:
            if cnt < bound:
                vis[i] = 1
                cnt += 1
        ret = []
        for x in perm2:
            if not vis[x]:
                vis[x] = 1
                ret.append(x)
                cnt += 1
            if cnt == l:
                break
        ret += perm1[:bound]
        return ret

    def swp(arr, l, r):
        t = arr[l]
        arr[l] = arr[r]
        arr[r] = t


    order = range(0, nodeCount)
    shuffle(order)
    obj, solution, o = incr(order)
    sol = list(solution)
    population = []
    psize = 50
    for i in range(psize):
        print i
        shuffle(order)
        population.append(incr(order))

    for i in range(500):
        if i % 20 == 0:
            print i
        for j in range(psize * 10):
            r1 = randint(0, psize - 1)
            r2 = randint(0, psize - 1)
            o = cross(population[r1][1], population[r2][1])
            tobj = calc(o)
            tsol = o[:]
            if tobj < obj:
                print 'update by cross from', obj, 'to ', tobj
                obj = tobj
                solution = tsol
                population.append((tobj, tsol, o))
        vl = population[:]
        for v in vl:
            r1 = randint(0, nodeCount - 1)
            r2 = randint(0, nodeCount - 1)
            if r1 == r2:
                continue
            swp(v[2], r1, r2)
            o = v[2]
            tobj, tsol, o = incr(o)
            if tobj < v[0]:
                population.append((tobj, tsol, o))
                if tobj < obj:
                    print 'update by mutate from', obj, 'to ', tobj
                    obj = tobj
                    solution = tsol
        for v in vl:
            r1 = randint(0, nodeCount - 1)
            r2 = randint(0, nodeCount - 1)
            vv = v[2]
            if r1 > r2:
                t = r1
                r1 = r2
                r2 = t
            o = vv[:r1] + list(reversed(vv[r1:r2])) + vv[r2:]
            tobj, tsol, o = incr(o)
            if tobj < v[0]:
                population.append((tobj, tsol, o))
                if tobj < obj:
                    print 'update by reverse from', obj, 'to ', tobj
                    obj = tobj
                    solution = tsol
        population.sort()
        population = population[:psize]


    def ridx(x):
        return points[x][2]

    # prepare the solution in the specified output format
    outputData = str(obj) + ' ' + str(0) + '\n'
    outputData += ' '.join(map(str, map(ridx, solution)))

    return outputData


import sys

if __name__ == '__main__':
    if len(sys.argv) > 1:
        fileLocation = sys.argv[1].strip()
        inputDataFile = open(fileLocation, 'r')
        inputData = ''.join(inputDataFile.readlines())
        inputDataFile.close()
        print solveIt(inputData)
    else:
        print 'This test requires an input file.  Please select one from the data directory. (i.e. python solver.py ./data/tsp_51_1)'

