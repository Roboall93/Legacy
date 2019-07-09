import java.util.ArrayList;
import java.util.HashMap;

/**
 * Vector class to store costs data and reference node
 */
public class CVector {

    private DVNode node;
    private HashMap<String, Integer> vector;

    public CVector(DVNode n){
        node = n;
        vector = new HashMap<String, Integer>();
    }

    public DVNode getNode() {
        return node;
    }

    public void setNode(DVNode node) {
        this.node = node;
    }

    public HashMap<String, Integer> getVector() {
        return vector;
    }

    public void clear(){
        vector.clear();
    }

    public void addCost(String label, int cost){
        vector.put(label, cost);
    }

}
