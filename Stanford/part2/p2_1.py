rawdata = open('clustering1.txt').readlines()
n = int(rawdata[0])
data = [ tuple(map(int,line.split())) for line in rawdata[1:] ]


def work(data):
    data.sort(key=lambda x:x[2])
    f = range(0,n+1)
    cc = n
    def find(x):
        if x==f[x]:
            return x
        else:
            f[x]=find(f[x])
            return f[x]

    obj = 1e9
    for e in data:
        fx = find(e[0])
        fy = find(e[1])
        if cc==4:
            if fx!=fy:
                obj=min(obj,e[2])
                break
        else:
            if fx!=fy:
                cc -= 1
                f[fx]=fy
    print obj


work(data)
