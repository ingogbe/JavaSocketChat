import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ThreadClient extends Thread{
	
	private Socket cliente;
	private ObjectInputStream entrada;
	private ObjectOutputStream saida;
	
	private String clientName;
	private String hostName;
	private String hostAdress;
	private long id;
	private boolean stopFlag;
	private int port;

	public ThreadClient(String hostAdress, int port, String name) {
		this.id = 0;
		this.cliente = null; 
		this.port = port;
		this.stopFlag = true;
		this.entrada = null;
		this.saida = null;
		this.clientName = name;
		this.hostAdress = hostAdress;
		this.hostName = "";	
	}
	
	public void connect() {
		try {
			cliente = new Socket(hostAdress, port);
			saida = new ObjectOutputStream(cliente.getOutputStream());
			entrada = new ObjectInputStream(cliente.getInputStream());
			
			//TODO
			updateClient();
			
			this.start();
			setStopFlag(false);
			
		} catch (IOException e) {
			System.out.println("Erro: " + e.getMessage());
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
	
	public void updateClient() {
		try {
			saida.flush();
			saida.writeObject(this);
			
			ThreadClient tc = (ThreadClient) entrada.readObject();
			this.hostAdress = tc.getHostAdress();
			this.hostName = tc.getHostName();
			this.id = tc.getId();
			this.port = tc.getPort();
			
		} catch (IOException | ClassNotFoundException e) {
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

	public void run() {
		
		while(!isStopFlag()) {
			
			readMessage();
		}
	}
	
	public boolean isClosed() {
		return cliente.isClosed();
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