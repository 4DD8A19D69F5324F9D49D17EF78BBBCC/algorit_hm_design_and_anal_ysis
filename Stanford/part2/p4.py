from collections import deque


def get_edge_list(rawdata):
    n = int(rawdata[0].split()[0])
    el = [[] for i in range(0, n + 1)]
    for edge in rawdata[1:]:
        u, v, w = map(int, edge.split())
        el[u].append((v, w))
    return n, el


def ssp(n, edgelist):
    q = deque(range(0, n))
    vis = [0] * (n + 1)
    dis = [0] * (n + 1)
    cnt = [1] * (n + 1)
    nc = False
    while len(q) != 0:
        u = q.popleft()
        vis[u] = 0
        if nc:
            break
        for edge in edgelist[u]:
            v, w = edge
            if dis[u] + w < dis[v]:
                dis[v] = dis[u] + w
                if not vis[v]:
                    if cnt[v] == n:
                        nc = True
                        break
                    vis[v] = 1
                    cnt[v] += 1
                    q.append(v)
    if nc:
        return 1e9
    else:
        x = min(dis[1:])
        if x < 0:
            return x
        else:
            ret = 1e9
            for i in range(1, n + 1):
                for edge in edgelist[i]:
                    ret = min(ret, edge[1])
            return ret


g1 = get_edge_list(open('g1.txt').readlines())
g2 = get_edge_list(open('g2.txt').readlines())
g3 = get_edge_list(open('g3.txt').readlines())

print ssp(g1[0], g1[1]), ssp(g2[0], g2[1]), ssp(g3[0], g3[1])
