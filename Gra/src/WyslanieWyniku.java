import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Klasa odpowiedzialna za wys³anie wyniku do serwera
 * @author Jakub Stanis³aw
 *
 */
public class WyslanieWyniku implements Runnable
{
	private String login;
	private int punkty;
	private int port;
	private InetAddress host;
	
	/**
	 * 
	 * @param login - login gracza
	 * @param punkty - punkty uzyskane podczas rozgrywki
	 * @param port - port, na którym nastêpuje komunikacja miêdzy serwerem a klientem
	 * @param host - adres IP
	 */
	public WyslanieWyniku(String login, int punkty, int port, InetAddress host)
	{
		this.login = login;
		this.punkty = punkty;
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
			pw.println(SerwerGry.przeslaniePunktow);
			pw.println(login);
			pw.println(punkty);
			socket.close();
		} 
		catch (Exception e) 
		{
			System.err.println("Client exception: " + e);
		}
		
	}

}


