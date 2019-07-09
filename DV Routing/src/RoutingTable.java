import java.util.*;

/**
 * Abstracted Data Structure for nodes to hold routing information
 */
public class RoutingTable {

    private LinkedList<RTRow> table;
    private CVector vector;

    public RoutingTable(DVNode n) {
        table = new LinkedList<RTRow>();
        vector = new CVector(n);
    }

    public static int minIndex (ArrayList<Integer> list) {
        return list.indexOf (Collections.min(list)); }

    public void addRow(RTRow row) {
        table.add(row);
    }

    public void deleteRow(RTRow row) {
        table.remove(table.indexOf(row));
    }

    public CVector getVector(){
        // Clear previous vector information
        vector.clear();
        // Gather data from cost column
        if (!table.isEmpty()) {
            for (RTRow r : table) {
                vector.addCost(r.getNode(), r.getCost());
            }
        }
        return vector;
    }

    public DVNode getNextLink(DVNode n){
        for (RTRow r : table){
            if (r.getNode() == n.getLabel()){
                return r.getNextlink();
            }
        }
        return null;
    }

    public void calculateNewCosts(ArrayList<CVector> vectors, ArrayList<DVNeighbour> neighbours) {
        // Update routing table based on cost data from neighbours
        // Clear current costs
        table.clear();
        ArrayList<String> processedNodes = new ArrayList<String>();
        ArrayList<Integer> costlist = new ArrayList<Integer>();
        CVector firstVector = vectors.get(0);
        for (String label : firstVector.getVector().keySet()){
            if (!processedNodes.contains(label)){
                // add to list of processed nodes so we don't do it again
                processedNodes.add(label);
                int cost = firstVector.getVector().get(label);
                // find which neighbour node this is and add the cost
                for (DVNeighbour n : neighbours){
                    if (n.getNeighbour() == firstVector.getNode()){
                        cost += n.getCost();
                        break;
                    }
                }
                costlist.add(cost);
                // add all the costs for the same node from other vectors to the list
                for (int i = 1; i < vectors.size(); i++){
                    // find which node
                    CVector v = vectors.get(i);
                    DVNode node = v.getNode();
                    cost = v.getVector().get(label);
                    for (DVNeighbour n : neighbours){
                        if (n.getNeighbour() == v.getNode()){
                            cost += n.getCost();
                            break;
                        }
                    }
                    costlist.add(cost);
                    // we now have a list of all vectors for this node (with total costs)
                    // find index of lowest value
                    int lindex = minIndex(costlist);
                    int lowestCost = costlist.get(lindex);
                    DVNode nextNode = vectors.get(lindex).getNode();
                    // Construct new table entry with this data
                    table.add(new RTRow(label, lowestCost, nextNode));
                }
            }
            // iterate keyset
        }

    }

}
