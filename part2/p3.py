#!/usr/bin/python
# -*- coding: utf-8 -*-



from random import random,shuffle,randint,randrange
from datetime import datetime
import  sys
sys.setrecursionlimit(10**6)

_cur = (0,0,0)
_nowstep = [0]
_count = 0

def solveIt(inputData):
    # Modify this code to run your optimization algorithm

    # parse the input
    lines = inputData.split('\n')

    firstLine = lines[0].split()
    items = int(firstLine[1])
    capacity = int(firstLine[0])

    values = []
    weights = []


    
    for i in range(1, items + 1):
        line = lines[i]
        parts = line.split()

        values.append(int(parts[0]))
        weights.append(int(parts[1]))

    items = len(values)


    data = zip(values, weights,range(0,items))
    def mycmp(a, b):
        if 1.0* a[0] / a[1] > 1.0* b[0] / b[1]:
            return -1
        elif 1.0* a[0] / a[1] == 1.0* b[0] / b[1]:
            return 0
        else:
            return 1

    
    def solvedp(data):
        dp = [ [0] * (capacity + 1) ]
        p = [ [-1] * (capacity + 1) ]
        
        for i in xrange(len(data)):
            
            dp.append(list(dp[i]))
            p.append([0] * (capacity + 1))
            item = data[i];
            for x in xrange(item[1], capacity + 1):
                tmp0 = dp[i][x];
                tmp1 = dp[i][x - item[1]] + item[0];
                if tmp1 > tmp0:
                    p[i + 1][x] = 1;
                    dp[i + 1][x] = tmp1;
                else:
                    p[i + 1][x] = 0;
                    dp[i + 1][x] = tmp0;
        last = dp[len(dp) - 1]
        _max = 0
        _p = 0
        for i in xrange(len(last)):
            if last[i] > _max:
                _max = last[i]
                _p = i
        _ans = _max
        _step = []
        _cur = len(dp) - 1
        while _cur != 0:
            _step.append(p[_cur][_p])
            _p -= p[_cur][_p] * data[_cur - 1][1];
            _cur -= 1;
        _step.reverse()
        return (_max, 1, _step)
    

    
    def solvedfs(data):
        def greedy(_data, prob):
            data=_data
            _cap = capacity
            _value = 0
            _step = [0]*items
            for i,item in  enumerate(data):
                if _cap >= item[1] and random() < prob:
                    _cap -= item[1]
                    _value += item[0]
                    _step[item[2]]=1
                else:
                    _step[item[2]]=0
            return (_value, 0, _step)
        
        starttime = datetime.now()
        global _cur
        global _nowstep
        _data=data[:]
   
        _cur = greedy(_data, 1.1)
        _nowstep = [0] * items
        global _count
        _count = 0
        def dfs(cap, pos, val):
            global _count
            _count += 1
            if _count %10000==0:
                print _count
            if pos%100==0:
                print pos
            if random()<0.001:
                pass
            global _cur
            global _nowstep
            nowtime = datetime.now()
            if (nowtime - starttime).seconds > 100:
                return

            optim = val
            _cap = cap
            for i in xrange(pos, len(_data)):
                if _nowstep[_data[i][2]]==0:
                    if _data[i][1] <= _cap:
                        _cap -= _data[i][1];
                        optim += _data[i][0]
                    else:
                        optim += 1.0 * _data[i][0]  / _data[i][1] * _cap
                        break
            if optim <= _cur[0]:
                return
            
            if val > _cur[0]:
                print 'updated from',_cur[0],'to ',val
                def f(x):
                    if x==-1:
                        return 0
                    else:
                        return x
                _cur = (val, 0, list(map(f,_nowstep)))
            if pos >= items:
                return    
            if data[pos][1] <= cap:
                _nowstep[data[pos][2]] = 1
                dfs(cap - data[pos][1], pos + 1, val + data[pos][0])
                _nowstep[data[pos][2]] = 0
            _nowstep[data[pos][2]]=-1
            dfs(cap, pos + 1, val)
            _nowstep[data[pos][2]]=0
        print _cur[0]
        dfs(capacity, 0, 0)
        return _cur
    print 'begin solving'
    print items,capacity
  
    
    if items * capacity <= 1:
        solution = solvedp(sorted(data,mycmp))
    else:
        solution = solvedfs(sorted(data,mycmp))
    
        
    
    # prepare the solution in the specified output format
    outputData = str(solution[0]) + ' ' + str(solution[1]) + '\n'
    outputData += ' '.join(map(str, solution[2]))
    return outputData



if __name__ == '__main__':
    if len(sys.argv) > 1:
        fileLocation = sys.argv[1].strip()
        inputDataFile = open(fileLocation, 'r')
        inputData = ''.join(inputDataFile.readlines())
        inputDataFile.close()
        print solveIt(inputData)
    else:
        print 'This test requires an input file.  Please select one from the data directory. (i.e. python solver.py ./data/ks_4_0)'

