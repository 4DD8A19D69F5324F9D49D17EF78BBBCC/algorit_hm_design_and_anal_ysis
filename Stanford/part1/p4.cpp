#include <stdio.h>
#include <string.h>
#include <vector>
#include <algorithm>
#include <functional>
using namespace std;

const int N=875714;

typedef vector<int>::iterator IT;
vector<int> g[N+10];
vector<int> rg[N+10];

bool vis[N+10]={0};
int scc[N+10]={0};
int stk[N+10],scnt;
int counter[N+10]={0};


void dfs1(int x){
    if(vis[x]) return;
    vis[x]=1;
    for(IT it=rg[x].begin();it!=rg[x].end();it++) dfs1(*it);
    stk[scnt++]=x;
}

bool dfs2(int x,int p){
    if(vis[x]) return false;
    vis[x]=1;
    for(IT it=g[x].begin();it!=g[x].end();it++) dfs2(*it,p);
    scc[x]=p;
}


int main(){
    int x,y;
    fill_n(g,N+10,vector<int>());
    fill_n(rg,N+10,vector<int>());
    while(scanf("%d%d",&x,&y)!=EOF){
        g[x].push_back(y);
        rg[y].push_back(x);
    }
    printf("reading_complete\n");
    memset(vis,0,sizeof(vis));
    scnt=0;
    int cnt=0;
    for(int i=1;i<=N;i++) dfs1(i);
    printf("dfs 1 complete\n");
    memset(vis,0,sizeof(vis));
    for(int i=scnt-1;i>=0;i--) if(dfs2(stk[i],cnt)) cnt++;
    printf("dfs 2 complete\n");
    printf("#scc:%d\n",cnt);
    for(int i=1;i<=N;i++) counter[scc[i]]++;
    sort(counter,counter+cnt,greater<int>());
    for(int i=0;i<5;i++) printf("%d,",counter[i]);
    putchar('\n');

}
