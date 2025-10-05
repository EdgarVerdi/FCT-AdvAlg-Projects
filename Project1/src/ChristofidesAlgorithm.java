import java.util.*;

public class ChristofidesAlgorithm {

    record Edge(int v1, int v2){}

    public static int[] mstPrim(int[][] graph) {
        int length = graph.length;
        int[] mst = new int[length];

        int[] key = new int[length];
        Arrays.fill(key, Integer.MAX_VALUE);
        boolean[] mstSet = new boolean[length];

        key[0] = 0;
        mst[0] = -1;

        for (int count = 0; count < length - 1; count++) {
            int min = Integer.MAX_VALUE, min_index = -1;

            for (int v = 0; v < mstSet.length; v++)
                if (!mstSet[v] && key[v] < min) {
                    min = key[v];
                    min_index = v;
                }
            int u = min_index;

            mstSet[u] = true;

            for (int v = 0; v < length; v++)
                if (graph[u][v] != 0 && !mstSet[v]
                        && graph[u][v] < key[v]) {
                    mst[v] = u;
                    key[v] = graph[u][v];
                }
        }

        return mst;
    }

    public static List<Integer> findOddVertexes(int[] mst) {
        int[] tmp = new int[mst.length - 1];
        List<Integer> vertexes = new ArrayList<>();
        for (int i = 0; i < mst.length; i++)  {
            tmp[i] += 1;
            tmp[mst[i]] += 1;
        }

        for (int vertex: tmp)
            if (tmp[vertex] % 2 == 1) {
                vertexes.add(vertex);
            }
        return vertexes;
    }

    public static void minimumWeightMatching(int[] mst, int[][] graph, List<Integer> oddVertexes) {
        while (!oddVertexes.isEmpty()) {
            var vertex = oddVertexes.removeFirst();
            float length = Float.MAX_VALUE;
            var closest = 0;
            for (int u: oddVertexes)
                if  (u != vertex && graph[vertex][u] < length) {
                    length = graph[vertex][u];
                    closest = u;
                }
            mst[vertex] = closest;
            oddVertexes.remove(closest);
        }
    }

    public static int[] EulerianCircuit(int[] matchedMst, int[][] graph) {
        int[] path = new int[matchedMst.length - 1];

        Map<Integer, List<Integer>> neighbours = new HashMap<>();
        for (int i = 0; i < matchedMst.length; i++)
            if (!neighbours.get(i).contains(matchedMst[i])) {
                neighbours.get(i).add(matchedMst[i]);
                neighbours.get(matchedMst[i]).add(i);
            }
        int startVertex = 0;
        path[startVertex] = neighbours.get(startVertex).getFirst();


        return path;
    }

    public static List<Integer> HamiltonianCircuit(int[] EulerianCircuit, int[][] graph) {
        List<Integer> path = new ArrayList<>();
        return path;
    }
}
