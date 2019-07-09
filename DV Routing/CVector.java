import java.util.ArrayList;
import java.util.HashMap;

/**
 * Vector class to store costs data and reference node
 */
public class CVector {

    private DVNode node;
    private HashMap<DVNode, Integer> vector;

    public CVector(DVNode n){
        node = n;
        vector = new HashMap<DVNode, Integer>();
    }

    public DVNode getNode() {
        return node;
    }

    public void setNode(DVNode node) {
        this.node = node;
    }

    public HashMap<DVNode, Integer> getVector() {
        return vector;
    }

    public void clear(){
        vector.clear();
    }

    public void addCost(DVNode node, int cost){
        vector.put(node, cost);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Vector " + node.getLabel() + "\n");
        for (DVNode key : vector.keySet()) {
            sb.append("Node: " + key.getLabel() + "; Cost: " + vector.get(key) + "\n");
        }
        return sb.toString();
    }
}

