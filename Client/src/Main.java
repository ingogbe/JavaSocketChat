import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;

public class Main extends JFrame implements WindowListener, ActionListener{
	
	private static final long serialVersionUID = 1L;

	private JMenuBar jmBarraMenu;
	private JMenu jmArquivo;
	private JMenuItem jmiArquivoSair;
	
	public static JTextArea jtaChat;
	JTextField jtfName, jtfIp, jtfPort;
	JButton jbConnect;
	
	static Container C;
	
	private ThreadClient client;
	
	public Main() {
		super("Client");
		setSize(800,600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setLayout(null);
		addWindowListener(this);
		
		C = getContentPane();
		C.setLayout(null);
		
		initComponents();
	}
	
	public void initComponents(){
		jmBarraMenu = new JMenuBar();
		jmBarraMenu.setBounds(0, 0, 800, 20);
		C.add(jmBarraMenu);
		
		//BEGIN - ARQUIVO
		jmArquivo = new JMenu("File");
		jmBarraMenu.add(jmArquivo);
		
		jmiArquivoSair = new JMenuItem("Exit");
		jmiArquivoSair.addActionListener(this);
		jmArquivo.add(jmiArquivoSair);
		//END - ARQUIVO
		
		TitledBorder tbConfiguration = new TitledBorder("Configuration");
		
		JPanel jpConfiguration = new JPanel();
		jpConfiguration.setLayout(null);
		jpConfiguration.setBounds(5, 25, 785, 60);
		jpConfiguration.setBorder(tbConfiguration);
		C.add(jpConfiguration);
		
		JLabel jlName = new JLabel("Name:");
		jlName.setBounds(10, 20, 40, 30);
		jpConfiguration.add(jlName);
		
		jtfName = new JTextField();
		jtfName.setBounds(50, 20, 200, 30);
		jtfName.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connectAction();
			}
		});
		jpConfiguration.add(jtfName);
		
		JLabel jlIp = new JLabel("IP:");
		jlIp.setBounds(260, 20, 20, 30);
		jpConfiguration.add(jlIp);
		
		jtfIp = new JTextField();
		jtfIp.setBounds(280, 20, 250, 30);
		jtfIp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connectAction();
			}
		});
		jpConfiguration.add(jtfIp);
		
		JLabel jlPort = new JLabel("Port:");
		jlPort.setBounds(540, 20, 40, 30);
		jpConfiguration.add(jlPort);
		
		jtfPort = new JTextField();
		jtfPort.setBounds(575, 20, 100, 30);
		jtfPort.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connectAction();
			}
		});
		jpConfiguration.add(jtfPort);
		
		jbConnect = new JButton("Connect");
		jbConnect.setBounds(680, 20, 95, 30);
		jbConnect.setMargin(new Insets(0, 0, 0, 0));
		jbConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connectAction();
			}
		});
		jpConfiguration.add(jbConnect);
		
		JPanel jpChat = new JPanel();
		jpChat.setBounds(7, 90, 600, 475);
		jpChat.setBackground(Color.GRAY);
		jpChat.setLayout(null);
		C.add(jpChat);
		
		jtaChat = new JTextArea();
		jtaChat.setEditable(false);
		jtaChat.setLineWrap(true);
		
		DefaultCaret caret = (DefaultCaret) jtaChat.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		JScrollPane jsp = new JScrollPane(jtaChat);
		jsp.setBounds(5, 5, 590, 430);
		jpChat.add(jsp);
		
		JTextField jtfMessageBox = new JTextField();
		jtfMessageBox.setBounds(5, 440, 500, 30);
		jtfMessageBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String message = jtfMessageBox.getText();
				client.sendMessage(message);
				jtfMessageBox.setText("");
			}
		});
		jpChat.add(jtfMessageBox);
		
		JButton jbSend = new JButton("Send");
		jbSend.setBounds(510, 440, 85, 30);
		jbSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String message = jtfMessageBox.getText();
				client.sendMessage(message);
				jtfMessageBox.setText("");
			}
		});
		jpChat.add(jbSend);
		
	}
	
	public void connectAction() {
		if(client == null || client.isClosed()) {
			int port = Integer.parseInt(jtfPort.getText());
			String ip = jtfIp.getText();
			String name = jtfName.getText();
			
			client = new ThreadClient(ip, port, name);
			client.connect();
			
			jbConnect.setText("Disconnect");
		}
		else {
			client.disconnect();
			jbConnect.setText("Connect");
		}
		
	}
	
	public static void main(String[] args) {
		Main main = new Main(); 
		main.setVisible(true);
		
		AlterFonts.alterFonts();
	}
	
	
	
	public void actionPerformed(ActionEvent e) {
		for(Component i :getContentPane().getComponents()){
			if(!i.equals(jmBarraMenu))
				i.setVisible(false);
		}
		
		if(e.getSource().equals(jmiArquivoSair)){
			System.exit(EXIT_ON_CLOSE);
		}
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

}
