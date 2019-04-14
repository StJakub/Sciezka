import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

/**
 * Klasa odpowiedzilna za rysowanie gry
 * @author Jakub Stanis�aw
 * @param boardWidth - domy�lna szeroko�� planszy
 * @param boardHeight - domy�lna wysoko�� planszy
 * @param p - stosunek szerokosci do wysokosci proporcji
 * @param k - wsp�czynnik skalowania
 */

public class Plansza extends JComponent
{
	public static final int boardWidth = 600;
	public static final int boardHeight = 400;
	private static final double p = (double)boardWidth / (double)boardHeight;				
	
	private static final long serialVersionUID = 1L;
	private OblugaMyszy mysz;
	private ZarzadzanieGra zarzadcaHND;
	private MainWindow oknoHND;						
	private double k;								
	
	/**
	 * 
	 * @param okno - okno, na kt�rym znajduje si� plansza 
	 */
	public Plansza(MainWindow okno) 
	{
		k = 1.0;
		oknoHND = okno;
		mysz = new OblugaMyszy(this);
		this.addMouseMotionListener(mysz);
		this.addMouseListener(mysz);
	}

	/**
	 * Funkcja odpowiedzialna za resetowanie myszy
	 */
	public void resetujMysz()
	{
		mysz.reset();
	}

	/**
	 * Funkcja rysuj�ca plansze i obiekty, kt�re si� na niej znajduj�, a tak�e odpowiedzialna za skalowanie
	 */
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		
		double proporcja = (double)getWidth()/ (double) getHeight();
		
		if(proporcja < p)
		{
			k = (double)getWidth() / boardWidth;
			int newHeight = (int)(k * boardHeight + 0.5);
		}
		else if(proporcja > p)
		{
			k = (double)getHeight() / boardHeight;
			int newWidth = (int)(k * boardWidth + 0.5);
		}
		
		
		g2.scale(k, k);
		// rysowanie tla
		g2.setPaint(Color.GREEN);
		Rectangle2D tlo = new Rectangle2D.Double(0, 0, boardWidth, boardHeight);
		g2.fill(tlo);
		// rysowanie sciezki
		g2.setPaint(Color.GRAY);
		zarzadcaHND.rysujSciezki(g2);
		
		// rysowanie przeszkod
		g2.setPaint(Color.RED);
		zarzadcaHND.rysujPrzeszkody(g2);

		
		// rysowanie bonusow
		g2.setPaint(Color.YELLOW);
		zarzadcaHND.rysujBonusy(g2);
		
		// rysowanie pojazdu
		Pojazd pojazd = zarzadcaHND.dostepDoPojazdu();
		if(pojazd != null)
		{
			g2.setPaint(Color.BLUE);
			g2.fill(pojazd.pobierzKszalt());
		}
	}
	
	/**
	 * Funkcja pobiera uchwyt do klasy Zarz�dzanieGra
	 * @return uchwyt do klasy ZarzadzanieGra
	 */
	public ZarzadzanieGra dostepDoZarzadcy()
	{
		return zarzadcaHND;
	}
	
	/**
	 * Zapewnia dost�p planszy do zarzadcy u�ywanego w programie
	 * @param zarzadca - uchwyt do zarz�dcy
	 */
	public void ustawZarzadce(ZarzadzanieGra zarzadca)
	{
		zarzadcaHND = zarzadca;
	}

	/**
	 * Funkcja zwraca  rozmiar okna po skalowaniu
	 */
	public Dimension getPreferredSize()
	{
		int w = (int)(k * boardWidth + 0.5);
		int h = (int)(k * boardHeight + 0.5);
		return new Dimension(w, h);	
	}
	
	/**
	 * Funkcja zwraca wsp�czynnik skalowania
	 * @return wsp�czynnik skalowania
	 */
public double skala()
	{
		return k;
	}
	
}
