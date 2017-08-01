import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerFileThread extends Thread{
	private Client client;
	private InputStream is;
	private OutputStream os;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private Socket socket;
	private boolean isRunning;
	
	public ServerFileThread(Socket socket) {
		super();
		this.socket = socket;
		this.client = null;
		this.isRunning = false;
		
		try {
			this.is = this.socket.getInputStream();
			this.os = this.socket.getOutputStream();
			
			this.oos = new ObjectOutputStream(this.os);
			this.ois = new ObjectInputStream(this.is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Message readMessage() {
		Message message = null;
	
		try {
			message = (Message) ois.readObject();
		} catch (IOException e) {	
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return message;
	}
	
	public void startServerFile(){
		this.isRunning = true;
		this.start();
	}
	
	public void run() {
		Message message = readMessage();
		this.client = message.getUpdate();
		FileManager fm = new FileManager(this.is, this.os);
		
		while(isRunning){
			
		}
	}
	
	
}
