import sun.awt.image.ImageWatched;

import java.util.*;

/**
 * Simple Distance Vector Routing Node
 */
public class DVNode {

    private String label;
    private RoutingTable rt;
    private ArrayList<DVNeighbour> neighbours;
    private ArrayList<CVector> recievedVectors;

    public DVNode(String label){
        this.label = label;
        this.rt = new RoutingTable(this);
        neighbours = new ArrayList<DVNeighbour>();
        recievedVectors = new ArrayList<CVector>();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void addNeighbour(DVNeighbour n){
        // Check if neighbour already exists
        if (!neighbours.contains(n)){
            neighbours.add(n);
        }
    }

    public void printNeighbours(){
        System.out.println(this);
        for (DVNeighbour n : neighbours){
            System.out.println(n);
        }
    }

    public void recieveVector(CVector vector){
        recievedVectors.add(vector);
    }

    public void sendVectors(){
        // Send routing table vector to all neighbours
        // NOTE: Add split horizon here
        for (DVNeighbour n : neighbours){
            n.recieveVector(rt.getVector());
        }
    }

    public void calculateVectors(){
        // Give routing table neighbour vectors and calculate new costs
        rt.calculateNewCosts(recievedVectors, neighbours);
        recievedVectors.clear();
    }

    public DVNode getNextLink(DVNode n){
        // Get next link from routing table;
        return rt.getNextLink(n);
    }

    @Override
    public String toString() {
        return "Node {'" +
                 label + '\'' +
                '}';
    }
}


