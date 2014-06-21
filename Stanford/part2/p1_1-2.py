rawdata = open('jobs.txt').readlines()
data = [ tuple(map(int, r.split())) for r in rawdata[1:] ]

def calc(data):
    t = 0
    ans = 0
    for item in data:
        t += item[1]
        ans += item[0]*t
    return ans

print 'ans1=',calc(sorted(data,key=lambda x:(-(x[0]-x[1]), -x[0])))
print 'ans2=',calc(sorted(data,key=lambda x:-1.0*x[0]/x[1]))
