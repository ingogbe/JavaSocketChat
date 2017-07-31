import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

public class ServerMessageThread extends Thread{
	Client client;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private InputStream is;
	private OutputStream os;
	
	private boolean stopFlag;
	private Socket socket;
	
	public ServerMessageThread(Socket socket) {
		super();
		this.socket = socket;
		this.stopFlag = false;
		this.client = new Client();
		
		try {
			this.is = this.socket.getInputStream();
			this.os = this.socket.getOutputStream();
			
			this.output = new ObjectOutputStream(this.os);
			this.input = new ObjectInputStream(this.is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		while(!isStopFlag()) {
			Message message = readMessage();
			
			if(message == null) {
				break;
			}
			else if(message.getType() == Message.TYPE_UPDATECLIENT) {
				String tempName = this.client.getName();
				System.out.println(this.client.getName());
				
				this.client = message.getUpdate();
				
				if(tempName.equals("")) {
					this.client.setId(MainServer.getNewID());
					Message msgUpdate = new Message(this.client, Message.TYPE_UPDATECLIENT);
					this.sendMessage(msgUpdate);
					
					for(Message msg : MainServer.messageHistoric) {
						if(msg.hasReceiver()) {
							if(this.getClient().getId() == msg.getReceiver().getId()) {
								this.sendMessage(message);
								break;
							}
						}
						else {
							this.sendMessage(msg);
						}
					}
					
					Date date = new Date();
					Message msgConnect = new Message("Client connected (" + client.getName() + "). ID: " + client.getId() + "!", Message.TYPE_PLAINTEXT, date);
					MainServer.jtaChat.append("[" + msgConnect.getFormattedServerDate() + "] " + msgConnect.getMessage() + "!\n");
					MainServer.messageHistoric.add(msgConnect);
					
					ArrayList<Client> clients = new ArrayList<Client>();
					for(ServerMessageThread tc: MainServer.getConnectedThreads()) {
						clients.add(tc.getClient());
					}
					
					Message updateUsers = new Message(clients, Message.TYPE_UPDATEUSERS);
					
					for(ServerMessageThread tc: MainServer.getConnectedThreads()) {
						tc.sendMessage(msgConnect);
						tc.sendMessage(updateUsers);
					}
				}
				else if(!tempName.equals(this.client.getName())) {
					Date date = new Date();
					Message msg = new Message(tempName + " has changed his name to " + this.client.getName() + "!", Message.TYPE_PLAINTEXT, date);
					MainServer.jtaChat.append("[" + msg.getFormattedServerDate() + "] " + msg.getMessage() + "\n");
					MainServer.messageHistoric.add(msg);
					
					ArrayList<Client> clients = new ArrayList<Client>();
					for(ServerMessageThread tc: MainServer.getConnectedThreads()) {
						clients.add(tc.getClient());
					}
					
					Message updateUsers = new Message(clients, Message.TYPE_UPDATEUSERS);
					
					for(ServerMessageThread tc: MainServer.getConnectedThreads()) {
						tc.sendMessage(msg);
						tc.sendMessage(updateUsers);
					}
					
				}
			}
			else if(message.getType() == Message.TYPE_PLAINTEXT) {
				message.setServerDate(new Date());
				
				MainServer.messageHistoric.add(message);
				
				if(message.hasReceiver()) {
					MainServer.jtaChat.append("["+ message.getFormattedServerDate() + "] " + message.getSender().getName() + "(ID: " + message.getSender().getId() + ") TO " + message.getReceiver().getName() + "(ID: " + message.getReceiver().getId() + ") >> " + message.getMessage() + "\n");
					
					for(ServerMessageThread tc: MainServer.getConnectedThreads()) {
						if(tc.getClient().getId() == message.getReceiver().getId() || tc.getClient().getId() == message.getSender().getId()) {
							tc.sendMessage(message);
						}
					}
				}
				else {
					MainServer.jtaChat.append("["+ message.getFormattedServerDate() + "] " + message.getSender().getName() + "(ID: " + message.getSender().getId() + ") >> " + message.getMessage() + "\n");
					
					for(ServerMessageThread tc: MainServer.getConnectedThreads()) {
						tc.sendMessage(message);
					}
				}
				
				
			}
			else if(message.getType() == Message.TYPE_FILE) {
				//TODO Pegar a thread de arquivo do cliente sender e fazer ela reenviar para as pessoas certas
				//TODO O download qd o sender mando � automatico, reconhecer arquivo enviado pela ID e filename
			}
			
			
		}
	}
	
	public void disconnect() {
		this.stopFlag = true;
		
		try {
			output.close();
			input.close();
			socket.close();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		MainServer.getConnectedThreads().remove(this);
		Date date = new Date();
		Message msg = new Message("Client " + client.getName() + " [ID:" + client.getId() + "] disconnected!", Message.TYPE_PLAINTEXT, date);
		MainServer.jtaChat.append("[" + msg.getFormattedServerDate() + "] " + msg.getMessage() + "!\n");
		MainServer.messageHistoric.add(msg);
		
		ArrayList<Client> clients = new ArrayList<Client>();
		for(ServerMessageThread tc: MainServer.getConnectedThreads()) {
			clients.add(tc.getClient());
		}
		
		Message updateUsers = new Message(clients, Message.TYPE_UPDATEUSERS);
		
		for(ServerMessageThread tc: MainServer.getConnectedThreads()) {
			tc.sendMessage(msg);
			tc.sendMessage(updateUsers);
		}
	}
	
	public void sendMessage(Message message) {
		try {
			output.flush();
			output.writeObject(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Message readMessage() {
		Message message = null;
	
		try {
			message = (Message) input.readObject();
		} catch (IOException e) {	
			disconnect();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return message;
	}
	
	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public ObjectInputStream getInput() {
		return input;
	}

	public void setInput(ObjectInputStream input) {
		this.input = input;
	}

	public ObjectOutputStream getOutput() {
		return output;
	}

	public void setOutput(ObjectOutputStream output) {
		this.output = output;
	}

	public boolean isStopFlag() {
		return stopFlag;
	}

	public void setStopFlag(boolean stopFlag) {
		this.stopFlag = stopFlag;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public InputStream getIs() {
		return is;
	}

	public void setIs(InputStream is) {
		this.is = is;
	}

	public OutputStream getOs() {
		return os;
	}

	public void setOs(OutputStream os) {
		this.os = os;
	}
	
	
	
}
