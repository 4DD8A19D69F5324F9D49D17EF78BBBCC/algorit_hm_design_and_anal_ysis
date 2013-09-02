rawdata = open('edges.txt').readlines()
n,m=map(int, rawdata[0].split())
data = [ tuple(map(int, l.split())) for l in rawdata[1:] ]
data.sort(key=lambda x:x[2])

f = range(0,n+1)
def find(x):
    if f[x]==x:
        return x
    else:
        f[x]=find(f[x])
        return f[x]

cost = 0
for edge in data:
    if find(edge[0])!=find(edge[1]):
        f[find(edge[0])] = find(edge[1])
        cost += edge[2]
print 'cost=',cost
