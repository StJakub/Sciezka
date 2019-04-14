import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * @author Jakub Stanis³aw
 * Klasa reprezentuje bonus w postaci ko³a
 * @param wartosc - reprezentuje ilosc punktow do zdobycia
 * @param polozenie - reprezentuje polozenie bonusu na planszy
 * @param promien - promien bonusu
 * 
 */
public class Bonus
{
	private int wartosc;
	private Point2D.Double polozenie;
	private int promien;
	
	/**
	 * 
	 * @param x - polozenie wzgledem wspolrzednej x
	 * @param y - polozenie wzgledem wspolrzednej y
	 * @param wartosc - nadaje wartosc bonusowi
	 */
	public Bonus(double x, double y, int wartosc)
	{
		polozenie = new Point2D.Double(x,  y);
		this.wartosc = wartosc;
		promien = 7;
	}
	
	/**
	 * @return aktualne polozenie wzgledem osi 0X
	 */
	public double getX()
	{
		return polozenie.getX();
	}
	
	/**
	 * @return aktualne polozenie wzgledem osi 0Y
	 */
	public double getY()
	{
		return polozenie.getY();
	}
	
	/**
	 * @return promien bonusu
	 */
	public double promienWielkosci()
	{
		return promien;
	}
	
	/**
	 * Funkcja zeruje wartosc bonusu i przekazuje do ogolnej punktacji
	 * @return wartosc bonusu 
	 */
	public int przekaz()
	{
		int ilosc = wartosc;
		wartosc = 0;
		return ilosc;
	}
	
	/**
	 * @return funkcja zwraca kszta³t bonusu
	 */
	public Ellipse2D pobierzKszalt()
	{
		return new Ellipse2D.Double(polozenie.x - promien, polozenie.y - promien, promien + promien, promien + promien);
	}
	
	/**
	 * Funkcja zapobiega przemieszczaniu siê bonusu
	 * @param dy - przesuniêcie planszy 
	 */
	public void moveDown(double dy)
	{
		polozenie.y -= dy;
	}

}
