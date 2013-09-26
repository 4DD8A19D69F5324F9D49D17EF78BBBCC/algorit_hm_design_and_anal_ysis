rawdata = open('clustering_big.txt').readlines()
n,m = map(int,rawdata[0].split())
data = [ int(''.join(line.split()),2) for line in rawdata[1:]]


def solve(data):
    data = list(set(data))
    n = len(data)
    f = range(0,n+1)
    cc = n
    def find(x):
        if x==f[x]:
            return x
        else:
            f[x]=find(f[x])
            return f[x]
    def union(a,b):
        fx = find(a)
        fy = find(b)
        if fx!=fy:
            f[fx]=fy
            return True
        return False
    
    tmpdict = {}
    edgelist = []
    
    for i,x in enumerate(data):
        if x not in tmpdict:
            tmpdict[x]=i
        else:
            cc-=union(i,tmpdict[x])
    for i,x in enumerate(data):
        if i%1000==0:
            print 'working with node',i
        for b1 in range(m):
            t = x^(1<<b1)
            if t in tmpdict:
                cc -= union(i,tmpdict[t])
        for b1 in range(m):
            for b2 in range(b1+1,m):
                t = x^(1<<b1) ^(1<<b2)
                if t in tmpdict:
                    cc -= union(i,tmpdict[t])
    return cc

ans=solve(data)
print ans

    
    
    
    
