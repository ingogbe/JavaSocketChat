import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ThreadClient extends Thread {
	
	private Socket cliente;
	private ObjectInputStream entrada;
	private ObjectOutputStream saida;
	
	private String clientName;
	private String hostName;
	private String hostAdress;
	private long id;
	private boolean stopFlag;
	private int port;

	public ThreadClient(Socket cliente, int id) {
		this.cliente = cliente; 
		this.id = id;
		this.stopFlag = false;
		this.port = cliente.getLocalPort();
		
		try {
			entrada = new ObjectInputStream(cliente.getInputStream());
			saida = new ObjectOutputStream(cliente.getOutputStream());
			
			clientName = ((String) entrada.readObject());
			hostName = cliente.getInetAddress().getHostName();
			hostAdress = cliente.getInetAddress().getHostAddress();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void sendMessage(String message) {
		try {
			saida.flush();
			saida.writeObject(message);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String readMessage() {
		String message = "";
	
		try {
			message = (String) entrada.readObject();
			
		} catch (IOException | ClassNotFoundException e) {
			
			disconnect();
			
		}
		
		return message;
	}
	
	public boolean isClosed() {
		return cliente.isClosed();
	}
	

	public void run() {
		
		System.out.println("Cliente conectado ("+ clientName +"[ID:" + id + "]): " + hostName + " " + hostAdress + " " + cliente.getLocalPort() + " " + cliente.getPort());
		
		for(ThreadClient tc: Main.getArrayClients()) {
			tc.sendMessage("Client " + clientName + "[ID:" + id + "] is connected!");
		}

		System.out.println("Cliente atendido com sucesso: " + cliente.getRemoteSocketAddress().toString());
		
		String message = "";
		
		while(!cliente.isClosed() || cliente.isConnected()) {
			
			message = readMessage();
			
			if(!isStopFlag()) {
				for(ThreadClient tc: Main.getArrayClients()) {
					tc.sendMessage(clientName + " >> " + message);
				}
			}
			else {
				break;
			}
			
		}
	}
	
	public void disconnect() {
		setStopFlag(true);
		
		try {
			saida.close();
			entrada.close();
			cliente.close();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			System.out.println("Epa Opa Upa, deu erro nos close da parada");
		}
		
		Main.getArrayClients().remove(this);
		System.out.println("Cliente desconectou ("+ clientName +"): " + hostName + " " + hostAdress);
		
		for(ThreadClient tc: Main.getArrayClients()) {
			tc.sendMessage("Client " + clientName + "[ID:" + id + "] disconnected!");
		}
	}

	public ObjectInputStream getEntrada() {
		return entrada;
	}

	public void setEntrada(ObjectInputStream entrada) {
		this.entrada = entrada;
	}

	public ObjectOutputStream getSaida() {
		return saida;
	}

	public void setSaida(ObjectOutputStream saida) {
		this.saida = saida;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Socket getCliente() {
		return cliente;
	}

	public void setCliente(Socket cliente) {
		this.cliente = cliente;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getHostAdress() {
		return hostAdress;
	}

	public void setHostAdress(String hostAdress) {
		this.hostAdress = hostAdress;
	}

	public boolean isStopFlag() {
		return stopFlag;
	}

	public void setStopFlag(boolean stopFlag) {
		this.stopFlag = stopFlag;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	
	

}