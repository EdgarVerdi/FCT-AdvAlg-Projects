import java.util.*;

public class MaximumIndependentSet {
    class Node implements Comparable<Node> {
        int object;
        int priority;

        public Node(int object, int priority) {
            this.object = object;
            this.priority = priority;
        }

        @Override
        public int compareTo(Node o) {
            if (priority != o.priority)
                return priority - o.priority;
            else
                return object - o.object;
        }
    }
    //https://dl.acm.org/doi/pdf/10.1145/195058.195221
    public Set<Integer> greedy(List<List<Integer>> adjacencyList){
        /*
        Greedy(G):
        S = {}
        While G is not empty:
            Let v be a node with minimum degree in G
            S = union(S, {v})
            remove v and its neighbors from G
        return S
        */
        Set<Integer> removed = new HashSet<>();
        Set<Integer> answer = new HashSet<>();
        PriorityQueue<Node> queue = new PriorityQueue<>(adjacencyList.size()*adjacencyList.size());
        int[] currDegree = new int[adjacencyList.size()];
        for (int i = 0; i < adjacencyList.size(); i++) {
            int iDegree = adjacencyList.get(i).size();
            queue.add(new Node(i, iDegree));
            currDegree[i] = iDegree;
        }
        while (removed.size() < adjacencyList.size()){
            int vertex = queue.remove().object;
            if (removed.contains(vertex)) continue;
            answer.add(vertex);
            removed.add(vertex);
            for (int adjacentVertex : adjacencyList.get(vertex)){
                if (removed.contains(adjacentVertex)) continue;
                removed.add(adjacentVertex);
                for (int adjacentSqVertex : adjacencyList.get(adjacentVertex)) {
                    if (removed.contains(adjacentSqVertex)) continue;
                    queue.add(new Node(adjacentSqVertex, --currDegree[adjacentSqVertex]));
                }
            }
        }
        return answer;
    }



    public Set<Integer> greedy2(List<List<Integer>> adjacencyList){
        /*
        Greedy(G):
        S = {}
        While G is not empty:
            Let v be a node with minimum degree in G
            S = union(S, {v})
            remove v and its neighbors from G
        return S
        */
        Set<Integer> removed = new HashSet<>();
        Set<Integer> answer = new HashSet<>();
        int[] currDegree = new int[adjacencyList.size()];
        for (int i = 0; i < adjacencyList.size(); i++)
            currDegree[i] = adjacencyList.get(i).size();
        while (removed.size() < adjacencyList.size()){
            int lowerDegree = currDegree[0];
            int vertex = 0;
            for (int i = 1; i < currDegree.length; i++)
                if ((currDegree[i] < lowerDegree) && !removed.contains(i)) {
                    vertex = i;
                    lowerDegree = currDegree[i];
                }
            answer.add(vertex);
            removed.add(vertex);
            for (int adjacentVertex : adjacencyList.get(vertex)){
                if (removed.contains(adjacentVertex)) continue;
                removed.add(adjacentVertex);
                for (int adjacentSqVertex : adjacencyList.get(adjacentVertex))
                    currDegree[adjacentSqVertex]--; // even if it was already removed it doesn't matter
            }
        }
        return answer;
    }

    public Set<Integer> greedyNoted(List<List<Integer>> adjacencyList){
        Set<Integer> removed = new HashSet<>();                     //O(1)
        Set<Integer> answer = new HashSet<>();                      //O(1)
        int s = adjacencyList.size()*adjacencyList.size();
        PriorityQueue<Node> queue = new PriorityQueue<>(s);         //O(V)      //O(V^2)
        int[] currDegree = new int[adjacencyList.size()];           //O(1)
        for (int i = 0; i < adjacencyList.size(); i++) {            //V times       O(V log(V))
            int iDegree = adjacencyList.get(i).size();                  //O(1)
            queue.add(new Node(i, iDegree));                            //O(log(V)
            currDegree[i] = iDegree;                                    //O(1)
        }
        while (removed.size() < adjacencyList.size()){              //worse case is V-1 O(V^3 log(V))
            int vertex = queue.remove().object;                         //O(1)
            if (removed.contains(vertex)) continue;                     //O(1)
            answer.add(vertex);                                         //O(1)
            removed.add(vertex);                                        //O(1)
            for (int adjacentVertex : adjacencyList.get(vertex)){       // worse case is V-1 O(V^2 log(V))
                if (removed.contains(adjacentVertex)) continue;             //O(1)
                removed.add(adjacentVertex);                                //O(1)
                for (int adjacentSqVertex : adjacencyList.get(adjacentVertex)) { // worse case is V-1 (O(V log(V)))
                    if (removed.contains(adjacentSqVertex)) continue;       //O(1)
                    queue.add(new Node(adjacentSqVertex, --currDegree[adjacentSqVertex]));//O(log V)
                }
            }
        }
        return answer;
    }

    public List<List<Integer>> example1() {
        return List.of(
                List.of(1, 2, 4, 6),        //0
                List.of(0, 3, 4, 7),        //1
                List.of(0, 3, 4, 8),        //2
                List.of(1, 2, 4, 9),        //3
                List.of(0, 1, 2, 3, 5),     //4
                List.of(4),             //5
                List.of(0, 8),              //6
                List.of(1, 9),              //7
                List.of(2, 6),              //8
                List.of(3, 7)               //9
        );
    }
}
