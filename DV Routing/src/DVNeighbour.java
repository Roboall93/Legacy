/**
 * Data class to store direct links and their costs
 */
public class DVNeighbour {

    private DVNode neighbour;
    private int cost;

    public DVNeighbour(DVNode n, int c){
        neighbour = n;
        cost = c;
    }

    public DVNode getNeighbour() {
        return neighbour;
    }

    public void setNeighbour(DVNode neighbour) {
        this.neighbour = neighbour;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void recieveVector(CVector c){
        neighbour.recieveVector(c);
    }

    @Override
    public String toString() {
        return "Neighbour{" +
                "neighbour=" + neighbour.getLabel() +
                ", cost=" + cost +
                '}';
    }
}
