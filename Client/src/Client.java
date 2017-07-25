import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client{
	
	private String ip;
	private int port;
	private String name;
	private int id;
	
	private Socket cliente;
	private ObjectOutputStream saida;
	private ObjectInputStream entrada;
	private Thread threadReadMessage;
	
	public Client(String ip, int port, String name) {
		this.ip = ip;
		this.port = port;
		this.name = name;	
	}
	
	public boolean isClosed() {
		return cliente.isClosed();
	}
	
	public void connect() {
		try {
			cliente = new Socket(ip, port);
			saida = new ObjectOutputStream(cliente.getOutputStream());
			entrada = new ObjectInputStream(cliente.getInputStream());
			
			sendMessage(getName());
			
			threadReadMessage = new Thread(new Runnable() {
				@Override
				public void run() {
					
					while(!cliente.isClosed()) {
						
						readMessage();
					}
				}
			});
			threadReadMessage.start();
			
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
			//e.printStackTrace();
			Main.jtaChat.append("Please, connect first. (" + e.getMessage() + ")\n");
		}
	}
	
	public String readMessage() {
		String message = "";
		
		try {
			message = (String) entrada.readObject();
			Main.jtaChat.append(message + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// Erro de leitura
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return message;
	}
	
	public void disconnect() {
		try {
			Main.jtaChat.append("Disconnected\n");
			entrada.close();
			saida.close();
			cliente.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
