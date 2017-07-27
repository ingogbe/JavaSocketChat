import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerThreadFile extends Thread{
	private FileOutputStream fos;
	private FileInputStream fis;
	private BufferedOutputStream bos;
	private BufferedInputStream bis;
	private InputStream is;
	private OutputStream os;
	
	private Socket socket;
	
	public final static int FILE_SIZE = 6022386; // file size temporary hard coded
    											 // should bigger than the file to be downloaded
	
	/*
	 * Quando vai enviar, coloca socket de quem vai receber
	 * Quando vai receber, coloca socket de quem recebe
	 */
	
	public final static int TYPE_RECEIVE = 1;
	public final static int TYPE_SEND = 2;
	private int type;
	private String sendFilePath;
	private String receiveFilePath;
	private Message message;
	private ServerThreadClient threadClient;
	
	public ServerThreadFile(Socket socket){
		this.socket = socket;
		this.type = 0;
		this.sendFilePath = "";
		this.receiveFilePath = "";
		this.message = null;
		this.threadClient = null;
		
		this.fos = null;
		this.fis = null;
		this.bos = null;
		this.bis = null;
		this.is = null;
		this.os = null;
	}
	
	public void run() {
		
		if(this.type == TYPE_SEND) {
			sendFile();
			threadClient.sendMessage(message);
		}
		else if(this.type == TYPE_RECEIVE) {
			receiveFile();
			
			if(message.hasReceiver()) {
				MainServer.jtaChat.append("["+ message.getFormattedServerDate() + "] " + message.getSender().getName() + "(ID: " + message.getSender().getId() + ") TO " + message.getReceiver().getName() + "(ID: " + message.getReceiver().getId() + ") >> Send a file: '" + message.getFileName()  + "'\n");
				
				for(ServerThreadClient tc: MainServer.getConnectedThreads()) {
					if(tc.getClient().getId() == message.getReceiver().getId() || tc.getClient().getId() == message.getSender().getId()) {
						ServerThreadFile tf = new ServerThreadFile(tc.getSocket());
						tf.startSendFile(MainServer.SERVER_STORAGE_PATH + message.getFileName(), message, tc);
					}
				}
			}
			else {
				MainServer.jtaChat.append("["+ message.getFormattedServerDate() + "] " + message.getSender().getName() + "(ID: " + message.getSender().getId() + ") >> Send a file: '" + message.getFileName()  + "'\n");
				
				for(ServerThreadClient tc: MainServer.getConnectedThreads()) {
					ServerThreadFile tf = new ServerThreadFile(tc.getSocket());
					tf.startSendFile(MainServer.SERVER_STORAGE_PATH + message.getFileName(), message, tc);
				}
			}
		}
		
		
		
		close();
	}
	
	public void startSendFile(String sendFilePath, Message message, ServerThreadClient tc) {
		this.type = TYPE_SEND;
		this.sendFilePath = sendFilePath;
		this.message = message;
		this.threadClient = tc;
		this.start();
	}
	
	public void startReceiveFile(String receiveFilePath, Message message) {
		this.type = TYPE_RECEIVE;
		this.receiveFilePath = receiveFilePath;
		this.message = message;
		this.start();
	}
	
	public void sendFile(){
		File myFile = new File(sendFilePath);
        byte [] mybytearray  = new byte [(int)myFile.length()];
        
		try {
			this.fis = new FileInputStream(myFile);
	        this.bis = new BufferedInputStream(this.fis);
	        this.bis.read(mybytearray,0,mybytearray.length);
	        this.os = this.socket.getOutputStream();
	        
	        System.out.println("Sending " + sendFilePath + "(" + mybytearray.length + " bytes)");
	        this.os.write(mybytearray,0,mybytearray.length);
	        this.os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
	}
	
	public void receiveFile() {
		byte [] mybytearray  = new byte [FILE_SIZE];
		
		try {
			this.is = this.socket.getInputStream();
			this.fos = new FileOutputStream(receiveFilePath);
			this.bos = new BufferedOutputStream(fos);
			int bytesRead = is.read(mybytearray,0,mybytearray.length);
			int current = bytesRead;
			
			do {
				bytesRead = is.read(mybytearray, current, (mybytearray.length-current));
				
				if(bytesRead >= 0) {
					current += bytesRead;
				}
			} while(bytesRead > -1);
			
			bos.write(mybytearray, 0 , current);
			bos.flush();
			
			System.out.println("File " + receiveFilePath + " downloaded (" + current + " bytes read)");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			if (fos != null) fos.close();
			if (fis != null) fis.close();
			if (bos != null) bos.close();
			if (bis != null) bis.close();
			if (os != null) os.close();
			if (is != null) is.close();
			if (socket != null) socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
