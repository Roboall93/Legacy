/**
 * Distance Vector Routing edge connecting two DV Nodes with a weight
 */
public class DVEdge {

    private DVNode a;
    private DVNode b;
    private int weight;

    public DVEdge(DVNode a, DVNode b, int weight){
        this.a = a;
        this.b = b;
        this.weight = weight;
    }

    public DVNode getA() {
        return a;
    }

    public void setA(DVNode a) {
        this.a = a;
    }

    public DVNode getB() {
        return b;
    }

    public void setB(DVNode b) {
        this.b = b;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "a=" + a +
                ", b=" + b +
                ", weight=" + weight +
                '}';
    }
}
