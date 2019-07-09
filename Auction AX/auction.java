import java.util.ArrayList;
import java.util.HashMap;


public interface auction
	extends java.rmi.Remote{
	
	static HashMap<Integer, auctionObject> aucList = new HashMap<Integer, auctionObject>();
	
	/*
	 * Creates an instance of an auction object and returns its unique identifier
	 */
	public int createAuction(String name, int value, int duration)
			throws java.rmi.RemoteException;

	/*
	 * Allows the user to place a bid on an auction item
	 */
	public void bidOnItem(int id, int bid)
			throws java.rmi.RemoteException;
	
	/*
	 * Returns a list of available auction items
	 */
	public ArrayList<auctionObject> getAuctionList()
			throws java.rmi.RemoteException;
}
