package utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GraphGenerator {

    private static Random rand = new Random();

    public static void main(String[] args) throws IOException {
        generateAll();
    }

    public static void generateAll() throws IOException {
        generateCategory("small", 3, 6, 10);
        generateCategory("medium", 3, 10, 20);
        generateCategory("large", 3, 20, 50);
        System.out.println("✅ All datasets generated in /data/");
    }

    private static void generateCategory(String name, int count, int minN, int maxN) throws IOException {
        for (int i = 1; i <= count; i++) {
            int n = randBetween(minN, maxN);
            boolean directed = true;

            List<Map<String, Object>> edges = new ArrayList<>();
            int edgeCount = randBetween(n, n * 3); // moderate density

            for (int e = 0; e < edgeCount; e++) {
                int u = rand.nextInt(n);
                int v = rand.nextInt(n);
                if (u == v) continue; // no self loops
                int w = randBetween(1, 10);
                Map<String, Object> edge = new HashMap<>();
                edge.put("u", u);
                edge.put("v", v);
                edge.put("w", w);
                edges.add(edge);
            }

            JSONObject obj = new JSONObject();
            obj.put("directed", directed);
            obj.put("n", n);
            obj.put("edges", new JSONArray(edges));
            obj.put("source", 0);
            obj.put("weight_model", "edge");

            // Save to /data/
            String filename = "data/" + name + "_" + i + ".json";
            try (FileWriter file = new FileWriter(filename)) {
                file.write(obj.toString(2));
            }

            System.out.printf("Generated %s (%d nodes, %d edges)\n", filename, n, edges.size());
        }
    }

    private static int randBetween(int a, int b) {
        return a + rand.nextInt(b - a + 1);
    }
}
