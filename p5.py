from collections import deque
graph_str = open('dijkstraData.txt').readlines()
n = len(graph_str)+1
adj = [ [] for i in range(n) ]
vertex = [7,37,59,82,99,115,133,165,188,197]

for line in graph_str:
    arr=line.split()
    idx=int(arr[0])
    for item in arr[1:]:
        pair=item.split(',')
        pair=tuple(map(int,pair))
        adj[idx].append(pair)

def spfa(graph,n,start):
    dis = [ 1e9] *n
    vis = [ 0 ] * n
    q = deque()
    q.append(start)
    dis[start]=0
    while q:
        t = q.popleft()
        vis[t]=0
        for edge in graph[t]:
            if dis[t]+edge[1]<dis[edge[0]]:
                dis[edge[0]]=dis[t]+edge[1]
                if not vis[edge[0]]:
                    vis[edge[0]]=1
                    q.append(edge[0])
    return dis

dis=spfa(adj,n,1)
print ','.join([ str(dis[v]) for v in vertex])
    

        


