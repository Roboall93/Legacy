/**
 * Container object that represents a routing table row, storing three different elements
 */
public class RTRow {

    private DVNode node;
    private int cost;
    private DVNode nextLink;

    public RTRow(DVNode node, int cost, DVNode nextLink) {
        this.node = node;
        this.cost = cost;
        this.nextLink = nextLink;
    }

    public DVNode getNode() { return node; }

    public void setNode(DVNode node) {
        this.node = node;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public DVNode getNextLink() {
        return nextLink;
    }

    public void setNextlink(DVNode nextlink) {
        this.nextLink = nextLink;
    }

    @Override
    public String toString() {
        if (nextLink == null){
            return  "node: '" + node.getLabel() + '\'' + ", cost: " + cost + ", nextlink: None";
        }
        else {
            return  "node: '" + node.getLabel() + '\'' + ", cost: " + cost + ", nextlink: " + nextLink.getLabel();
        }
    }
}

