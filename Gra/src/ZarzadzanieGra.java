import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;

/**
 * Klasa odpowiedzialna za zarz¹dzanie i sterowanie gr¹
 * @author Jakub Stanis³aw
 * @param planszaWidokHND - uchwyt do klasy plansza
 * @param droga - tablica przechowuj¹ca odcinki drogi
 * @param wyœwietleniePunktów - wyœwietla liczbê zdobytych punktów podczas rozgrywki
 * @param wyswietlenieSzans - wyslwietla liczbe szans 
 * @param port -numer portu przez który nastêpuje komunikacja z serwerem
 * @param host - numerIP hosta, z którym nastepuje komunikacja
 * 
 */

public class ZarzadzanieGra 
{
	private Pojazd pojazd;							
	private Plansza planszaWidokHND;
	private ArrayList<Sciezka> droga;
	private int aktualnyNumerSciezki;						
	private NajlepszeWynikiOffLine najlepsze;
	
	private JLabel wyswietleniePunktow;
	private JLabel wyswietlenieSzans;

	private int sumaDy;
	private Timer zegar;
	private boolean wToku;
	private boolean pauza;
	private Thread watekPrzeslaniaPunktow;
	
	private int port;
	private InetAddress host;
	
	
	/**
	 * 
	 * @param planszaWidok - uchwyt do klasy plansza
	 * @param plikKonfig - plik konfiguracyjny
	 * 
	 */
	public ZarzadzanieGra(Plansza planszaWidok, String plikKonfig)
	{
		najlepsze = null;
		aktualnyNumerSciezki = -1;
		wyswietleniePunktow = new JLabel("0");
		wyswietlenieSzans = new JLabel("3");
		
		droga = new ArrayList<Sciezka>();
		pojazd = null;
		planszaWidokHND = planszaWidok;
		
		port = -1;
		
		BufferedReader in;
		try 
		{
			in = new BufferedReader(new FileReader(plikKonfig));
			try 
			{
				port = Integer.parseInt(in.readLine());
				String adres = in.readLine();
				host = InetAddress.getByName(adres);
				in.close();
			} 
			catch (NumberFormatException | IOException e) {
				e.printStackTrace();
			}
		} 
		catch (FileNotFoundException e) 
		{
			JOptionPane.showMessageDialog(null, e.getMessage(), "Plik konfiguracyjny", JOptionPane.WARNING_MESSAGE);
		}
		zegar = new Timer(42, new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				przesunPlansze(1);
				
				numerSciezki();			// ustawia aktualnyNumerSciezki
				
				
				if(czyKolizja())
				{
					pojazd.ZabierzSzanse();
					
					if(pojazd.czyJestSzansa())
					{
						planszaWidokHND.resetujMysz();
						
						cofnijPlansze();
						ustawPojazdNaPoczatek();
					}
					else
					{
						
						wToku = false;
						zegar.stop();
						planszaWidokHND.resetujMysz();
						
					}
				}
				sprawdzBonusy();
				boolean czyKoncowka = koncowka();
				if(czyKoncowka)
					zegar.stop();
				
				wyswietlenieSzans.setText("" + pojazd.ileSzans());
				planszaWidokHND.repaint();
				
			}
		});
	}
	
	/**
	 * Funkcja sprawdza czy jest pauza, uzywana, aby gracz nie móg³ poruszaæ pojazdem podczas pauzy
	 * @return czy pauza
	 */
	public boolean czyPauza()
	{
		return pauza;
	}
	/**
	 * Sprawdzenie czy gra funkcjonuje online czy offline
	 * @return zawartosc tablicy wynikow lokalnych
	 */
	public boolean czyZdalnie()
	{
		return najlepsze == null;
	}
	
	/**
	 * Funkcja sprawdza czy gracz zostal ustalony, jesli nie to pojazd i plansza nie moga zostac narysowane
	 * @return czy pojazd zostal narysowany
	 */
	public boolean czyUstalonyGracz()
	{
		return (pojazd != null);
	}
	
	/**
	 * Funkcja po otrzymaniu z okna dialogowego nicku tworzy pojazd
	 * @param login - login gracza
	 */
	public void ustalGracza(String login)
	{
	pojazd = new Pojazd(0, 0, 10, this, login);
	}
	
	/**
	 * Funkcja na ustala jaki jest tryb gry, online czy offline
	 * @param tryb - czy zdalnie
	 */
	public void ustalTrybZdalnie(boolean tryb)
	{
		if(tryb)								// zdalnie
			najlepsze = null;
		else									// lokalnie
			najlepsze = new NajlepszeWynikiOffLine();
	}
	
	/**
	 * Funkcja s³u¿y do zapisania do pliku najlepszych wyników w trybie offline
	 */
	public void zapiszWynikiLokalnie()
	{
		if(najlepsze != null) najlepsze.zapiszWyniki();
	}
	
	/**
	 * Funkcja zwraca aktualna liczbe punktow gracza
	 * @return liczba punktow gracza
	 */
	public JLabel etykietaWyswietlaniaPunktow()
	{
		return wyswietleniePunktow;
	}
	
	/**
	 * Funkcja zwraca aktualna liczbe szans gracza
	 * @return liczba szans gracza
	 */
	public JLabel etykietaWyswietlaniaSzans()
	{
		return wyswietlenieSzans;
	}
	
	/**
	 * Funkcja ustawia pojazd na poczatek drogi na jej srodku
	 */
	public void ustawPojazdNaPoczatek()
	{
		Sciezka pierwszaSc = droga.get(0);
		final double y = 10;
		pojazd.zmienPozycje(pierwszaSc.getCenterDownX(), y);
	}
	
	/**
	 * Funkcja sprawdza czy gracz jest w trakcie rozgrywki
	 * @return czy jest w trakcie rozgrywki
	 */
	public boolean isGameRunning()
	{
		return zegar.isRunning() || wToku;
	}
	
	/**
	 * Funkcja pobiera wyniki z serwera, a jesli nie jest on dostepny to wczytuje je lokalnie
	 * @param win okno aplikacji
	 */
	public void pobierzNajlepsze(MainWindow win)
	{
		if(czyZdalnie())
		{
			PobranieWynikow wyniki = new PobranieWynikow(win, port, host);
			Thread zadanie = new Thread(wyniki);
			zadanie.start();
		}
		else
		{
			String wyniki = najlepsze.pobierz();
			if(wyniki.isEmpty())
				wyniki = "Brak wynikow";
			JOptionPane.showMessageDialog(win, wyniki, "Najpelsze wyniki lokalnie",JOptionPane.INFORMATION_MESSAGE);
		}

	}
	
	/**
	 * Funkcja sprawdza czy nastapila kolizja
	 * @return czy kolizja
	 */
	public boolean czyKolizja()
	{
		if(aktualnyNumerSciezki == -1)
			return false;
		Sciezka s = droga.get(aktualnyNumerSciezki);
		return s.czyKolizja(pojazd);	
	}
	
	/**
	 * Funkcja sprawdza czy zdobyto bonus
	 */
	public void sprawdzBonusy()
	{
		if(aktualnyNumerSciezki != -1)
		{
			pojazd.sprawdzBonus(droga.get(aktualnyNumerSciezki));
			wyswietleniePunktow.setText("" + pojazd.pobierzPunkty());
		}
	}
	
	/**
	 *  Funkcja próbuje sie skomunikowac z serwerem, jesli to sie nie uda, to zwraca false i probuje wczytac plansze lokalnie
	 */
	public void wczytajKolejnyPoziom()
	{
		pojazd.przyznajPunkty();
		if(najlepsze == null)
		{
			WyslanieWyniku wysWyniku = new WyslanieWyniku(pojazd.pobierzLogin(), pojazd.ileZdobyto(), port, host);
			Thread zadanie = new Thread(wysWyniku);
			zadanie.start();
		}
		else
		{
			najlepsze.dodajWynik(pojazd.pobierzLogin(), pojazd.ileZdobyto());
		}

		if(pojazd.ktoryPoziom() < 10)
		{
			pojazd.przeniesNaKolejnyPoziom();
			wczytajPlansze();
			sumaDy = 0;
			zegar.start();
			
		}
	}
	
	/**
	 * Funkcja resetuje grê
	 */
	public void resetuj()
	{
		pojazd.resetuj();
		sumaDy = 0;
	}
	
	/**
	 * Funkcja próbuje wczytaæ plansze z pliku, jeœli tryb online jest niedostepny to próbuje zrobiæ to lokalnie
	 */
	public void wczytajPlansze()
	{
		if(this.czyZdalnie())
		{
			wczytajPlanszeZdalnie();
		}
		else
		{
			try 
			{
				wczytajPlanszeLokalnie();
				uruchomGre();
			} 
			catch (IOException e) 
			{
				JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), "Wczytanie planszy z pliku", JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}
	
	/**
	 * Funkcja odpowiedzialna za zdalne wczytywanie planszy
	 */
	private void wczytajPlanszeZdalnie()
	{
		PobraniePlanszy pobranie = new PobraniePlanszy(pojazd.ktoryPoziom(), this, port, host);
		Thread zadanie = new Thread(pobranie);
		zadanie.start();	
	}
	
	/**
	 * Funkcja ustala nowa sciezke
	 * @param nowaDroga - tablica zawierajaca informacje o sciezce
	 */
	public void ustalDroge(ArrayList<Sciezka> nowaDroga)
	{
		droga = nowaDroga;
	}
	
	/**
	 * Funkcja odpowiedzialna za wczytanie sciezki z pliku konfiguracyjnego
	 * @param in - bufor czytaj¹cy znaki ze strumienia
	 * @return sciezka po wczytaniu informacji z pliku
	 */
	public static ArrayList<Sciezka> wczytajZbufora(BufferedReader in)
	{
		ArrayList<Sciezka> nowaDroga = new ArrayList<Sciezka>();
		try 
		{
			String wiersz = in.readLine();
			String [] elementy = wiersz.split(" ");
			
			byte kod = Byte.parseByte(elementy[0]);
			int xL = Integer.parseInt(elementy[1]);
			int xR = Integer.parseInt(elementy[2]);
			int y = Integer.parseInt(elementy[3]);
			int h = Integer.parseInt(elementy[4]);
			
			ArrayList<Bonus> bonusy = new ArrayList<Bonus>();
			ArrayList<Przeszkoda> przeszkody = new ArrayList<Przeszkoda>();
			
			
			int xL_up, xR_up, yy, hh;
			
			
			while(!(wiersz = in.readLine()).equals("x"))
			{	
				System.out.println(wiersz);
				
				
				elementy = wiersz.split(" ");
				kod = Byte.parseByte(elementy[0]);
				Sciezka sciezka = null;
				
				if(kod == Plansze.wspolrzedne)
				{
					xL_up = Integer.parseInt(elementy[1]);
					xR_up = Integer.parseInt(elementy[2]);
					yy = Integer.parseInt(elementy[3]);
					hh = Integer.parseInt(elementy[4]);
					
					sciezka = new Sciezka(xL, xR, xL_up, xR_up, h, y);
					sciezka.dodajBonusy(bonusy);
					sciezka.dodajPrzeszkody(przeszkody);
					
					bonusy = new ArrayList<Bonus>();
					przeszkody = new ArrayList<Przeszkoda>();
					
					nowaDroga.add(sciezka);
					
					xL = xL_up;
					xR = xR_up;
					h = hh;
					y = yy;
				}
				else if(kod == Plansze.przeszkoda)
				{
					double x_przeszkody = Double.parseDouble(elementy[1]);
					double y_przeszkody = Double.parseDouble(elementy[2]);
					double Ax = Double.parseDouble(elementy[3]);
					double Ay = Double.parseDouble(elementy[4]);
					double alpha = Double.parseDouble(elementy[5]);
					przeszkody.add(new Przeszkoda(x_przeszkody, y_przeszkody, Ax, Ay, alpha));
					
				}
				else if(kod == Plansze.bonus)
				{
					double x_bon = Double.parseDouble(elementy[1]);
					double y_bon = Double.parseDouble(elementy[2]);
					int pkt = Integer.parseInt(elementy[3]);
					bonusy.add(new Bonus(x_bon, y_bon, pkt));
				}
			}
			
			
		}
		catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return nowaDroga;
			
	}
	
	/**
	 * Wczytanie planszy lokalnie
	 * @throws IOException
	 */
	public void wczytajPlanszeLokalnie() throws IOException
	{
		droga.clear();
		BufferedReader in = null;
		in = new BufferedReader(new FileReader(Plansze.plansze[pojazd.ktoryPoziom()]));
		droga = ZarzadzanieGra.wczytajZbufora(in);
		in.close();

	}
	
	/**
	 * Funkcja odpowiedzialna za uruchomienie gry
	 */
	public void uruchomGre()
	{
		ustawPojazdNaPoczatek();
		planszaWidokHND.repaint();
		wToku = true;
		zegar.start();
		pauza = false;
	}
	
	/**
	 * Funkcja odpowiedzialna za zatrzymanie rozgrywki w celu jej zakoñczenia
	 */
	public void zatrzymajGre()
	{
	pauza = false;
	wToku = false;
	zegar.stop();
	}
	
	/**
	 * Funkcja odpowiedzialna za tryb aktywnej pauzy
	 */
	public void pauza()
	{
		if(!wToku)
			return;
		
		if(zegar.isRunning())
		{
			pauza = true;
			zegar.stop();
		}
		else
		{
			zegar.start();
			pauza = false;
		}
			
	}
	
	/**
	 * Funkcja zmienia pozycje pojazdu
	 * @param x - wspolrzedne x pojazdu
	 * @param y - wspolrzedne y pojazdu
	 */
	public void zmienPozycjePojazdu(int x, int y)
	{
		pojazd.zmienPozycje(x, y);
	}
	
	/**
	 * Funkcja pobiera uchwyt do klasy Pojazd
	 * @return uchwyt do klasy Pojazd
	 */
	public Pojazd dostepDoPojazdu()
	{
		return pojazd;
	}
	
	/**
	 * Funkcja odpowiada za przesuniêcie planszy
	 * @param dy - wartoœæ o która przesuwana jest plansza
	 */
	public void przesunPlansze(double dy)
	{
		sumaDy += dy;
		for(Sciezka s : droga)
		{
			s.moveDown(dy);
		}

	}
	
	/**
	 * Funkcja sprawdza czy pojazd znajduje siê na koñcu drogi
	 * @return czy koniec planszy
	 */
	public boolean koncowka()
	{
		int ostatnia = droga.size() - 1;
		boolean czyMoznaPrzesunac = droga.get(ostatnia).getUp() <= Plansza.boardHeight;
		return czyMoznaPrzesunac;
	}
	
	/**
	 * Funkcja cofa plansze na pocz¹tek drogi
	 */
	public void cofnijPlansze()
	{
		for(Sciezka s : droga)
		{
			s.moveDown(-sumaDy);
		}
		sumaDy = 0;
		
		
	}
	
	/**
	 * Funkcja zwraca aktualny numer œcie¿ki
	 */
	public void numerSciezki()
	{
		for(int i = 0; i < droga.size(); ++i)
		{
			if(pojazd.getY() >= droga.get(i).getDown() && pojazd.getY() < droga.get(i).getUp())
			{
				aktualnyNumerSciezki = i;
				return;
			}
		}
		aktualnyNumerSciezki = -1;
	}
	
	/**
	 * Funkcja odpowiedzialna rysowanie przeszkód
	 * @param g2 - obiekt graficzny
	 */
	public void rysujPrzeszkody(Graphics2D g2)
	{
		for(Sciezka s : droga)
		{
			s.rysujPrzeszkody(g2);
		}

	}
	
	/**
	 * Funkcja odpowiedzialna rysowanie bonusów
	 * @param g2 - obiekt graficzny
	 */
	public void rysujBonusy(Graphics2D g2)
	{
		for(Sciezka s : droga)
		{
			s.rysujBonusy(g2);
		}
	}
	
	
	 /** Funkcja odpowiedzialna rysowanie sciezek
	 * @param g2 - obiekt graficzny
	 */
	public void rysujSciezki(Graphics2D g2)
	{
		for(Sciezka s : droga)
		{
			g2.fill(s.getShape());
		}
	}	
}
