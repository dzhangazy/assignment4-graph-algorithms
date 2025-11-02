package graph.scc;

import metrics.BasicMetrics;  // ✅ добавим пакет метрик
import java.util.*;

public class TarjanSCC {
    private final int n;
    private final List<List<Integer>> adj;
    private int time = 0;
    private final int[] low, disc, comp;
    private final boolean[] inStack;
    private final Deque<Integer> stack = new ArrayDeque<>();
    private int sccCount = 0;
    private final List<List<Integer>> sccList = new ArrayList<>();

    // ✅ добавляем объект метрик
    private final BasicMetrics metrics = new BasicMetrics();

    public TarjanSCC(int n) {
        this.n = n;
        this.adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        low = new int[n];
        disc = new int[n];
        comp = new int[n];
        inStack = new boolean[n];
        Arrays.fill(disc, -1);
    }

    public void addEdge(int u, int v) {
        adj.get(u).add(v);
    }

    public List<List<Integer>> run() {
        metrics.startTimer(); // ✅ начать измерение времени
        for (int i = 0; i < n; i++)
            if (disc[i] == -1) dfs(i);
        metrics.stopTimer(); // ✅ остановить таймер
        return sccList;
    }

    private void dfs(int u) {
        metrics.incrementCounter("dfs_calls"); // ✅ подсчёт вызовов DFS
        disc[u] = low[u] = ++time;
        stack.push(u);
        inStack[u] = true;

        for (int v : adj.get(u)) {
            metrics.incrementCounter("edges_checked"); // ✅ подсчёт рёбер
            if (disc[v] == -1) {
                dfs(v);
                low[u] = Math.min(low[u], low[v]);
            } else if (inStack[v]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }

        if (low[u] == disc[u]) {
            List<Integer> compNodes = new ArrayList<>();
            while (true) {
                metrics.incrementCounter("stack_pops"); // ✅ считаем pops
                int v = stack.pop();
                inStack[v] = false;
                comp[v] = sccCount;
                compNodes.add(v);
                if (v == u) break;
            }
            sccList.add(compNodes);
            sccCount++;
        }
    }

    public int[] getComponents() { return comp; }
    public int getSccCount() { return sccCount; }

    public Map<Integer, Set<Integer>> buildCondensation() {
        Map<Integer, Set<Integer>> dag = new HashMap<>();
        for (int i = 0; i < sccCount; i++) dag.put(i, new HashSet<>());
        for (int u = 0; u < n; u++) {
            for (int v : adj.get(u)) {
                if (comp[u] != comp[v])
                    dag.get(comp[u]).add(comp[v]);
            }
        }
        return dag;
    }

    // ✅ метод для вывода статистики
    public void printMetrics() {
        System.out.println("Tarjan SCC metrics:");
        System.out.println("  Time (ns): " + metrics.getElapsedTime());
        System.out.println("  DFS calls: " + metrics.getCounter("dfs_calls"));
        System.out.println("  Edges checked: " + metrics.getCounter("edges_checked"));
        System.out.println("  Stack pops: " + metrics.getCounter("stack_pops"));
    }

    // ✅ метод для внешнего доступа к метрикам (если нужно)
    public BasicMetrics getMetrics() {
        return metrics;
    }
}
