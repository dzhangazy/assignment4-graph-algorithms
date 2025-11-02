package graph.topo;

import metrics.BasicMetrics; // ✅ импортируем систему метрик
import java.util.*;

public class KahnTopoSort {
    // ✅ Перегрузка метода: с метриками и без
    public static List<Integer> topologicalSort(Map<Integer, Set<Integer>> graph) {
        return topologicalSort(graph, null);
    }

    public static List<Integer> topologicalSort(Map<Integer, Set<Integer>> graph, BasicMetrics metrics) {
        if (metrics == null) metrics = new BasicMetrics();
        metrics.startTimer(); // старт измерения времени

        Map<Integer, Integer> indeg = new HashMap<>();
        for (int u : graph.keySet()) indeg.put(u, 0);

        // ✅ считаем рёбра и увеличиваем счётчик
        for (int u : graph.keySet()) {
            for (int v : graph.get(u)) {
                indeg.put(v, indeg.getOrDefault(v, 0) + 1);
                metrics.incrementCounter("edges_processed");
            }
        }

        Queue<Integer> q = new ArrayDeque<>();
        for (int u : indeg.keySet()) {
            if (indeg.get(u) == 0) {
                q.add(u);
                metrics.incrementCounter("initial_push");
            }
        }

        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            metrics.incrementCounter("pop"); // ✅ узел достаётся из очереди
            int u = q.poll();
            order.add(u);
            for (int v : graph.get(u)) {
                metrics.incrementCounter("edge_relaxed");
                indeg.put(v, indeg.get(v) - 1);
                if (indeg.get(v) == 0) {
                    q.add(v);
                    metrics.incrementCounter("push");
                }
            }
        }

        metrics.stopTimer(); // завершение таймера
        return order;
    }

    // ✅ удобный метод для вывода статистики
    public static void printMetrics(BasicMetrics metrics) {
        System.out.println("Kahn TopoSort metrics:");
        System.out.println("  Time (ns): " + metrics.getElapsedTime());
        System.out.println("  Edges processed: " + metrics.getCounter("edges_processed"));
        System.out.println("  Pops: " + metrics.getCounter("pop"));
        System.out.println("  Pushes: " + metrics.getCounter("push"));
        System.out.println("  Initial pushes: " + metrics.getCounter("initial_push"));
        System.out.println("  Edge relaxations: " + metrics.getCounter("edge_relaxed"));
    }
}
