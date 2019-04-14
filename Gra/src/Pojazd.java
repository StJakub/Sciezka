import java.awt.geom.Ellipse2D;

/**
 * 
 * @author Jakub Stanis�aw
 * Klasa reprezentuje pojazd w postaci ko�a
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
	 * @param - xPojazd - wsp�rz�dna pojazdu wzgl�dem osi OX
     * @param - yPojazd - wsp�rz�dna pojazdu wzgl�dem osi OY
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
	 * @return String zawieraj�cy login gracza
	 */
	
	public String pobierzLogin()
	{
		return login;
	}
	
	/**
	 * 
	 * @return funkcja zwraca liczb� punkt�w uzyskanych przez gracza podczas rozgrywki
	 */
	public int ileZdobyto()
	{
		return sumaPunktow;
	}
	
	/**
	 * Funkcja dodaje do sumy punkt�w, punkty za ka�dy uko�czony poziom
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
	 * FUnkcja sprawdza poziom, na kt�rym znajduje si� gracz
	 * @return zmienna okre�laj�ca numer poziomu
	 */
	public int ktoryPoziom()
	{
		return numerPoziomu;
	}
	
	/**
	 * Funkcja jest odpowiedzialna za przeniesienie gracza na wy�szy poziom po uko�czeniu poprzedniego
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
	 *  Funkcja sprawdza czy liczba szans jest r�na od 0
	 * @return czy liczba szans jest r�wna 0
	 */
	public boolean czyJestSzansa()
	{
		return szans != 0;
	}
	
	/**
	 * Funkcja zwraca liczb� szans jakie pozosta�y graczowi
	 * @return zmienna okre�laj�ca ile szans jeszcze zosta�o
	 */
	public int ileSzans()
	{
		return szans;
	}
	
	/**
	 * Funkcja zmienia po�o�enie pojazdu
	 * @param newX - nowa wsp�rz�dna wzgl�dem osi OX
	 * @param newY - nowa wsp�rz�dna wzgl�dem osi OY
	 */
	public void zmienPozycje(double newX, double newY)
	{
		xPojazd = newX;
		yPojazd = newY;
	}
	
	/**
	 * Funkcja przesuwa pojazd o zadane d�ugo�ci
	 * @param dx - przesuni�cie wzgl�dem osi OX
	 * @param dy - przesuni�cie wzgl�dem osi OY
	 */
	public void przesun(double dx, double dy)
	{
		xPojazd += dx;
		yPojazd += dy;
	}
	
	/**
	 * Funkcja pobiera kszta�t pojazdu
	 * @return kszta�t pojazdu
	 */
	public Ellipse2D pobierzKszalt()
	{
		return new Ellipse2D.Double(xPojazd - promien, yPojazd - promien, promien + promien, promien + promien);
	}
	
	/**
	 * Funkcja sprawdza czy kursor myszy znajduje si� wewn�trz pojazdu
	 * @param mouseX - wsp�rz�dna myszy wzgl�dem osi OX
	 * @param mouseY - wsp�rz�dna myszy wzgl�dem osi OY
	 * @return czy kursor znajduje si� wewn�trz pojazdu
	 */
	public boolean czyWewnatrzPojazdu(final double mouseX, final double mouseY)
	{
		return Math.pow(xPojazd - mouseX, 2.0) + Math.pow(yPojazd - mouseY, 2.0) <= Math.pow(promien, 2.0);
	}
	
	/**
	 * Funkcja sprawdza po�o�enie pojazdu wzgl�dem osi OY
	 * @return wsp�rzedna Y pojazdu
	 */
	public double getY()
	{
		return yPojazd;
	}
	
	/**
	 * Funkcja sprawdza po�o�enie pojazdu wzgl�dem osi OX
	 * @return wsp�rzedna X pojazdu
	 */
	public double getX()
	{
		return xPojazd;
	}
	
	/**
	 * Funkcja sprawdza promie�
	 * @return zmienna okre�laj�ca promie�
	 */
	public double getWielkosc()
	{
		return promien;
	}
	
	/**
	 * Funkcja odpowiedzialna za sprawdzenie odleglosci pojazdu od bonusu
	 * @param s - sciezka, na kt�rej znajduje si� bonus
	 */
	public void sprawdzBonus(Sciezka s)
	{
	
		s.sprawdzBonus(this);
	}
	
	/**
	 * Funkcja dodaje bonus do sumy punkt�w gracza
	 * @param b - bonus, na kt�ry najecha� pojazd
	 */
	void dodajPunkty(Bonus b)
	{
		sumaPunktow += b.przekaz();
	}
	
	/**
	 * Funkcja pobiera sum� punkt�w
	 * @return zmienna zawieraj�ca sum� punkt�w
	 */
	int pobierzPunkty()
	{
		return sumaPunktow;
	}
	
	
	
}
