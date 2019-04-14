
/**
 * Klasa opisuj¹ca najlepszy wynik
 * @author Jakub Stanis³aw
 *
 */
public class Rekord 

{
	private String login;
	private int punkty;
	
	/**
	 * @param login nick gracza
	 * @param punkty - liczba punktów uzyskanych przez gracza podczas rozgrywki
	 */
	Rekord(String login, int punkty)
	{
		this.login = login;
		this.punkty = punkty;
	}
	
	String getValue(int col)
	{
		if(col == 0)
			return login;
		return String.valueOf(punkty);
		
	}
	
	public boolean equal(String innyLogin)
	{
		return login.equals(innyLogin);
	}
	
	public void poprawWynik(int nowyWynik)
	{
		if(nowyWynik > punkty)
			punkty = nowyWynik;
	}
	
	/**
	 * Metoda porównawcza zastosowana w celu aktualizacji nalepszych wyników
	 * @param r - rekord
	 * @return login
	 */
	public int compareTo(Rekord r)
	{
		if(punkty > r.punkty)
			return -1;
		if(punkty < r.punkty)
			return 1;
		return login.compareTo(r.login);
	}
	
	// do zapisu rekordu w pliku
	public String toString()
	{
		return login + " " + String.valueOf(punkty);
	}
}
