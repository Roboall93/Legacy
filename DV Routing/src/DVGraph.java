import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Undirected graph representing a simple network
 */
public class DVGraph {

    private HashMap<String, DVNode> nodes;
    private ArrayList<DVEdge> edges;

    public DVGraph(ArrayList<DVNode> nodeList, ArrayList<DVEdge> edgeList) {
        this.edges = edgeList;
        // Put nodes in list into hashmap with label key
        for (DVNode n : nodeList){
            nodes.put(n.getLabel(), n);
        }
    }

    public void doExchange(){
        // Get all nodes to send vectors to direct neighbours
        for (String key : nodes.keySet()){
            nodes.get(key).sendVectors();
        }
        // Get nodes to update costs
        for (String key : nodes.keySet()){
            nodes.get(key).calculateVectors();
        }
    }

    // Print all nodes and edges in graph
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Distance Vector Routing Graph\n");
        sb.append("Nodes:\n");
        for (String key : nodes.keySet()){
            sb.append(nodes.get(key) + "\n");
        }
        sb.append("Edges:\n");
        for (DVEdge e : edges){
            sb.append(e + "\n");
        }
        sb.append("END\n");
        return sb.toString();
    }
}
