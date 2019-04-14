import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * Klasa charakteryzuje przeszkode
 * @author Jakub Stanis�aw
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
	 * @param Ax - amplituda przemieszczania si� przeszkody wzgl�dem osi OX
	 * @param Ay - amplituda przemieszczania si� przeszkody wzgl�dem osi OX
	 * @param alpha - k�t
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
	 * Funkcja zapobiega przemieszczaniu si� przeszkody wraz z przesuwaniem si� planszy
	 * @param ddy - wielko�� o ktora przemiescila sie plansza
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
	 * Funkcja s�u�y zwr�ceniu aktualnego po�o�enia przeszkody wzgl�dem osi OX
	 * @return po�o�enie wzgl�dem osi OY
	 */
	public double getX()
	{
		return polozenie.getX();
	}
	
	/**
	 * Funkcja s�u�y zwr�ceniu aktualnego po�o�enia przeszkody wzgl�dem osi OY
	 * @return po�o�enie wzgl�dem osi OY
	 */
	public double getY()
	{
		return polozenie.getY();
	}
	
	/**
	 * Funkcja s�u�y zwr�ceniu promienia przeszkody 
	 * @return promien przeszkody
	 */
	public double promienWielkosci()
	{
		return r;
	}
	
	
	

}
