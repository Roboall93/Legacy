import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Undirected graph representing a simple network
 */
public class DVGraph {

    private HashMap<String, DVNode> nodes;
    private ArrayList<DVEdge> edges;
    private boolean stable;
    private boolean split;

    public DVGraph(ArrayList<DVNode> nodeList, ArrayList<DVEdge> edgeList, boolean splitH) {
        edges = edgeList;
        // Put nodes in list into hashmap with label key
        nodes = new HashMap<String, DVNode>();
        for (DVNode n : nodeList){
            nodes.put(n.getLabel(), n);
        }
        stable = false;
        split = splitH;
    }

    public boolean isStable() {
        return stable;
    }

    public void setStable(boolean stable) {
        this.stable = stable;
    }

    public boolean isSplit() {
        return stable;
    }

    public void setSplit(boolean split) {
        this.split = split;
    }

    // Check if all nodes in the graph are stable
    public void checkStability(){
        for (String k: nodes.keySet()){
            if (nodes.get(k).isStable()){
                setStable(true);
            }
            else {
                setStable(false);
            }
        }
    }

    public void doExchange(){
        // Get all nodes to send vectors to direct neighbours
        for (String key : nodes.keySet()){
            nodes.get(key).sendVectors(split);
        }
        // Get nodes to update costs
        for (String key : nodes.keySet()){
            nodes.get(key).calculateVectors();
        }
    }

    public void printNodes(){
        for (String key : nodes.keySet()){
            System.out.println(key);
        }
    }

    public void printNeighbours(){
        for (String key : nodes.keySet()){
            nodes.get(key).printNeighbours();
        }
    }

    // Print a node's routing table
    public void printRoutingTable(String s){
        DVNode n = nodes.get(s);
        System.out.println(n.getRt().toString());
    }

    public void failLink(String a, String b){
	   // Get the nodes
        DVNode nodeA = nodes.get(a);
        DVNode nodeB = nodes.get(b);
        DVNeighbour failedNode = null;
        // find the neighbour in each list and delete it
        for (DVNeighbour n : nodeA.getNeighbours()){
            if (n.getNeighbour().equals(nodeB)){
                failedNode = n;
            }
        }
        nodeA.removeNeighbour(failedNode);
        // Also remove it from the routing table;
        nodeA.getRt().changeRow(failedNode.getNeighbour());
        // Do the same with the other side of the link
        for (DVNeighbour n : nodeB.getNeighbours()){
            if (n.getNeighbour().equals(nodeA)){
                failedNode = n;
            }
        }
        nodeB.removeNeighbour(failedNode);
        nodeB.getRt().changeRow(failedNode.getNeighbour());
    }

    public void changeLink(String a, String b, int c){
        DVNode nodeA = nodes.get(a);
        DVNode nodeB = nodes.get(b);
        DVNeighbour changedNode = null;
        // find the neighbour in each list and change the cost
        for (DVNeighbour n : nodeA.getNeighbours()){
            if (n.getNeighbour().equals(nodeB)){
                n.setCost(c);
            }
        }
        // Do the same with the other side of the link
        for (DVNeighbour n : nodeB.getNeighbours()){
            if (n.getNeighbour().equals(nodeA)){
                n.setCost(c);
            }
        }
    }

    public String findPath(String a, String b){
        // get the nodes we need from the node list
        DVNode nodeA = nodes.get(a);
        DVNode nodeB = nodes.get(b);
        boolean found = false;
        DVNode currentNode = null;
        // List to store the path we create
        LinkedList<DVNode> nodePath = new LinkedList<DVNode>();
        nodePath.add(nodeA);
        currentNode = nodeA.getNextLink(nodeB);
        // Check if a link to the node exists in the table
        if (currentNode == null){
            return "No link between " + nodeA.getLabel() + " and " + nodeB.getLabel();
        }
        else {
            // There is a link, loop until we reach it and push the path into the list
            while (!found){
                nodePath.add(currentNode);
                if (currentNode.equals(nodeB)){
                    found = true;
                }
                currentNode = currentNode.getNextLink(nodeB);
            }
        }
        // Print out the path to a buffer, and return it
        StringBuffer sb = new StringBuffer();
        sb.append("Path from " + nodeA.getLabel() + " to " + nodeB.getLabel() + "\n");
        for (DVNode n : nodePath){
            if (n.equals(nodeB)){
                sb.append(n.getLabel());
            }
            else {
                sb.append(n.getLabel() + " => ");
            }
        }
        return sb.toString();
    }

    // Print all nodes and edges in graph
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Distance Vector Routing Graph\n");
        sb.append("Nodes:\n");
        for (String key : nodes.keySet()){
            sb.append(nodes.get(key).toString() + "\n");
        }
        sb.append("Edges:\n");
        for (DVEdge e : edges){
            sb.append(e.toString() + "\n");
        }
        sb.append("END\n");
        return sb.toString();
    }
}

