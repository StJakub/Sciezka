import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.table.AbstractTableModel;

/**
 * Klasa odpowiedzialna za tabele najlepszych wyników
 * @author Jakub Stanis³aw
 *
 */

public class TableRecordModel extends AbstractTableModel
	{
		private final static String nazwaPliku = "rekordy.txt";							
		private static final long serialVersionUID = 3629893619023455248L;
		private final String [] kolumny = {"Login", "Punkty" };
		private ArrayList<Rekord> rekordy;
		
		
		public TableRecordModel()
		{
			rekordy = new ArrayList<Rekord>();
			wczytajWyniki(rekordy, nazwaPliku);
			this.fireTableDataChanged();
		}
		
		/**
		 * Funkcja s³u¿y pobraniu rekordu
		 * @param index - numer rekordu
		 * @return rekord przekonwertowany na ci¹g znaków
		 */
		public String pobierzRekord(int index)
		{
			Rekord rd = rekordy.get(index);
			return rd.toString();
		}
		
		/**
		 * Funkcja odpowiada za wczytanie wyników
		 * @param rekordy - tablica najlepszych wyników
		 * @param nazwa - nazwa pliku, z którego wczytywane s¹ rekordy
		 */
		public static void wczytajWyniki(ArrayList<Rekord> rekordy, String nazwa)
		{
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(nazwa));
				while(in.ready())
				{
					String wiersz = in.readLine();				// zakladamy ze sa posortowane
					String [] elementy = wiersz.split(" ");		// zakladamy ze w loginie nie ma spacji
					int punktow = Integer.parseInt(elementy[1]);
					rekordy.add(new Rekord(elementy[0], punktow));
				}
				in.close();
			}
			catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/**
		 * Funkcja odpowiedzialna za zapisanie najlepszych wyników
		 */
		public void zapiszWyniki()
		{
			zapiszWyniki2(rekordy, nazwaPliku);
		}
		
		/**
		 * Funkcja s³u¿aca zapisaniu rekordów do pliku po zakoñczeniu rozgrywki
		 * @param rekordyDoZapisu - najlepsze wyniki 
		 * @param nazwa - nazwa pliku, do którego nastêpuje zapis
		 */
		public static void zapiszWyniki2(ArrayList<Rekord> rekordyDoZapisu, String nazwa)
		{
			PrintWriter out;
			try 
			{
				out = new PrintWriter(nazwa);
				for(int i = 0; i < rekordyDoZapisu.size(); ++i)
				{
					out.println(rekordyDoZapisu.get(i));
				}
				out.close();
			} 
			catch (FileNotFoundException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/**
		 * Funkcja dodaje wynik do listy rekordów
		 * @param rek - tablica rekordów
		 * @param login - login gracza
		 * @param pkt - liczba punktów uzyskanych przez gracza
		 */
		public static void dodajWynik2(ArrayList<Rekord> rek, String login, int pkt)
		{
			for(int i = 0; i < rek.size(); ++i)
			{
				Rekord r = rek.get(i);
				if(r.equal(login))
				{
					r.poprawWynik(pkt);
					Collections.sort(rek, new RekordComparator());
					return;
				}
			}
			rek.add(new Rekord(login, pkt));
			Collections.sort(rek, new RekordComparator());
			if(rek.size() > 5)
				rek.remove(5);
		}
		
		
		/**
		 * Funkcja s³u¿y dodaniu wyniku do tabeli rekordów
		 * @param login - login gracza
		 * @param pkt - liczba punktów uzyskanych w trakcie rozgrywki
		 */
		public void dodajWynik(String login, int pkt)
		{
			dodajWynik2(rekordy, login, pkt);
			this.fireTableDataChanged();
		}

		
		@Override
		/**
		 * Funkcja zwraca liczbê kolumn w tabeli
		 */
		public int getColumnCount() {
			// TODO Auto-generated method stub
			return kolumny.length;
		}

		@Override
		/**
		 * Funkcja zwraca liczbê rzêdów w tabeli
		 */
		public int getRowCount() {
			// TODO Auto-generated method stub
			return rekordy.size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Rekord r = rekordy.get(rowIndex);
			return r.getValue(columnIndex);
		}
		
		@Override
		public String getColumnName(int col)
		{
			return kolumny[col];
		}
		
		/**
		 * Funkcja blokuje edycje tablicy
		 */
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}
		
		
	}
	
	/**
	 * Klasa odpowiada za sortowanie wyników
	 * @author Jakub Stanis³aw
	 *
	 */
	class RekordComparator implements Comparator<Rekord> {

		@Override
		public int compare(Rekord r1, Rekord r2) {
			// TODO Auto-generated method stub
			return r1.compareTo(r2);
		}
	}