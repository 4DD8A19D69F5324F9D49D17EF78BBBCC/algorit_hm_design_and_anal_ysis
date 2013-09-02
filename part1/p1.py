
arr = [ int(line) for line in open('IntegerArray.txt').readlines() ]

def decrete(arr):
    tarr = list(enumerate(arr))
    tarr.sort(key=lambda x:x[1])
    return map(lambda x:x[0]+1,tarr)

def calc(arr):
    n = len(arr)
    ta = [ 0 ] * (n+1)
    tarr = decrete(arr)
    ret = 0
    def t_add(ta,p):
        while p<=n:
            ta[p]+=1
            p+=p&(-p)
    def t_sum(ta,p):
        ret=0
        while p>0:
            ret+=ta[p]
            p-=p&(-p)
        return ret
    for x in reversed(tarr):
        ret += t_sum(ta,x-1)
        t_add(ta,x)
    return ret

print calc(arr)








