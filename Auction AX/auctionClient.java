
import java.rmi.Naming;			//Import the rmi naming - so you can lookup remote object
import java.rmi.RemoteException;	//Import the RemoteException class so you can catch it
import java.net.MalformedURLException;	//Import the MalformedURLException class so you can catch it
import java.rmi.NotBoundException;	import java.util.ArrayList;
import java.util.Scanner;
//Import the NotBoundException class so you can catch it

public class auctionClient {
	
	private static void startTextInterface(auction a){
		
		String input;
		String separator = "-----";
		Scanner scanner = new Scanner(System.in);
		System.out.println("Welcome to the Auction Interface, please enter a command:");
		System.out.println("Type '?' for help");
		System.out.print(">");
		
		input = scanner.nextLine().toLowerCase();
		while(!input.equals("quit")){
			
			switch (input){
				case "?": 
					System.out.println("'new'- Create auction\t 'bid'- bid on auction\t 'list' - list auctions\t '?' - help");
					break;
				case "new":
					System.out.print("Enter the auction name: ");
					String name = scanner.nextLine();
					System.out.println("Enter the minimum value:");
					int minval = scanner.nextInt();
					System.out.println("Enter the auction duration:");
					int duration = scanner.nextInt();
					scanner.nextLine();
					try {
						a.createAuction(name, minval, duration);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					break;
				case "bid":
					System.out.println("Enter the id of the auction you wish to bid on:");
					int id = scanner.nextInt();					
					System.out.println("Enter your bid:");
					int bid = scanner.nextInt();
					scanner.nextLine(); 
					try {
						a.bidOnItem(id, bid);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					break;
				case "list":
					ArrayList<auctionObject> auctions = null;
					try {
						auctions = a.getAuctionList();
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					
					if (auctions != null){
						System.out.println(separator);
						System.out.println("ID\tAuction\tBid\tMininum\tDuration");
						for (auctionObject auc: auctions){
							System.out.printf("%d\t%s\t%d\t%d\t%d\n", 
								auc.getId(), auc.getName(), auc.getCurrentBid(), auc.getMinvalue(), auc.getDuration());
						}
						System.out.println(separator);
					}
					else {
						System.out.println(separator);
						System.out.println("There are currently no auctions available.");
						System.out.println(separator);
					}
					break;
				default:
					System.out.println("Input not recognised. Type '?' for help.");
					System.out.println("Input:" + input);
					break;
				}
			
			System.out.print("\n>");
			input = scanner.nextLine().toLowerCase();
		}
		 System.out.println("Quitting...");
		 scanner.close();
		 System.exit(0);
	}

    public static void main(String[] args) {
        
       String reg_host = "localhost";
       int reg_port = 1099;
       
       if (args.length == 1) {
       	reg_port = Integer.parseInt(args[0]);
      } else if (args.length == 2) {
      	reg_host = args[0];
      	reg_port = Integer.parseInt(args[1]);
      }
        
	try {

	    // Create the reference to the remote object through the remiregistry			
            auction a = (auction)
            		//Naming.lookup("rmi://localhost/CalculatorService");
                    Naming.lookup("rmi://" + reg_host + ":" + reg_port + "/AuctionService");
            
            startTextInterface(a);
        }
        // Catch the exceptions that may occur - rubbish URL, Remote exception
	// Not bound exception or the arithmetic exception that may occur in 
	// one of the methods creates an arithmetic error (e.g. divide by zero)
	catch (MalformedURLException murle) {
            System.out.println();
            System.out.println("MalformedURLException");
            System.out.println(murle);
        }
        catch (RemoteException re) {
            System.out.println();
            System.out.println("RemoteException");
            System.out.println(re);
        }
        catch (NotBoundException nbe) {
            System.out.println();
            System.out.println("NotBoundException");
            System.out.println(nbe);
        }
    }
}

