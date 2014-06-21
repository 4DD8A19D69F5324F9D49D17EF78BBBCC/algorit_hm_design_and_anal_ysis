
from collections import Counter
import sys
sys.setrecursionlimit(1000000000)
el = [ line.split() for line in open('SCC.txt').readlines() ]
print 'loading complete'
g = []
rg = []
n = 875714+10
for i in xrange(n):
    g.append([])
    rg.append([])

for e in el:
    g[int(e[0])].append(int(e[1]))
    g[int(e[1])].append(int(e[0]))


print 'building complete'

vis = [0]*n
stk = []
scc = [0]*n
def dfs1(x,vis):
    if vis[x]==1:
        return
    vis[x]=1
    for y in rg[x]:
        dfs1(y,vis)
    stk.append(x)

def dfs2(x,p,vis):
    if vis[x]==1:
        return False
    vis[x]=1
    for y in g[x]:
        dfs2(y,p,vis)
    scc[x]=p
    return True

for i in range(1,n):
    dfs1(i,vis)
vis=[0]*n
cnt=0
for i in reversed(stk):
    if dfs2(i,cnt,vis)==True:
        cnt+=1

ct = Counter(scc)
result = [ct[key] for key in ct]
result.sort(reverse=True)
print result[:10]
    



    
    
