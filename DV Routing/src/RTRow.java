/**
 * Container object that represents a routing table row, storing three different elements
 */
public class RTRow {

    private String node;
    private int cost;
    private DVNode nextlink;

    public RTRow(String node, int cost, DVNode nextlink) {
        this.node = node;
        this.cost = cost;
        this.nextlink = nextlink;
    }

    public String getNode() { return node; }

    public void setNode(String node) {
        this.node = node;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public DVNode getNextlink() {
        return nextlink;
    }

    public void setNextlink(DVNode nextlink) {
        this.nextlink = nextlink;
    }
}
