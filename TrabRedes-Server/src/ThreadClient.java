import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

public class ThreadClient extends Thread{
	Client client;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private boolean stopFlag;
	private Socket socket;
	
	public ThreadClient(Socket socket) {
		super();
		this.socket = socket;
		this.stopFlag = false;
		this.client = new Client();
		
		try {
			this.output = new ObjectOutputStream(this.socket.getOutputStream());
			this.input = new ObjectInputStream(this.socket.getInputStream());
		} catch (IOException e) {
			//TODO Tratar catch
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
					
					//TODO
					ArrayList<Client> clients = new ArrayList<Client>();
					for(ThreadClient tc: MainServer.getConnectedThreads()) {
						clients.add(tc.getClient());
					}
					
					Message updateUsers = new Message(clients, Message.TYPE_UPDATEUSERS);
					
					//TODO
					for(ThreadClient tc: MainServer.getConnectedThreads()) {
						tc.sendMessage(msgConnect);
						tc.sendMessage(updateUsers);
					}
				}
				else if(!tempName.equals(this.client.getName())) {
					Date date = new Date();
					Message msg = new Message(tempName + " has changed his name to " + this.client.getName() + "!", Message.TYPE_PLAINTEXT, date);
					MainServer.jtaChat.append("[" + msg.getFormattedServerDate() + "] " + msg.getMessage() + "\n");
					MainServer.messageHistoric.add(msg);
					
					//TODO
					ArrayList<Client> clients = new ArrayList<Client>();
					for(ThreadClient tc: MainServer.getConnectedThreads()) {
						clients.add(tc.getClient());
					}
					
					Message updateUsers = new Message(clients, Message.TYPE_UPDATEUSERS);
					
					//TODO
					for(ThreadClient tc: MainServer.getConnectedThreads()) {
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
					
					for(ThreadClient tc: MainServer.getConnectedThreads()) {
						if(tc.getClient().getId() == message.getReceiver().getId() || tc.getClient().getId() == message.getSender().getId()) {
							tc.sendMessage(message);
						}
					}
				}
				else {
					MainServer.jtaChat.append("["+ message.getFormattedServerDate() + "] " + message.getSender().getName() + "(ID: " + message.getSender().getId() + ") >> " + message.getMessage() + "\n");
					
					for(ThreadClient tc: MainServer.getConnectedThreads()) {
						tc.sendMessage(message);
					}
				}
				
				
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
			//TODO Tratar catch
			e2.printStackTrace();
		}
		
		MainServer.getConnectedThreads().remove(this);
		Date date = new Date();
		Message msg = new Message("Client " + client.getName() + " [ID:" + client.getId() + "] disconnected!", Message.TYPE_PLAINTEXT, date);
		MainServer.jtaChat.append("[" + msg.getFormattedServerDate() + "] " + msg.getMessage() + "!\n");
		MainServer.messageHistoric.add(msg);
		
		//TODO
		ArrayList<Client> clients = new ArrayList<Client>();
		for(ThreadClient tc: MainServer.getConnectedThreads()) {
			clients.add(tc.getClient());
		}
		
		Message updateUsers = new Message(clients, Message.TYPE_UPDATEUSERS);
		
		for(ThreadClient tc: MainServer.getConnectedThreads()) {
			tc.sendMessage(msg);
			tc.sendMessage(updateUsers);
		}
	}
	
	public void sendMessage(Message message) {
		try {
			output.flush();
			output.writeObject(message);
		} catch (IOException e) {
			//TODO Tratar catch
			e.printStackTrace();
		}
	}
	
	public Message readMessage() {
		Message message = null;
	
		try {
			message = (Message) input.readObject();
		} catch (IOException e) {	
			//TODO Tratar catch
			disconnect();
		} catch (ClassNotFoundException e) {
			//TODO Tratar catch
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
	
	
	
}
