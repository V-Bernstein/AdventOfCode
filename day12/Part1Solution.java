import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.lang.StringBuilder;

class Node {
    private String value;
    private List<Node> edgesOut;
    private boolean isSmall;

    public Node(String value, boolean isSmall) {
        this.value = value;
        this.edgesOut = new ArrayList<Node>();
        this.isSmall = isSmall;
    }

    public String getValue() {
        return this.value;
    }

    public List<Node> getEdges() {
        return this.edgesOut;
    }

    public void addEdge(Node node) {
        this.edgesOut.add(node); // TODO: Check for node first?
    }

    public boolean isSmall() {
        return this.isSmall;
    }

    public boolean isStart() {
        return this.value.equals("start");
    }

    public boolean isEnd() {
        return this.value.equals("end");
    }

    public void print() {
        System.out.println("Val: " + this.value + " isSmall: " + this.isSmall);
        for (Node n : edgesOut) {
            System.out.println(this.value + " <-> " + n.getValue());
        }
    }
    
    @Override
    public int hashCode() {
        return this.value.hashCode(); // Each cave should be the same cave if the value is the same
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Node) {
            Node other = (Node) o;
            return this.value.equals(other.getValue());
        }
        return false;
    }
}

class Graph {
    private Map<Node, Node> nodes;

    public Graph() {
        this.nodes = new HashMap<>();
    }

    public Node getNode(Node node) {
        if (!this.nodes.containsKey(node)) {
            return null;
        }
        return this.nodes.get(node);
    }

    public Node getStartNode() {
        Node temp = new Node("start", false);
        if (!this.nodes.containsKey(temp)) {
            return null;
        }
        return this.nodes.get(temp);
    }

    public void addNode(Node node) {
        this.nodes.put(node, node);
    }

    public void print() {
        for (Node n : nodes.values()) {
            n.print();
        }
    }
}

public class Part1Solution {
    //private final static String FILE_NAME = "input.txt";
    private final static String FILE_NAME = "in.txt";

    public static void main(String[] args) {
        Graph caves = parseInput(FILE_NAME);
        if (caves == null) {
	        System.out.println("Error parsing file");
	        return;
	    }

        caves.print();
        List<String> paths = findPaths(caves);
        System.out.println(paths);
        System.out.println("Solution is: " + paths.size());
    }

    private static List<String> findPaths(Graph caves) {
        Node startNode = caves.getStartNode();
        if (startNode == null) {
            System.out.println("No start node!");
            return null;
        }
        List<String> paths = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        List<String> path = new ArrayList<>();
        findPathsHelper(caves, paths, visited, startNode, path);
        return paths;
    }

    // return string which records path?
    private static void findPathsHelper(Graph caves, List<String> paths, Set<String> visited, Node curNode, List<String> path) {
        if (curNode == null) {
            return;
        }

        if (curNode.isEnd()) {
            path.add("end");
            paths.add(prettifyPath(path));
            path.clear();
            return;
        }

        if (curNode.isSmall() && visited.contains(curNode.getValue())) {
            System.out.println("is small and visited");
            path.clear();
            return;
        }

        visited.add(curNode.getValue());
        path.add(curNode.getValue());

        System.out.println("path: " + curNode.getValue());

        for (Node cave : curNode.getEdges()) {
            if (cave.isStart()) { // Don't go back to start
                continue;
            }
            findPathsHelper(caves, paths, visited, cave, path);
        }
    }

    private static String prettifyPath(List<String> path) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < path.size(); i++) {
            sb.append(path.get(i));
            if (i < path.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    private static Graph parseInput(String fileName) {
        Graph graph = new Graph();
        try(FileReader fr = new FileReader(fileName)) {
           BufferedReader br = new BufferedReader(fr);
           String line;
	       while ((line = br.readLine()) != null) {
            String[] caves = line.split("-");
            Node startCave = new Node(caves[0], isSmallCave(caves[0]));
            Node endCave = new Node(caves[1], isSmallCave(caves[1]));
            Node existingStartCave = graph.getNode(startCave);
            if (existingStartCave != null) {
                existingStartCave.addEdge(endCave);
            } else {
                startCave.addEdge(endCave);
                graph.addNode(startCave);
            }

            Node existingEndCave = graph.getNode(endCave);
            if (existingEndCave != null) {
                existingEndCave.addEdge(startCave);
            } else {
                endCave.addEdge(startCave);
                graph.addNode(endCave);
            }
           }
    	} catch(Exception e) {
	       System.out.println(e);
           return null;
	    }
    	return graph;
   }

    private static boolean isSmallCave(String s) {
        if (s == null) {
            return false;
        }
        return !(s.equals("start") || s.equals("end")) && s.toLowerCase().equals(s);
    }
}
