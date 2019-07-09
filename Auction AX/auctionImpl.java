import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author 1102085a
 */

/**
 * 
 *
 */
public class auctionImpl implements auction {
	
	private static HashMap<Integer, auctionObject> aucList = new HashMap<Integer, auctionObject>();

	/* (non-Javadoc)
	 * @see auction#createAuction(java.lang.String, int, int)
	 */
	@Override
	public synchronized int createAuction(String name, int value, int duration)
			throws RemoteException {
		auctionObject ao = new auctionObject(name, value, duration);
		aucList.put(ao.getId(), ao);
		return ao.getId();
	}

	/* (non-Javadoc)
	 * @see auction#bidOnItem(java.util.UUID, int)
	 */
	@Override
	public synchronized void bidOnItem(int id, int bid) throws RemoteException {
		if (!aucList.isEmpty()){
			auctionObject ao = aucList.get(id);
			if (bid > ao.getCurrentBid() && bid > ao.getMinvalue()){
				ao.setCurrentBid(bid);
				System.out.printf("Current bid is now £%d\n", bid);
			}
			else if(bid < ao.getCurrentBid()) {			
				System.out.printf("You must bid over the current bid of £%d\n", ao.getCurrentBid());
				}
			else {
				System.out.printf("You must bid over the minimum value of £%d", ao.getMinvalue());
			}
		}
	} 

	/* (non-Javadoc)
	 * @see auction#getAuctionList()
	 */
	@Override
	public synchronized ArrayList<auctionObject> getAuctionList() throws RemoteException {
		if (!aucList.isEmpty()){
			ArrayList<auctionObject> auctions = new ArrayList<auctionObject>();
			for (auctionObject ao : aucList.values()){
				auctions.add(ao);
			}
			return auctions;
		}
		else {
			return null;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
