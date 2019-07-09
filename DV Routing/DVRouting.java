import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class DVRouting {

    public static void main (String[] args){

        if (args.length == 0 ){
            System.out.println("Program usage: 'java DVRouting C:/input/file/path'");
            System.exit(0);
        }

        // Various argument trackers and flags (We need to track everything the user could enter!)
        String arg;
        int i = 1;
        int exchanges = 0;
        boolean stable = false;
        boolean splitHorizon = false;
        boolean trace = false;
        boolean fail = false;
        boolean change = false;
        String viewNode = "";
        String traceRoute = "";
        String failLink = "";
        String changeLink = "";
        String nodeA = null;
        String nodeB = null;
        String failNodeA = null;
        String failNodeB = null;
        String changeNodeA = null;
        String changeNodeB = null;
        int noticeExchange = 0;
        int failExchange = 0;
        int changeExchange = 0;
        int changeValue = 0;
        while (i < args.length && args[i].startsWith("-")) {
            arg = args[i++];

            // check for arguments and arguments for the arguments
            if (arg.equals("-help")){
                System.out.println("Command use: java -jar DVRouting [-s/-e #] [-v 'Node'] [-t 'N1,N2,EX'] [-f 'N1,N2,EX'] [-c 'N1,N2,C,EX'] [-split]");
                System.exit(0);
            }
            else if (arg.equals("-e")) {
                if (i < args.length)
                    exchanges = Integer.parseInt(args[i++]);
                else {
                    System.err.println("-e requires a number '-e 10'");
                    System.exit(0);
                }
            }
            else if (arg.equals("-v")) {
                if (i < args.length)
                    viewNode = args[i++];
                else {
                    System.err.println("-v requires a node to view 'N1'");
                    System.exit(0);
                }
            }
            else if (arg.equals("-t")) {
                if (i < args.length) {
                    traceRoute = args[i++];
                    String[] items = traceRoute.split(",");
                    nodeA = items[0];
                    nodeB = items[1];
                    noticeExchange = Integer.parseInt(items[2]);
                    trace = true;
                } else {
                    System.err.println("-t requires a two nodes and an exchange number to trace on");
                    System.exit(0);
                }
            }
            else if (arg.equals("-f")) {
                if (i < args.length) {
                    failLink = args[i++];
                    String[] items = failLink.split(",");
                    failNodeA = items[0];
                    failNodeB = items[1];
                    failExchange = Integer.parseInt(items[2]);
                    fail = true;
                } else {
                    System.err.println("-f requires a two nodes and an exchange number to fail on");
                    System.exit(0);
                }
            }
            else if (arg.equals("-c")) {
                if (i < args.length) {
                    changeLink = args[i++];
                    String[] items = changeLink.split(",");
                    changeNodeA = items[0];
                    changeNodeB = items[1];
                    changeValue = Integer.parseInt(items[2]);
                    changeExchange = Integer.parseInt(items[3]);
                    change = true;
                } else {
                    System.err.println("-c requires a two nodes, a cost and an exchange number to change on");
                    System.exit(0);
                }
            }
            else if (arg.equals("-s")){
                stable = true;
            }
            else if (arg.equals("-split")){
                splitHorizon = true;
            }
        }

        System.out.println("Getting file");
        // The name of the file to open.
        String fileName = args[0];

        // Holds one line at a time
        String line = null;

        try {
            // Read the file
            FileReader fr =
                    new FileReader(fileName);

            // Wrap FileReader in BufferedReader.
            BufferedReader br =
                    new BufferedReader(fr);

            // Grab first line ( which should be list of nodes)
            line = br.readLine();
            //System.out.println(line);
            // Strip brackets { }, split into list
            String strippedLine = line.replaceAll("[\\[\\](){}]","");
            String[] nodes = strippedLine.split(",");

            //Create the nodes, add to list
            ArrayList<DVNode> nodeList = new ArrayList<DVNode>();
            for (String label: nodes){
                nodeList.add(new DVNode(label));
            }

            ArrayList<DVEdge> edgeList = new ArrayList<DVEdge>();
            // Get the edges
            while((line = br.readLine()) != null) {
                // Strip out the brackets, split
                strippedLine = line.replaceAll("[\\[\\](){}]","");
                String[] edge = strippedLine.split(",");
                // Get nodes on the edge
                DVNode a = null;
                DVNode b = null;
                for (DVNode n: nodeList){
                    if (n.getLabel().equals(edge[0])){
                        a = n;
                    }
                    else if(n.getLabel().equals(edge[1])){
                        b = n;
                    }
                }
                if (a == null || b == null) // Could not find nodes in nodelist
                    System.out.println("Could not resolve edge between: " + edge[0] + " - " + edge[1]);
                else {
                    // Add edge to list of edges
                    int cost = Integer.parseInt(edge[2]);
                    DVEdge newEdge = new DVEdge(a, b, cost);
                    edgeList.add(newEdge);
                    //Add node as neighbour to each node
                    a.addNeighbour(new DVNeighbour(b, cost));
                    b.addNeighbour(new DVNeighbour(a, cost));
                }
            }
            // Create the graph using the node/edge lists
            if (!nodeList.isEmpty() && !edgeList.isEmpty()) {
                DVGraph routingGraph = new DVGraph(nodeList, edgeList, splitHorizon);

                //Operations on the graph
                System.out.println("Graph constructed successfully.");
                if (splitHorizon){
                    System.out.println("Split Horizon: ON");
                }
                System.out.println(routingGraph);
                int counter = 1;
                // If going until stability, loop
                if (stable) {
                    while (!routingGraph.isStable()){
                        System.out.println("Exchange " + (counter));
                        // Check our flags to see if it's time to use them
                        if (trace && counter == noticeExchange){
                            System.out.println(routingGraph.findPath(nodeA, nodeB));
                        }
                        if (fail && counter == failExchange){
                            routingGraph.failLink(failNodeA, failNodeB);
                            System.out.println("Failing Link: " + failNodeA + " - " + failNodeB);
                        }
                        if (change && counter == changeExchange){
                            routingGraph.changeLink(changeNodeA, changeNodeB, changeValue);
                            System.out.println("Changing Link: " + changeNodeA + " - " + changeNodeB + ": " + changeValue);
                        }
                        if (!viewNode .equals("")){
                            routingGraph.printRoutingTable(viewNode);
                        }
                        routingGraph.doExchange();
                        routingGraph.stableNode(viewNode);
                        routingGraph.checkStability();
                        counter++;
                    }
                    System.out.println("Stability achieved");
                }
                // Else exchange selected number of times
                else {
                    for (int j = 0; j < exchanges; j++){
                        System.out.println("Exchange " + (counter));
                        // check flags
                        if (trace && counter == noticeExchange){
                            System.out.println(routingGraph.findPath(nodeA, nodeB));
                        }
                        if (fail && counter == failExchange){
                            System.out.println("Failing Link: " + failNodeA + " - " + failNodeB);
                            routingGraph.failLink(failNodeA, failNodeB);
                        }
                        if (change && counter == changeExchange){
                            routingGraph.changeLink(changeNodeA, changeNodeB, changeValue);
                            System.out.println("Changing Link: " + changeNodeA + " - " + changeNodeB + ": " + changeValue);
                        }
                        if (!viewNode .equals("")){
                            routingGraph.printRoutingTable(viewNode);
                        }
                        routingGraph.doExchange();
                        counter++;
                    }
                }


                br.close();
            }
            // We couldn't make the graph
            else {
                System.out.println("Not enough information to construct graph.");
            }
        }
        // Catch exceptions
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + fileName + "'");
        }
    }

}

