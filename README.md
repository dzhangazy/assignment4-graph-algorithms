# Assignment 4 – Graph Algorithms (SCC, Topological Sort, DAG Paths)


**Student:** Jangazy Bakytzhan
**Course:** Design and Analysis of Algorithms (Assignment 4)


## 📘 Overview

This project implements and analyzes **three core graph algorithms** with full **instrumentation and metrics collection**:

1. **Strongly Connected Components (Tarjan’s Algorithm)**
2. **Topological Sort (Kahn’s Algorithm)**
3. **Shortest & Longest Paths in a Directed Acyclic Graph (DAG)**

All results are automatically processed and benchmarked across multiple datasets stored in the `/data/` directory.



## 🧩 1. Strongly Connected Components (TarjanSCC)

**Algorithm:** Tarjan’s DFS-based algorithm for SCC detection.  
**Input:** Directed graph from `.json` file.  
**Output:**

- List of strongly connected components
- Size of each component
- Condensation DAG built from SCCs

**Metrics Recorded:**

- DFS calls
- Edges visited
- Stack pops
- Execution time (in nanoseconds)


## 🔁 2. Topological Sort (KahnTopoSort)

**Algorithm:** Kahn’s algorithm based on in-degree counting and queue processing.  
**Input:** Condensation DAG (built from SCCs).  
**Output:** Valid topological order of components.

**Metrics Recorded:**

- Number of edges processed
- Push and pop operations
- Initial pushes (zero in-degree nodes)
- Execution time (in nanoseconds)


## 🧮 3. Shortest and Longest Paths in a DAG

**Algorithm:**

- **Shortest Path:** Dynamic programming relaxation in topological order.
- **Longest Path:** Maximum DP relaxation using the same order.

**Choice Justification:**  
The algorithm uses **edge weights** (not node weights) as **task durations**.

**Output:**

- Shortest distances from source
- Longest (critical) path length
- One reconstructed optimal path (if required)

**Metrics Recorded:**

- Relaxation attempts and successes
- Execution time (in nanoseconds)


## ⏱ 4. Instrumentation & Metrics

A common interface `Metrics` and its implementation `BasicMetrics` are used to collect:

- Operation counters (DFS, relaxations, pushes, pops)
- Timing using `System.nanoTime()`
- Output saved to `metrics_report.csv`

**Example of CSV Output:**
Algorithm,Time(ns),Operations
TarjanSCC,197600,DFS=24,Edges=29,Pops=24
KahnTopo,141600,Edges=17,Pushes=12,Pops=17
DAGShortest,37100,Attempts=4,Success=3
DAGLongest,19400,Attempts=4,Success=4



## 🧾 5. Dataset Generation

All datasets are stored under the `/data/` folder.

| Category   | Size (n) | Description                    | Variants |
| ---------- | -------- | ------------------------------ | -------- |
| **Small**  | 6–10     | Simple cycles or small DAGs    | 3        |
| **Medium** | 10–20    | Mixed graphs with several SCCs | 3        |
| **Large**  | 20–50    | Performance and timing tests   | 3        |

Each JSON file follows this structure:

```json
{
  "n": 10,
  "directed": true,
  "source": 0,
  "edges": [
    {"u": 0, "v": 1, "w": 5},
    {"u": 1, "v": 2, "w": 3}
  ]
}
```

🧪 6. Unit Testing (JUnit)

Located in src/test/java/graph/GraphTests.java

Includes tests for:

SCC correctness on small graphs

Topological order validity

Shortest/longest path consistency

Edge cases: disconnected nodes, single-component graphs

Run tests:

mvn test


Expected output:

Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS

⚙️ 7. Running the Program

Build and execute using Maven:

mvn compile

mvn exec:java -D"exec.mainClass=Main"


Example Output:

📂 Analyzing file: large_1.json
Strongly Connected Components:
  SCC 0: [9, 20, 17, 19]
Topological order of SCC DAG: [10, 13, 14, ...]
Tarjan SCC metrics: Time(ns): 197600, DFS: 24, Edges: 29
Kahn TopoSort metrics: Time(ns): 141600, Edges: 17, Pops: 17
DAG Shortest Path metrics: Time(ns): 37100
✅ Metrics successfully written to metrics_report.csv

📁 8. Project Structure
assignment4-graph-algorithms/
├── src/
│   ├── main/java/
│   │   ├── graph/
│   │   │   ├── scc/TarjanSCC.java
│   │   │   ├── topo/KahnTopoSort.java
│   │   │   └── dagsp/DAGShortestPaths.java
│   │   ├── metrics/
│   │   │   ├── Metrics.java
│   │   │   └── BasicMetrics.java
│   │   └── Main.java
│   └── test/java/graph/GraphTests.java
├── data/
│   ├── small_1.json
│   ├── small_2.json
│   ├── medium_1.json
│   ├── large_1.json
│   └── ...
├── metrics_report.csv
├── pom.xml
└── README.md

📊 9. Results Summary
Dataset	SCCs	Topo Edges	Shortest (ns)	Longest (ns)
small_1.json	3	1	9.1k	8.0k
medium_2.json	7	7	5.3k	5.1k
large_2.json	18	17	10.4k	7.9k

🧩 10. Conclusion

✅ The project fully meets the requirements of Assignment 4:

Implemented SCC (Tarjan), TopoSort (Kahn), and DAG Paths

Collected timing and operation metrics

Tested on multiple datasets (small, medium, large)

Produced automated CSV reports

Includes JUnit test coverage

Result: ✅ 100% complete and ready for submission.

