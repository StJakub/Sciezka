import java.awt.geom.Ellipse2D;

/**
 * 
 * @author Jakub Stanis³aw
 * Klasa reprezentuje pojazd w postaci ko³a
 */

public class Pojazd 
{
	
	private double xPojazd;
	private double yPojazd;
	private double promien;
	
	private String login;
	private int sumaPunktow;
	private int szans;
	private int numerPoziomu;				
	
	private ZarzadzanieGra zarzadzanieHND;
	
	/**
	 * 
	 * @return funkcja zwraca uchwyt do klasy ZarzadzanieGra
	 */
	public ZarzadzanieGra dajDostep()
	{
		return zarzadzanieHND;
	}
	
	/**
	 * 
	 * @param - xPojazd - wspó³rzêdna pojazdu wzglêdem osi OX
     * @param - yPojazd - wspó³rzêdna pojazdu wzglêdem osi OY
     * @param - promien - promien pojazdu
     * @param - login - login gracza
	 * @param - zarzadzanie - uchwyt do klasy ZarzadzanieGra
	 */
	public Pojazd(double xPojazd, double yPojazd, double promien, ZarzadzanieGra zarzadzanie, String login)
	{
		this.xPojazd = xPojazd;
		this.yPojazd = yPojazd;
		this.promien = promien;
		zarzadzanieHND = zarzadzanie;
		this.login = login;
	}
	
	/**
	 * Funkcja pobiera login gracza
	 * @return String zawieraj¹cy login gracza
	 */
	
	public String pobierzLogin()
	{
		return login;
	}
	
	/**
	 * 
	 * @return funkcja zwraca liczbê punktów uzyskanych przez gracza podczas rozgrywki
	 */
	public int ileZdobyto()
	{
		return sumaPunktow;
	}
	
	/**
	 * Funkcja dodaje do sumy punktów, punkty za ka¿dy ukoñczony poziom
	 */
	public void przyznajPunkty()
	{
		sumaPunktow += numerPoziomu * 50;
	}
	
	/**
	 * Funkcja resetuje parametry ustawia poziom na 1 po ponownym uruchomieniu nowej gry
	 */
	public void resetuj()
	{
		szans = 3;
		numerPoziomu = 1;
		sumaPunktow = 0;
	}
	
	/**
	 * FUnkcja sprawdza poziom, na którym znajduje siê gracz
	 * @return zmienna okreœlaj¹ca numer poziomu
	 */
	public int ktoryPoziom()
	{
		return numerPoziomu;
	}
	
	/**
	 * Funkcja jest odpowiedzialna za przeniesienie gracza na wy¿szy poziom po ukoñczeniu poprzedniego
	 */	
	public void przeniesNaKolejnyPoziom()
	{
		++numerPoziomu;
	}
	
/**
 * funkcja zabiera szanse i sprawdza czy mozna kontynuowac
 */
	public void ZabierzSzanse()
	{
		szans -= 1;
	}
	
	/**
	 *  Funkcja sprawdza czy liczba szans jest ró¿na od 0
	 * @return czy liczba szans jest równa 0
	 */
	public boolean czyJestSzansa()
	{
		return szans != 0;
	}
	
	/**
	 * Funkcja zwraca liczbê szans jakie pozosta³y graczowi
	 * @return zmienna okreœlaj¹ca ile szans jeszcze zosta³o
	 */
	public int ileSzans()
	{
		return szans;
	}
	
	/**
	 * Funkcja zmienia po³o¿enie pojazdu
	 * @param newX - nowa wspó³rzêdna wzglêdem osi OX
	 * @param newY - nowa wspó³rzêdna wzglêdem osi OY
	 */
	public void zmienPozycje(double newX, double newY)
	{
		xPojazd = newX;
		yPojazd = newY;
	}
	
	/**
	 * Funkcja przesuwa pojazd o zadane d³ugoœci
	 * @param dx - przesuniêcie wzglêdem osi OX
	 * @param dy - przesuniêcie wzglêdem osi OY
	 */
	public void przesun(double dx, double dy)
	{
		xPojazd += dx;
		yPojazd += dy;
	}
	
	/**
	 * Funkcja pobiera kszta³t pojazdu
	 * @return kszta³t pojazdu
	 */
	public Ellipse2D pobierzKszalt()
	{
		return new Ellipse2D.Double(xPojazd - promien, yPojazd - promien, promien + promien, promien + promien);
	}
	
	/**
	 * Funkcja sprawdza czy kursor myszy znajduje siê wewn¹trz pojazdu
	 * @param mouseX - wspó³rzêdna myszy wzglêdem osi OX
	 * @param mouseY - wspó³rzêdna myszy wzglêdem osi OY
	 * @return czy kursor znajduje siê wewn¹trz pojazdu
	 */
	public boolean czyWewnatrzPojazdu(final double mouseX, final double mouseY)
	{
		return Math.pow(xPojazd - mouseX, 2.0) + Math.pow(yPojazd - mouseY, 2.0) <= Math.pow(promien, 2.0);
	}
	
	/**
	 * Funkcja sprawdza po³o¿enie pojazdu wzglêdem osi OY
	 * @return wspó³rzedna Y pojazdu
	 */
	public double getY()
	{
		return yPojazd;
	}
	
	/**
	 * Funkcja sprawdza po³o¿enie pojazdu wzglêdem osi OX
	 * @return wspó³rzedna X pojazdu
	 */
	public double getX()
	{
		return xPojazd;
	}
	
	/**
	 * Funkcja sprawdza promieñ
	 * @return zmienna okreœlaj¹ca promieñ
	 */
	public double getWielkosc()
	{
		return promien;
	}
	
	/**
	 * Funkcja odpowiedzialna za sprawdzenie odleglosci pojazdu od bonusu
	 * @param s - sciezka, na której znajduje siê bonus
	 */
	public void sprawdzBonus(Sciezka s)
	{
	
		s.sprawdzBonus(this);
	}
	
	/**
	 * Funkcja dodaje bonus do sumy punktów gracza
	 * @param b - bonus, na który najecha³ pojazd
	 */
	void dodajPunkty(Bonus b)
	{
		sumaPunktow += b.przekaz();
	}
	
	/**
	 * Funkcja pobiera sumê punktów
	 * @return zmienna zawieraj¹ca sumê punktów
	 */
	int pobierzPunkty()
	{
		return sumaPunktow;
	}
	
	
	
}
