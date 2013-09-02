
arr = [ int(line) for line in open('QuickSort.txt').readlines() ]


def part(arr,l,r,pos):
    def swap(l,r):
        t=arr[l]
        arr[l]=arr[r]
        arr[r]=t
    swap(pos,l)
    p = arr[l]
    i = l+1
    for j in range(l+1,r):
        if arr[j]<p:
            swap(j,i)
            i+=1
    swap(l,i-1)
    return i-1

def qs(arr,l,r,piv):
    if l==r:
        return 0
    ret = r-l-1
    if l<r:
        p = part(arr,l,r,piv(arr,l,r))
        ret+=qs(arr,l,p,piv)
        ret+=qs(arr,p+1,r,piv)
    return ret


print 'Q1:',qs(arr[:],0,len(arr),lambda arr,l,r:l)
print 'Q2:',qs(arr[:],0,len(arr),lambda arr,l,r:r-1)
def q3(arr,l,r):
    if r-l<=2:
        return l
    tmp = [ (arr[l],l),(arr[(l+r-1)/2],(l+r-1)/2),(arr[r-1],r-1) ]
    return sorted(tmp)[1][1]
print 'Q3:',qs(arr[:],0,len(arr),q3)



    








