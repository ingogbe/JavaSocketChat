import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Main {

	private static final int PORT = 12345;
	
	private static ArrayList<ThreadClient> arrayClients = new ArrayList<ThreadClient>();
	private static ServerSocket servidor;
	
	public static void main(String[] x) {
		try {
			/* 
			 * Porta pode ser entre 0 e 65535, recomendado uso de 1024 para frente, pois 
			 * as portas com números abaixo deste são reservados para o uso do sistema.
			 * 
			 * ServerSocket = Responsável por atender pedidos via rede e em determinada porta.
			 * 
			 * Instancia o ServerSocket ouvindo a porta especificada
			 */
	        servidor = new ServerSocket(PORT);
			System.out.println("Servidor criado e ouvindo. Ip: "+ InetAddress.getLocalHost() +". Port: " + PORT);
	          
			int id = 1;
			
	        //Aguarda conexões
	        while(true) {
	        	/*
				 * O método accept() bloqueia a execução até que o servidor receba um pedido
				 * de conexão
				 */
	        	Socket cliente = servidor.accept();
	        	
	        	//Inicia thread do cliente
	        	ThreadClient tc = new ThreadClient(cliente,id);
	        	arrayClients.add(tc);
	        	tc.start();
	        	id++;
	        }    
		}
		catch(Exception e) {
			System.out.println("Erro: " + e.getMessage());
			e.printStackTrace();
		}
		finally {
	    	//Nada a fazer
	    } 
		
	}

	public static ArrayList<ThreadClient> getArrayClients() {
		return arrayClients;
	}

	public static void setArrayClients(ArrayList<ThreadClient> arrayClients) {
		Main.arrayClients = arrayClients;
	}
	
}
