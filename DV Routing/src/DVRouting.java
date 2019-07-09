import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class DVRouting {

public static void main (String[] args){

    if (args.length == 0 ){
        System.out.println("Program usage: 'java DVRouting \"C:/input/file/path\"'");
    }

    // The name of the file to open.
    String fileName = args[0];

    // Holds one line at a time
    String line = null;

    try {
        // Read the file
        FileReader fr =
                new FileReader(fileName);

        // Always wrap FileReader in BufferedReader.
        BufferedReader br =
                new BufferedReader(fr);

        // Grab first line ( which should be list of nodes)
        line = br.readLine();
        // Strip brackets { }, split into list
        line.replaceAll("[\\[\\](){}]","");
        String[] nodes = line.split(",");

        //Create the nodes, add to list
        ArrayList<DVNode> nodeList = new ArrayList<DVNode>();
        for (String label: nodes){
            nodeList.add(new DVNode(label));
        }

        ArrayList<DVEdge> edgeList = new ArrayList<DVEdge>();
        // Get the edges
        while((line = br.readLine()) != null) {
            line.replaceAll("[\\[\\](){}]","");
            String[] edge = line.split(",");
            // Get nodes on the edge
            DVNode a = null;
            DVNode b = null;
            for (DVNode n: nodeList){
                if (n.getLabel() == edge[0]){
                    a = n;
                }
                else if(n.getLabel() == edge[1]){
                    b = n;
                }
            }
            if (a == null || b == null) // Could not find nodes
                System.out.println("Could not resolve edge between: " + a.getLabel() + " - " + b.getLabel());
            else {
                //Add edge to list
                int cost = Integer.parseInt(edge[2]);
                DVEdge newEdge = new DVEdge(a, b, cost);
                edgeList.add(newEdge);
                //Add node as neighbour to each node
                a.addNeighbour(new DVNeighbour(b, cost));
                b.addNeighbour(new DVNeighbour(a, cost));
            }
        }
        // Create the graph
        DVGraph routingGraph = new DVGraph(nodeList, edgeList);
        System.out.println("Graph constructed successfully");
        System.out.println(routingGraph);
        br.close();
    }
    catch(FileNotFoundException ex) {
        System.out.println(
                "Unable to open file '" +
                        fileName + "'");
    }
    catch(IOException ex) {
        System.out.println(
                "Error reading file '"
                        + fileName + "'");
    }
}

}
