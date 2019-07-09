import java.util.*;

/**
 * Abstracted Data Structure for nodes to hold routing information
 */
public class RoutingTable {

    private LinkedList<RTRow> table;
    private CVector vector;
    private DVNode tableNode;

    public RoutingTable(DVNode n) {
        table = new LinkedList<RTRow>();
        // Add self to table with cost 0
        table.add(new RTRow(n, 0, null));
        vector = new CVector(n);
        tableNode = n;
    }

    // Helper function to find the minimum item in a list
    public static int minIndex (ArrayList<Integer> list) {
        return list.indexOf (Collections.min(list)); }

    public void addRow(RTRow row) {
        table.add(row);
    }

    public void deleteRow(DVNode n) {
        for (RTRow r : table){
            if (r.getNode().equals(n)){
                table.remove(table.indexOf(r));
            }
        }
    }

    public void changeRow(DVNode n) {
        for (RTRow r : table){
            if (r.getNode().equals(n)){
                r.setCost(Integer.MAX_VALUE);
            }
        }
    }


    public CVector getVector(DVNeighbour n, boolean split){
	   //System.out.println(tableNode.getLabel() + " creating vector for " + n.getNeighbour().getLabel());
        // Clear previous vector information
        vector = new CVector(tableNode);
        // Gather data from cost column
        if (!table.isEmpty()) {
            for (RTRow r : table) {
                // Check for split horizon to avoid loops
                if (split && n.getNeighbour().equals(r.getNextLink())){
				//System.out.println("Omitting: "+ r);
                    continue;
                }
			 //System.out.println("Adding: "+ r);
                vector.addCost(r.getNode(), r.getCost());
            }
        }
        return vector;
    }

    public DVNode getNextLink(DVNode n){
        for (RTRow r : table){
            if (r.getNode() == n){
                return r.getNextLink();
            }
        }
        return null;
    }

    // Convert entire table to string for comparison;
    public String convertToString(){
        StringBuffer sb = new StringBuffer();
        for (RTRow r : table) {
            sb.append(r);
        }
        return sb.toString();
    }

    // Get the routing table in a readable manner
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("-----------------------------\n");
        sb.append("Table for " + tableNode.getLabel() + "\n");
        for (RTRow r : table){
            sb.append(r.toString() + "\n");
        }
        sb.append("Stable: " + tableNode.isStable() + "\n");
        sb.append("-----------------------------\n");
        return sb.toString();
    }

    public void calculateNewCosts(ArrayList<CVector> vectors, ArrayList<DVNeighbour> neighbours) {
        // Update routing table based on cost data from neighbours
        // Clear current costs
        String tableString = this.convertToString();
        table = new LinkedList<RTRow>();
        // Add node back into its own table;
        table.add(new RTRow(tableNode, 0, null));
        HashMap<DVNode, ArrayList<vectorElement>> knownRoutes = new HashMap<DVNode, ArrayList<vectorElement>>();
        for (CVector cv : vectors) {
            int totalcost = 0;
            for (DVNode n: cv.getVector().keySet()){
                if (n.getLabel().equals(tableNode.getLabel())){
                    continue;
                }
                // If we don't have an entry for the node we're asking about, add it
                if (!knownRoutes.containsKey(n)){
                    knownRoutes.put(n, new ArrayList<vectorElement>());
                }
                // Add the node being checked (for next link), and its total cost, factoring in neighbour links
                // Check if the neighbour exists
                boolean found = false;
                for (DVNeighbour nb : neighbours){
                    if (nb.getNeighbour().getLabel().equals((cv.getNode().getLabel()))){
                        // check for broken links
                        if (nb.getCost() == Integer.MAX_VALUE || cv.getVector().get(n) == Integer.MAX_VALUE) {
                            totalcost = Integer.MAX_VALUE;
                        }
				        else {
                            totalcost = nb.getCost() + cv.getVector().get(n);
                        }
                        found = true;
                    }
                }
			 // Add this item as a new vectorElement under node key (Node: {<NextLink, Cost>,...})
                if (found){
                    knownRoutes.get(n).add(new vectorElement(cv.getNode(), totalcost));
                }
                else {
                    System.out.println("Neighbour not found: " + cv.getNode());
                }
            }
        }
        // We should now have a list of all reachable nodes, with a list of path cost and next link nodes
        ArrayList<vectorElement> routes = new ArrayList<vectorElement>();
        int minValue = Integer.MAX_VALUE;
        DVNode thisNode = null;
        DVNode minNode = null;
        // First, find the minimum value and the next link node
        for (DVNode key : knownRoutes.keySet()){
            routes = knownRoutes.get(key);
		  // System.out.println("Key: " + key);
            for (vectorElement ve : routes){
			// System.out.println("Node: " + ve.getNode().getLabel() + "; Cost: " + ve.getCost());
                if (ve.getCost() < minValue){
                    minValue = ve.getCost();
                    minNode = ve.getNode();
                }
            }
            // Found lowest cost for this key, add it to table
            thisNode = key;
            RTRow row = new RTRow(thisNode, minValue, minNode);
            table.add(row);
            minValue = Integer.MAX_VALUE;
        }
	   // Check if current table matched the previous table
        String newTableString = convertToString();
        if (tableString.equals(newTableString)){
            tableNode.setStable(true);
        }
	   else {
		   tableNode.setStable(false);
		}
    }
}

// Temporary class for storing vector elements
class vectorElement {
    private DVNode node;
    private int cost;

    public vectorElement(DVNode n, int c){
        node = n ;
        cost = c;
    }

    public DVNode getNode(){
        return node;
    }

    public int getCost(){
        return cost;
    }
}


