package graph;

import graph.topo.KahnTopoSort;
import graph.dagsp.DAGShortestPaths;
import graph.scc.TarjanSCC;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class GraphTests {
    @Test
    public void testSmallSCC() {
        TarjanSCC scc = new TarjanSCC(4);
        scc.addEdge(0,1); scc.addEdge(1,2); scc.addEdge(2,0);
        scc.addEdge(2,3);
        List<List<Integer>> comps = scc.run();
        assertEquals(2, comps.size());
    }

    @Test
    public void testTopoAndPaths() {
        DAGShortestPaths dag = new DAGShortestPaths(4);
        dag.addEdge(0,1,1);
        dag.addEdge(0,2,2);
        dag.addEdge(1,3,3);
        dag.addEdge(2,3,1);
        Map<Integer, Set<Integer>> graph = new HashMap<>();
        graph.put(0, Set.of(1,2));
        graph.put(1, Set.of(3));
        graph.put(2, Set.of(3));
        graph.put(3, Set.of());
        List<Integer> topo = KahnTopoSort.topologicalSort(graph);
        int[] sp = dag.shortestPath(0, topo);
        assertEquals(0, sp[0]);
        assertEquals(3, sp[3]);
    }
    @Test
    public void testEmptyGraphSCC() {
    TarjanSCC scc = new TarjanSCC(0);
    List<List<Integer>> comps = scc.run();
    assertTrue(comps.isEmpty(), "Empty graph should have 0 SCCs");
    }
    
    @Test
    public void testSingleNodeGraph() {
    TarjanSCC scc = new TarjanSCC(1);
    List<List<Integer>> comps = scc.run();
    assertEquals(1, comps.size(), "Single node should be one SCC");
    }
}
