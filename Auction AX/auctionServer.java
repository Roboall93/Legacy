import java.rmi.Naming;	//Import naming classes to bind to rmiregistry
import java.rmi.server.UnicastRemoteObject;

/**
 * @param args
 */
public class auctionServer {
	static int port = 1099;
	
   //auctionServer constructor
   public auctionServer() {
     
     //Construct a new AuctionImpl object and bind it to the local rmiregistry
     //N.b. it is possible to host multiple objects on a server by repeating the
     //following method. 

     try {
       	auctionImpl ai = new auctionImpl();
       	auction a = (auction) UnicastRemoteObject.exportObject(ai, 0);
       	//System.setProperty("java.rmi.server.hostname","localhost");
       	Naming.rebind("rmi://localhost:" + port + "/AuctionService", a);
     } 
     catch (Exception e) {
       System.out.println("Server Error: " + e);
     }
   }

   public static void main(String args[]) {
     	//Create the new Auction server
	if (args.length == 1)
		port = Integer.parseInt(args[0]);
	System.out.println("Running server...");
	new auctionServer();
   }
}
