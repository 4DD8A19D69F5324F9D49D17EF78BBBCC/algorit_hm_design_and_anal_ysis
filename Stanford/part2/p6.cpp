#include <iostream>
#include <stdio.h>
#include <string.h>
#include <algorithm>
using namespace std;

const int N=1000010<<1;
const int E=N<<2;


struct Graph{
    int head[N],to[E],next[E];
    int edge_cnt;
    void clear(){
        edge_cnt=0;
        memset(head,0,sizeof(head));
    }
}g,rg;

int f(int u){
    if(u>0) return u<<1;
    else return (-u)<<1|1;
}

void add_edge(Graph &g,int s,int t){
    g.edge_cnt++;
    g.to[g.edge_cnt]=t;
    g.next[g.edge_cnt]=g.head[s];
    g.head[s]=g.edge_cnt;
}

void add_relation(Graph &g,Graph &rg,int u,int v) {
    u=f(u);
    v=f(v);
    add_edge(g,u^1,v);
    add_edge(g,v^1,u);
    add_edge(rg,v,u^1);
    add_edge(rg,u,v^1);
}

int stk[N],scnt;
int vis[N];
int scc[N];
int n;

void dfs1(Graph &g,int x) {
    if(vis[x]) return;
    vis[x]=1;
    for(int i=g.head[x];i;i=g.next[i]) {
       if(!vis[g.to[i]]) dfs1(g,g.to[i]);
    }
    stk[scnt++]=x;
}

void dfs2(Graph &g,int x,int sccid) {
    if(vis[x]) return;
    vis[x]=1;
    for(int i=g.head[x];i;i=g.next[i]) {
        if(!vis[g.to[i]]) dfs2(g,g.to[i],sccid);
    }
    scc[x]=sccid;
}

int main(int argc,char *argv[])
{
    if(argc == 1){
        printf("usage: pa6 [filename]\n");
    }else {
        FILE *fp=fopen(argv[1],"r");

        fscanf(fp,"%d",&n);
        g.clear();
        rg.clear();
        for(int i=0;i<n;i++) {
            int u,v;
            fscanf(fp,"%d%d",&u,&v);
            add_relation(g,rg,u,v);
        }

        fclose(fp);
        memset(vis,0,sizeof(vis));
        scnt=0;
        for(int i=2;i<=(n<<1|1);i++) if(!vis[i]){
            dfs1(rg,i);
        }
        memset(vis,0,sizeof(vis));
        memset(scc,0,sizeof(scc));
        int sccid=0;
        for(int i=scnt-1;i>=0;i--) if(!vis[stk[i]]){
            dfs2(g,stk[i],++sccid);
        }
        bool can=true;
        for(int i=2;i<=(n<<1|1);i+=2) {
            if(scc[i]==scc[i+1]) can=false;
        }
        printf("%d\n",can);
    }
    return 0;
}
