import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JOptionPane;

/**
 * Klasa opisuje wys³anie loginu i punktacji na serwer
 * @author Jakub Stanis³aw
 *
 */
public class PrzeslijPunkty implements Runnable
{
	private String nick;
	private int punkty;
	private int port;
	private InetAddress host;
	
	/**
	 * 
	 * @param nick - login gracza
	 * @param punkty - punkty uzyskane podczas rozgrywki
	 * @param port - port, na którym nastêpuje komunikacja miêdzy serwerem a klientem
	 * @param host - adres IP
	 */
	public PrzeslijPunkty(String nick, int punkty, int port, InetAddress host)
	{
		this.nick = nick;
		this.punkty = punkty;
		this.port = port;
		this.host = host;
	}
	
	public void run()
	{
		try 
		{
			Socket socket = new Socket(host, port);
			
			OutputStream os = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(os, true);
			
			pw.println(nick);
			pw.println(punkty);
			socket.close();
		} 
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), "Przeslanie wyniku na serwer", JOptionPane.WARNING_MESSAGE);
		}
	}
}
