import java.awt.EventQueue;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Klasa odpowiedzialna za funkcjonowanie serwera 
 * @author Jakub Stanis³aw
 *
 */
public class SerwerGry extends JFrame implements Runnable
{	
	private String plikKonfig;
	
	private static final long serialVersionUID = 1L;
	final private static int width = 400;
	final private static int heigh = 200;
	
	final public static String zadajPlansze = "1";
	final public static String przeslaniePunktow = "2";
	final public static String najlepszeWyniki = "3";
	
	private JTable rekordy;									
	private TableRecordModel model;								
	private JMenuItem startServer;
	private JMenuItem stopServer;
	
	private int port;
	private ServerSocket serverSocket;
	private Thread watekNasluchiwania;
	
	/**
	 * 
	 * @param plikKonfig - plik zawieraj¹cy parametry - port i host 
	 */
	public SerwerGry(String plikKonfig)
	{
		this.plikKonfig = plikKonfig;
		this.setSize(width, heigh);
		this.setTitle("Serwer gry");
		JMenuBar bar = new JMenuBar();
		this.setJMenuBar(bar);
		watekNasluchiwania = null;
		
		JMenu serwerMenu = new JMenu("Serwer");		
		serwerMenu.addMenuListener(new MenuListener()
		{
			public void menuCanceled(MenuEvent ev) { }
			public void menuDeselected(MenuEvent ev) { }

			@Override
			public void menuSelected(MenuEvent ev) {
				boolean uruchomiony = (watekNasluchiwania != null);
				startServer.setEnabled(!uruchomiony);
				stopServer.setEnabled(uruchomiony);
			}
		});
		
		startServer = new JMenuItem("Start");
		startServer.addActionListener(new ActionListener() {

			@Override
			// Zdarzenie uruchomienia serwera
			public void actionPerformed(ActionEvent e)
			{
				start();
			}
		});
		
		stopServer = new JMenuItem("Stop");
		stopServer.addActionListener(new ActionListener() {

			@Override
			// Zdarzenie zakoñczenia pracy serwera
			public void actionPerformed(ActionEvent e)
			{
				stop();
			}
		});
		
		serwerMenu.add(startServer);
		serwerMenu.add(stopServer);
		bar.add(serwerMenu);
		
		this.addWindowListener(new WindowAdapter() 
		{
			public void windowClosing(WindowEvent e)
			{
				if(watekNasluchiwania != null)
					stop();
				model.zapiszWyniki();	
			}
		});
		
		
		model = new TableRecordModel();
		rekordy = new JTable(model);
		JScrollPane scroll = new JScrollPane(rekordy);
		this.add(scroll);
		
		serverSocket = null;
		this.setLocationRelativeTo(null);
	}
	
	public void start()
	{
		watekNasluchiwania = new Thread(this);
		watekNasluchiwania.start();
	}
	
	public void stop()
	{
		watekNasluchiwania.interrupt();
		
		try 
		{
			serverSocket.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		watekNasluchiwania = null;
	}
	
	public static void main(String[] args) 
	{
		String filename = "config.txt";
		
		
		EventQueue.invokeLater(new Runnable()
		{
			@Override
			// zadanie wyswietlenia okna
			public void run() 
			{
				SerwerGry window = new SerwerGry(filename);
				window.setVisible(true);
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});		

	}


	@Override
	// nasluchiwanie na porcie
	public void run()
	{	
		try 
		{
			BufferedReader in = new BufferedReader(new FileReader(plikKonfig));
			port = Integer.parseInt(in.readLine());
			in.close();
			serverSocket = new ServerSocket(port);
		}
		catch (Exception e) 
		{
			System.err.println("Create server socket: " + e);
			return;
		}
		try 
		{
			while(true)
			{
				Socket doGraczaSocket = serverSocket.accept();
						
				InputStream is = doGraczaSocket.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				
				String numer = br.readLine();
						
				if(numer.equals(zadajPlansze))
				{
					numer = br.readLine(); // numer poziomu
					int poziom = Integer.parseInt(numer);
					try 
					{
						OutputStream os = doGraczaSocket.getOutputStream();
						PrintWriter pw = new PrintWriter(os, true);
						
						
						BufferedReader in = new BufferedReader(new FileReader(Plansze.plansze[poziom]));
						

						
						String wiersz = null;
						
						while((wiersz = in.readLine()) != null) 
						{
							pw.println(wiersz);
						}
						in.close();
					}
					catch (Exception e)
					{
						e.printStackTrace();
						break;
					}
				}
				else if(numer.equals(przeslaniePunktow))
				{
					try 
					{
						String login = br.readLine();
						String punkty = br.readLine();
						model.dodajWynik(login,  Integer.parseInt(punkty));
					}
					catch (Exception e)
					{
						e.printStackTrace();
						break;
					}
				}
				else if(numer.equals(najlepszeWyniki))
				{
					OutputStream os = doGraczaSocket.getOutputStream();
					PrintWriter pw = new PrintWriter(os, true);
					
					for(int i = 0; i < model.getRowCount(); ++i)
					{
						pw.println(model.pobierzRekord(i));
					}
					doGraczaSocket.close();
				}
				
			}
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(this, e.getMessage(), "Polaczenie",JOptionPane.INFORMATION_MESSAGE);
			
		}
	}
} 

