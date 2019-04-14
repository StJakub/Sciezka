import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;
/**
 * Klasa opisuje œcie¿kê po której porusza siê pojazd
 * @author Jakub Stanis³aw
 * @param przeszkody - tablica, w której znajduja siê przeszkody
 * @param bonusy - tablica, w której znajduja siê bonusy
 * @param wspolczynnikiLewej - wspolczynniki prostej lewej
 * @param wspolczynnikiPrawej - wspolczynniki prostej prawej
 */
public class Sciezka 
{
	private double height;
	private double xLeftDown;
	private double xRightDown;
	private double xLeftUp;
	private double xRightUp;
	private double yDown;
	
	private ArrayList<Przeszkoda> przeszkody;
	private ArrayList<Bonus> bonusy;

	private double [] wspolczynnikiLewej;
	private double [] wspolczynnikiPrawej;

	/**
	 * Funkcja obliczajaca wspolczynniki prostej na podstawie punktow znajdujacych sie na niej
	 * @param wspolczynniki[0] - A
	 * @param wspolczynniki[1] - B
	 * @param wspolczynniki[2] - C
	 * @param wspolczynniki[3] - pierwiastek z (A^2 + B^2) 
	 * @param x0 - wspó³rzêdna x punktu 1 przechodz¹cego przez prost¹
	 * @param y0 - wspó³rzêdna y punktu 1 przechodz¹cego przez prost¹
	 * @param x1 - wspó³rzêdna x punktu 2 przechodz¹cego przez prost¹
	 * @param y1 - wspó³rzêdna y punktu 2 przechodz¹cego przez prost¹
	 * @return wspolczynniki prostej
	 */
	private static double [] wyliczWspolczynnikiProstej(double x0, double y0, double x1, double y1)
	{
		double [] wspolczynniki = new double [4];
		if(x0 == x1) 
		{
			wspolczynniki[0] = 0;
			wspolczynniki[1] = y1-y0;
			wspolczynniki[2] = 0;
			wspolczynniki[3] = Math.sqrt(Math.abs(y1-y0));;
		}
		else 
		{
			wspolczynniki[0] = (y1 - y0) / (x0 - x1);
			wspolczynniki[1] = 1;
			wspolczynniki[2] = - y0 - wspolczynniki[0] * x0;
			wspolczynniki[3] = Math.sqrt(Math.pow(wspolczynniki[0], 2.0) + Math.pow(wspolczynniki[1], 2.0));
		}
		return wspolczynniki;
	}
	
	/**
	 * 
	 * @param xLeftDown - wspolrzedna x dolnego punktu po lewej stronie
	 * @param xRightDown - wspolrzedna x dolnego punktu po prawej stronie
	 * @param xLeftUp - wspolrzedna x górnego punktu po lewej stronie
	 * @param xRightUp - wspolrzedna x górnego punktu po prawej stronie
	 * @param height - wysokosc, odleglosc pomiedzy dolnym a gornym punktem na osi OY
	 * @param yDown - dolna wartosc y
	 */
	public Sciezka(double xLeftDown, double xRightDown, double xLeftUp, double xRightUp, double height, double yDown)
	{
		this.height = height;
		this.xLeftDown = xLeftDown;
		this.xRightDown = xRightDown;
		this.xLeftUp = xLeftUp;
		this.xRightUp = xRightUp;
		this.yDown = yDown;
		
		przeszkody = null;
		bonusy = null;
	}
	
	/**
	 * Funkcja odpowiedzialna za wype³nienie kszta³tu bonusów
	 * @param g2 - obiekt graficzny 
	 */
	public void rysujBonusy(Graphics2D g2)
	{
		for(Bonus b : bonusy)
			g2.fill(b.pobierzKszalt());
	}
	
	/**
	 * Funkcja odpowiedzialna za wype³nienie kszta³tu przeszkód
	 * @param g2 - obiekt graficzny 
	 */
	public void rysujPrzeszkody(Graphics2D g2)
	{
		for(Przeszkoda p : przeszkody)
			g2.fill(p.pobierzKszalt());
	}
	
	/**
	 * Funkcja odpowiada za dodanie przeszkód
	 * @param przeszkody tablica z przeszkodami
	 */
	public void dodajPrzeszkody(ArrayList<Przeszkoda> przeszkody)
	{
		this.przeszkody = przeszkody;
	}
	
	/**
	 * Funkcja odpowiada za dodanie bonusów
	 * @param przeszkody tablica z bonusami
	 */
	public void dodajBonusy(ArrayList<Bonus> bonusy)
	{
		this.bonusy = bonusy;
	}
		/**
		 * Funkcja ³¹czy ze sob¹ punkty, tak aby powsta³a œcie¿ka
		 * @return kszta³t œcie¿ki
		 */
	public Shape getShape()
	{
		GeneralPath ksztalt = new GeneralPath();
		ksztalt.moveTo(xLeftDown, yDown);
		ksztalt.lineTo(xRightDown, yDown);
		ksztalt.lineTo(xRightUp, yDown + height);
		ksztalt.lineTo(xLeftUp, yDown + height);
		ksztalt.closePath();
		return ksztalt;
	}
	
	/**
	 * Funkcja przesuwa œcie¿kê w dó³ i sprawia, ¿e bonusy i przeszkody nie zmieniaj¹ swojego po³o¿enia
	 * @param dy - przesuniêcie planszy 
	 */
	public void moveDown(double dy)
	{
		yDown -= dy;
	
		for(Przeszkoda p : przeszkody)
			p.moveDown(dy);
		
		for(Bonus b : bonusy)
			b.moveDown(dy);
		
	}
	
	/**
	 * Funkcja pobiera doln¹ wartoœæ y prostych tworz¹cych œcie¿kê 
	 * @return zmienna przechowuj¹ca doln¹ wartoœæ y œcie¿ki
	 */
	public double getDown()
	{
		return yDown;
	}
	
	/**
	 * Funkcja pobiera górna wartoœæ y prostych tworz¹cych œcie¿kê 
	 * @return gorna wartoœæ y œcie¿ki
	 */
	public double getUp()
	{
		return yDown + height;
	}
	
	/**
	 * Funkcja pobiera œrodek wspó³rzednych osi OX tworz¹cych œcie¿kê
	 * @return wartoœæ œrodka wspó³rzednych osi OX typu double
	 */
	public double getCenterDownX()
	{
		return (xLeftDown + xRightDown) /2.0;
	}
	
	/**
	 * Funkcja sprawdza czy mia³a miejsce kolizja
	 * @param p - pojazd poruszaj¹cy siê po œcie¿ce
	 * @return - czy nast¹pi³a kolizja
	 */
	public boolean czyKolizja(Pojazd p)
	{
		boolean pozaDraga = czyPozaDroga(p);
		if(pozaDraga)
			return true;
		boolean kolizjaPrzeszkoda = czyKolizjaPrzeszkoda(p);
		if(kolizjaPrzeszkoda)
			return true;
		
		return false;
	}
	
	/**
	 * Funkcja sprawdza czy pojazd najecha³ na bonus
	 * @param p - pojazd poruszaj¹cy siê po œcie¿ce
	 * @return
	 */
	public void sprawdzBonus(Pojazd p)
	{
		for(Bonus bonus : bonusy)
		{
			double kw_odleglosci = Math.pow(p.getX() - bonus.getX(), 2.0) + Math.pow(p.getY() - bonus.getY(), 2.0);
			double kw_roznicaBezwzgl = Math.pow(p.getWielkosc() - bonus.promienWielkosci(),2.0);
			if(kw_odleglosci <= kw_roznicaBezwzgl)		
				p.dodajPunkty(bonus);
		}	
	}
	
	/**
	 * Funkcja sprawdza czy pojazd zderzyl sie z przeszkoda
	 * @param p - pojazd poruszaj¹cy siê po œcie¿ce
	 * @return czy kolizja z przeszkod¹
	 */
	public boolean czyKolizjaPrzeszkoda(Pojazd p)
	{
		for(Przeszkoda przeszkoda : przeszkody)
		{
			double kw_odleglosci = Math.pow(p.getX() - przeszkoda.getX(), 2.0) + Math.pow(p.getY() - przeszkoda.getY(), 2.0);
			double kw_R_pojazdu_R_przeszkody = Math.pow(p.getWielkosc() + przeszkoda.promienWielkosci(), 2.0);
			if(kw_odleglosci <= kw_R_pojazdu_R_przeszkody)
				return true;
		}
		return false;
	}
	
	
	/**
	 * Funkcja sprawdza czy nast¹pi³a kolizja pojazdu ze œcie¿k¹
	 * @param p - pojazd poruszaj¹cy siê po œcie¿ce
	 * @return czy kolizja ze œcie¿k¹
	 */
	public boolean czyPozaDroga(Pojazd p)
	{
		wspolczynnikiLewej = wyliczWspolczynnikiProstej(xLeftDown, yDown, xLeftUp, yDown + height);
		wspolczynnikiPrawej = wyliczWspolczynnikiProstej(xRightDown, yDown, xRightUp, yDown + height);
		
		
		double odlegloscOdLewej = (wspolczynnikiLewej[0] * p.getX() + wspolczynnikiLewej[1] * p.getY() + wspolczynnikiLewej[2]) / wspolczynnikiLewej[3];
		double odlegloscOdPrawej = (wspolczynnikiPrawej[0] * p.getX() + wspolczynnikiPrawej[1] * p.getY() + wspolczynnikiPrawej[2]) / wspolczynnikiPrawej[3];
		
		
		if(odlegloscOdLewej > 0 && odlegloscOdPrawej > 0 ||
			odlegloscOdLewej < 0 && odlegloscOdPrawej < 0)
			return true;
			
		if(odlegloscOdLewej < 0)
			odlegloscOdLewej = -odlegloscOdLewej;
		
		if(odlegloscOdLewej <= p.getWielkosc())
			return true;
		
		if(odlegloscOdPrawej < 0)
			odlegloscOdPrawej = -odlegloscOdPrawej;
		
		if(odlegloscOdPrawej <= p.getWielkosc())
			return true;
			
		
		return false;
			
	}
	
}
