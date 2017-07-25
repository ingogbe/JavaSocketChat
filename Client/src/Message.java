import java.util.Date;

public class Message {
	
	public static final int TYPE_PLAIN = 1;
	public static final int TYPE_FILE = 2;
	
	public static final int NO_RECIPIENT = -1;
	
	private String message;
	private int type;
	private Date date;
	
	private int receiverId;
	private int senderId;
	
	public Message(String message, int type) {
		super();
		this.message = message;
		this.type = type;
		this.receiverId = NO_RECIPIENT;
	}
	
	public Message(String message, int type, int recipientId) {
		super();
		this.message = message;
		this.type = type;
		this.receiverId = recipientId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
	}

	public int getType() {
		return type;
	}
	
	
	
}
