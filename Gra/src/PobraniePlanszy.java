import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * Klasa odpowiedzialna za pobranie planszy z serwera
 * @author Jakub Stanis³aw
 *
 */
public class PobraniePlanszy implements Runnable
{
	private final int poziom;
	private ZarzadzanieGra zarzadzanie;
	private int port;
	private InetAddress host;
	
	/**
	 * 
	 * @param poziom - numer poziomu, który nale¿y przes³aæ
	 * @param zarzadzanie - zarz¹dca 
	 * @param port - numer portu, na którym ma miejsce komunikacja miêdzy klientem a serwerem
	 * @param host - adres IP
	 */
	PobraniePlanszy(int poziom, ZarzadzanieGra zarzadzanie, int port, InetAddress host)
	{
		this.poziom = poziom;
		this.zarzadzanie = zarzadzanie;
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
			
			pw.println(SerwerGry.zadajPlansze);
			pw.println(poziom);
			
			InputStream is = socket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			ArrayList<Sciezka> nowaDroga = ZarzadzanieGra.wczytajZbufora(br);
			
			socket.close();
			
			zarzadzanie.ustalDroge(nowaDroga);
			zarzadzanie.uruchomGre();
		} 
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, e.getMessage(), "Polaczenie do serwera", JOptionPane.WARNING_MESSAGE); 
			try
			{
				zarzadzanie.wczytajPlanszeLokalnie();
				zarzadzanie.ustalTrybZdalnie(false);
				zarzadzanie.uruchomGre();
			}
			catch (IOException ev) 
			{
				JOptionPane.showMessageDialog(null, ev.getLocalizedMessage(), "Wczytanie planszy z pliku", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}

