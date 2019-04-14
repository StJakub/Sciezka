import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * Klasa charakteryzuje przeszkode
 * @author Jakub Stanis³aw
 *
 */
public class Przeszkoda
{
	private Point2D.Double start;
	private Point2D.Double polozenie;		
	private double r;
	private double alpha;
	private double amplitudaX;
	private double amplitudaY;
	
	/**
	 * 
	 * @param x - wspolrzedna x
	 * @param y - wspolrzedna y
	 * @param Ax - amplituda przemieszczania siê przeszkody wzglêdem osi OX
	 * @param Ay - amplituda przemieszczania siê przeszkody wzglêdem osi OX
	 * @param alpha - k¹t
	 */
	public Przeszkoda(double x, double y, double Ax, double Ay, double alpha)
	{
		polozenie = new Point2D.Double(x,  y);
		start = new Point2D.Double(x,  y);
		r = 10;
		amplitudaX = Ax;
		amplitudaY = Ay;
		this.alpha = alpha;
	}
	
	/**
	 * Funkcja zapobiega przemieszczaniu siê przeszkody wraz z przesuwaniem siê planszy
	 * @param ddy - wielkoœæ o ktora przemiescila sie plansza
	 */
	public void moveDown(double ddy)
	{
		start.y -= ddy;
		alpha += 10;
		double dx = amplitudaX * Math.cos(alpha * Math.PI / 180.0);
		double dy = amplitudaY * Math.sin(alpha * Math.PI / 180.0);
		polozenie.x = start.x + dx;
		polozenie.y = start.y + dy;
	}
	
	public Ellipse2D pobierzKszalt()
	{
		return new Ellipse2D.Double(polozenie.x - r, polozenie.y - r, r + r, r + r);
	}
	
	/**
	 * Funkcja s³u¿y zwróceniu aktualnego po³o¿enia przeszkody wzglêdem osi OX
	 * @return po³o¿enie wzglêdem osi OY
	 */
	public double getX()
	{
		return polozenie.getX();
	}
	
	/**
	 * Funkcja s³u¿y zwróceniu aktualnego po³o¿enia przeszkody wzglêdem osi OY
	 * @return po³o¿enie wzglêdem osi OY
	 */
	public double getY()
	{
		return polozenie.getY();
	}
	
	/**
	 * Funkcja s³u¿y zwróceniu promienia przeszkody 
	 * @return promien przeszkody
	 */
	public double promienWielkosci()
	{
		return r;
	}
	
	
	

}
