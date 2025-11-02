import graph.scc.TarjanSCC;
import graph.topo.KahnTopoSort;
import graph.dagsp.DAGShortestPaths;
import metrics.BasicMetrics; // ✅ добавили импорт метрик

import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;
import org.json.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Path dataDir = Path.of("data");
        if (!Files.exists(dataDir)) {
            System.out.println("❌ Folder 'data/' not found!");
            return;
        }

        String csvPath = "metrics_report.csv";
        try (PrintWriter csv = new PrintWriter(new FileWriter(csvPath))) {
            // Заголовок таблицы
            csv.println("File,SCC_Time(ns),DFS_Calls,Edges_Checked,Stack_Pops,Topo_Time(ns),Edges_Processed,Pops,Pushes,Relaxations_Shortest,Relaxations_Longest,Shortest_Time(ns),Longest_Time(ns)");

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dataDir, "*.json")) {
                for (Path path : stream) {
                    System.out.println("\n===============================");
                    System.out.println("📂 Analyzing file: " + path.getFileName());
                    System.out.println("===============================");

                    try {
                        analyzeGraph(path, csv);
                    } catch (Exception e) {
                        System.out.println("⚠️ Error processing " + path.getFileName() + ": " + e.getMessage());
                    }
                }
            }

            System.out.println("\n✅ Metrics successfully written to " + csvPath);
        }
    }

    private static void analyzeGraph(Path jsonPath, PrintWriter csv) throws Exception {
        String jsonText = Files.readString(jsonPath);
        JSONObject obj = new JSONObject(jsonText);
        int n = obj.getInt("n");
        boolean directed = obj.optBoolean("directed", true);
        int source = obj.optInt("source", 0);

        // Initialize graph algorithms
        TarjanSCC scc = new TarjanSCC(n);
        DAGShortestPaths dag = new DAGShortestPaths(n);

        // Read edges
        JSONArray edges = obj.getJSONArray("edges");
        for (int i = 0; i < edges.length(); i++) {
            JSONObject e = edges.getJSONObject(i);
            int u = e.getInt("u");
            int v = e.getInt("v");
            int w = e.optInt("w", 1);
            scc.addEdge(u, v);
            dag.addEdge(u, v, w);
        }

        // 1️⃣ Tarjan SCC
        List<List<Integer>> comps = scc.run();
        System.out.println("Strongly Connected Components:");
        for (int i = 0; i < comps.size(); i++) {
            System.out.println("  SCC " + i + ": " + comps.get(i));
        }
        scc.printMetrics();

        // 2️⃣ Condensation DAG + Kahn TopoSort
        Map<Integer, Set<Integer>> condensation = scc.buildCondensation();
        BasicMetrics topoMetrics = new BasicMetrics();
        List<Integer> topoOrder = KahnTopoSort.topologicalSort(condensation, topoMetrics);
        System.out.println("Topological order of SCC DAG: " + topoOrder);
        KahnTopoSort.printMetrics(topoMetrics);

        // 3️⃣ DAG Shortest / Longest Paths
        int[] shortest = dag.shortestPath(source, topoOrder);
        dag.printMetrics("Shortest");
        int[] longest = dag.longestPath(source, topoOrder);
        dag.printMetrics("Longest");

        System.out.println("Shortest distances from source " + source + ": " + Arrays.toString(shortest));
        System.out.println("Longest distances from source " + source + ": " + Arrays.toString(longest));

        // 4️⃣ Write metrics to CSV
        csv.printf(
            "%s,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d%n",
            jsonPath.getFileName(),
            scc.getMetrics().getElapsedTime(),
            scc.getMetrics().getCounter("dfs_calls"),
            scc.getMetrics().getCounter("edges_checked"),
            scc.getMetrics().getCounter("stack_pops"),
            topoMetrics.getElapsedTime(),
            topoMetrics.getCounter("edges_processed"),
            topoMetrics.getCounter("pop"),
            topoMetrics.getCounter("push"),
            dag.getMetrics().getCounter("relax_success"),
            dag.getMetrics().getCounter("relax_success_longest"),
            dag.getMetrics().getElapsedTime(),
            dag.getMetrics().getElapsedTime() // можешь потом сделать раздельные таймеры
        );
    }
}
