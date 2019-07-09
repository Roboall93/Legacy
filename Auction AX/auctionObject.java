import java.io.Serializable;
import java.time.LocalDateTime;
/*
 * Object to hold auction details
 * id, name, minimum value, duration and current bid
 */

public class auctionObject implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2487267059514291680L;
	private static int counter = 0;
	private final int id;
	private final String name;
	private final int minvalue;
	private final int duration;
	private int currentBid;
	private LocalDateTime expireTime;
	private Calendar timeout;

	/**
	 * @param args
	 */
	public auctionObject(String name, int minval, int duration){
		id = counter++;
		this.name = name;
		minvalue = minval;
		this.duration = duration;
		currentBid = 0;
		Calendar c = new GregorianCalendar();
		expireTime = 
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}


	public int getMinvalue() {
		return minvalue;
	}


	public int getDuration() {
		return duration;
	}

	public int getCurrentBid(){
		return currentBid;
	}
	
	public void setCurrentBid(int bid){
			currentBid = bid;
	}
	
	

}
