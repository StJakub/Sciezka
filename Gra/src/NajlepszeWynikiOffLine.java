import java.util.ArrayList;

/**
 * Klasa odpowiedzialna za pobieranie, zapisywanie i odczytywanie najlepszych wyników w trybie offline
 * @author Jakub Stanis³aw
 * @param najlepsze - tablica 5 najlepszych rekordów
 * @param nazwaPliku - plik przechowuje listê najlepszych wyników
 */
public class NajlepszeWynikiOffLine
{
	private ArrayList<Rekord> najlepsze;
	private static String nazwaPliku = "lokalneWyniki.txt";
	

	public NajlepszeWynikiOffLine()
	{
		najlepsze = new ArrayList<Rekord>();
		TableRecordModel.wczytajWyniki(najlepsze, nazwaPliku);
	}
	
	/**
	 * Funkcja zapisuje wyniki do pliku
	 */
	public void zapiszWyniki()
	{
		TableRecordModel.zapiszWyniki2(najlepsze, nazwaPliku);
	}
	
	/**
	 * Funkcja dodaje wynik do listy rekordów
	 */
	public void dodajWynik(String login, int pkt)
	{
		TableRecordModel.dodajWynik2(najlepsze, login, pkt);
	}
	
	/**
	 * Funkcja pobiera listê najlepszych wyników
	 * @return String zawieraj¹cy wyniki
	 */
	public String pobierz()
	{
		String wyniki = "";
		for(int i = 0; i < najlepsze.size(); ++i)
		{
			wyniki += najlepsze.get(i).toString();
			wyniki += "\n";
		}
		return wyniki;
	}
}
