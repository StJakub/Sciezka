import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 * Klasa odpowiedzialna za pobranie wyników z serwera
 * @author Jakub Stanis³aw
 *
 */
public class PobranieWynikow implements Runnable
{
	private MainWindow mainWindHND;
	private int port;
	private InetAddress host;
	
	/**
	 * 
	 * @param window - okno aplikacji
	 * @param port - port na którym ma miejsce komunikacja miêdzy klientem a serwerem
	 * @param host - adres IP
	 */
	public PobranieWynikow(MainWindow window, int port, InetAddress host)
	{
		mainWindHND = window;
		this.port = port;
		this.host = host;
	}
	
	@Override
	public void run()
	{
		try 
		{
			Socket socket = new Socket(host, port); 
			OutputStream os = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(os, true);
			
			pw.println(SerwerGry.najlepszeWyniki);
			
			InputStream is = socket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			String message = "";
			String line = null;
			while((line = br.readLine()) != null)
			{
				message += line;
				message += "\n";
			}
			if(message.isEmpty())
				message = "Brak rekordow\n";
			socket.close();
			
			JOptionPane.showMessageDialog(mainWindHND, message, "Najpelsze wyniki zdalnie", JOptionPane.INFORMATION_MESSAGE);
		} 
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(mainWindHND, e.getMessage(), "Serwer",JOptionPane.WARNING_MESSAGE);
		}
		
	}

}
