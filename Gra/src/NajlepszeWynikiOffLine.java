import java.util.ArrayList;

/**
 * Klasa odpowiedzialna za pobieranie, zapisywanie i odczytywanie najlepszych wynik�w w trybie offline
 * @author Jakub Stanis�aw
 * @param najlepsze - tablica 5 najlepszych rekord�w
 * @param nazwaPliku - plik przechowuje list� najlepszych wynik�w
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
	 * Funkcja dodaje wynik do listy rekord�w
	 */
	public void dodajWynik(String login, int pkt)
	{
		TableRecordModel.dodajWynik2(najlepsze, login, pkt);
	}
	
	/**
	 * Funkcja pobiera list� najlepszych wynik�w
	 * @return String zawieraj�cy wyniki
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
