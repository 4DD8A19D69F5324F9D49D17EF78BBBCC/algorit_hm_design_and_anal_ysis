from random import shuffle
adj = [ line.split() for line in open('kargerMinCut.txt').readlines() ]
n = len(adj)
el = []
for line in adj:
    _line=map(int,line)
    for i in range(1,len(_line)):
        if _line[0]<_line[i]:
            el.append((_line[0],_line[i]))

def mincut(el,n):
    f = range(0,n+1)
    cnt = 0
    def find(x):
        if x==f[x]:
            return x
        else:
            f[x]=find(f[x])
            return f[x]
    shuffle(el)
    for e in el:
        if find(e[0])!=find(e[1]):
            f[find(e[0])]=find(e[1])
            cnt+=1
        if cnt==n-2:
            break
    cut = 0
    for e in el:
        if find(e[0])!=find(e[1]):
            cut+=1
    return cut

_min=1e9
for i in range(0,2*n):
    _tmp=mincut(el,n)
    if _tmp<_min:
        print 'updated from',_min,' to ',_tmp
        _min=_tmp
print 'min cut=',_min
    
    
