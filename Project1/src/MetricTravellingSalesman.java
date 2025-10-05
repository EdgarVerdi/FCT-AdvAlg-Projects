import java.util.*;

class FullGraph {
    int[] edgeWeights;
    int numVertices;
    int[][] fullMatrix = null;

    FullGraph(int[] edgeWeights){
        this.edgeWeights = edgeWeights;
        numVertices = (1 + (int) Math.sqrt(1+8*edgeWeights.length))/2;
        fullMatrix = buildWeightMatrix();
    }

    public int[][] buildWeightMatrix(){
        int[][] matrix = new int[numVertices][numVertices];
        int reading = 0;
        for (int i = 0; i<numVertices; i++)
            for (int j = i+1; j<numVertices; j++)
                matrix[i][j] = matrix[j][i] = edgeWeights[reading++];
        return matrix;
    }

    public boolean isMetric(int[] edges){
        for (int i = 0; i<numVertices; i++)
            for (int j = i+1; j<numVertices; j++)
                for (int k = 0; i<numVertices; i++)
                    if (fullMatrix[i][j] > fullMatrix[i][k] + fullMatrix[k][j])
                        return false;
        return true;
    }

    public int numNodes(){
        return numVertices;
    }

    public int cost(int a, int b){
        return fullMatrix[a][b];
    }
}

record PriorityNode(int object, int priority) implements Comparable<PriorityNode> {
    @Override
    public int compareTo(PriorityNode o) {
        return priority != o.priority ? priority - o.priority : object - o.object;
    }
}

record Edge(int v1, int v2){}

public class MetricTravellingSalesman {

    int[] test(){
        return new int[]{67, 201, 267, 200, 34, 134, 200, 100, 34, 134, 200, 267, 101, 201, 133, 33, 33, 266, 267, 167,
                67, 67, 167, 167, 133, 299, 333, 199, 167, 233, 166, 200, 332, 300, 234, 100, 234, 134, 68, 134, 234,
                168, 100, 166, 66
        };
    }

    int[] testSlides(){
        return new int[]{1, 3, 3, 1, 2, 4, 1, 4, 3, 3};
    }

    Edge[] mstPrim( FullGraph graph ) {
        Edge[] mst = new Edge[ graph.numNodes() - 1 ];
        int mstSize = 0;
        boolean[] selected = new boolean[ graph.numNodes() ];
        int[] cost = new int[ graph.numNodes() ];
        Edge[] via = new Edge[ graph.numNodes() ];
        PriorityQueue<PriorityNode> connected = new PriorityQueue<>( graph.numNodes() );
        for (int v = 0; v < graph.numNodes(); v++) {
            selected[v] = false;
            cost[v] = Integer.MAX_VALUE;
        }
        int origin = 0;
        cost[origin] = 0;
        connected.add(new PriorityNode(origin, 0));
        do {
            int node = connected.remove().object();
            if (selected[node]) continue;
            selected[node] = true;
            if ( node != origin )
                mst[ mstSize++ ] = via[node];
            exploreNode(graph, node, selected, cost, via, connected);
        }
        while ( !connected.isEmpty() && mstSize < graph.numNodes()-1);
        return mst;
    }

    void exploreNode( FullGraph graph, int source, boolean[] selected, int[] cost, Edge[] via, PriorityQueue<PriorityNode> connected ) {
        for (int node = 0; node < graph.numNodes(); node++){
            if (node == source) continue;
            int edgeCost = graph.cost(node, source);
            if ( !selected[node] && (edgeCost < cost[node]) ) {
                boolean nodeIsInQueue = cost[node] < Integer.MAX_VALUE;
                cost[node] = edgeCost;
                via[node] = new Edge(source, node);// (edge: node, source)
                /*
                if ( nodeIsInQueue )
                    connected.decreaseKey(node, cost[node]);
                else
                    connected.insert(cost[node], node);
                 */
                connected.add(new PriorityNode(node, cost[node]));
            }
        }
    }

    @SuppressWarnings("unchecked")
    List<Integer>[] rebuildGraphFromMst (Edge[] mst){
        int verticesNum = mst.length+1;
        List<Integer>[] graph = (List<Integer>[]) new List[verticesNum];

        for (Edge edge: mst){
            List<Integer> l = graph[edge.v1()];
            if (l == null){
                l = new LinkedList<>();
                graph[edge.v1()] = l;
            }
            l.add(edge.v2());
        }
        return graph;
    }

    void printEdges(Edge[] edges){
        for(Edge edge : edges)
            System.out.printf("%d %d\n", edge.v1(), edge.v2());
    }

    int solveMetricTSP1(FullGraph graph){
        List<Integer>[] primGraph = rebuildGraphFromMst(mstPrim(graph));
        Deque<Integer> stack = new LinkedList<>();
        stack.add(0);
        int totalCost = 0;
        int last = 0;
        while (!stack.isEmpty()){
            int curr = stack.removeLast();
            totalCost += graph.cost(curr, last);
            List<Integer> nextNodes = primGraph[curr];
            if (nextNodes != null)
                //reversed here is not really necessary, but will yeild different results
                stack.addAll(primGraph[curr].reversed());
            last = curr;
        }
        totalCost += graph.cost(0, last);
        return totalCost;
    }
}

