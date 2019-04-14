import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/**
 * Klasa opiuj¹ca okno gry i rozk³ad umieszczonych na nim elementów
 * @author Jakub Stanis³aw
 * 
 */

public class MainWindow extends JFrame
{
	private static final long serialVersionUID = 1L;

	private JMenuItem stop;
	private JMenuItem start;
	private JMenuItem nowa;
	private JMenuItem najlepszeWyniki;
	
	private Plansza widokPlanszy;
	private ZarzadzanieGra zarzadzanie;
	private int marginesX;
	private int marginesY;
	
	private final JLabel liczbaPunktow;
	private final JLabel liczbaSzans;
	
	/**
	 * 
	 * @param filename - plik zawieraj¹cy parametry dotycz¹ce funkcjonalnoœci sieciowej
	 */
	public MainWindow(String filename)
	{
		widokPlanszy = new Plansza(this);
		zarzadzanie = new ZarzadzanieGra(widokPlanszy, filename);
		widokPlanszy.ustawZarzadce(zarzadzanie);
		
		this.setLayout(new BorderLayout());
		this.add(widokPlanszy, BorderLayout.CENTER);
		
		liczbaPunktow = new JLabel("Liczba punktow:");
		liczbaSzans = new JLabel("Szans:");
		
		JPanel panelPoludnie = new JPanel();
		panelPoludnie.add(liczbaSzans);
		panelPoludnie.add(zarzadzanie.etykietaWyswietlaniaSzans());
		panelPoludnie.add(liczbaPunktow);
		panelPoludnie.add(zarzadzanie.etykietaWyswietlaniaPunktow());

		this.add(panelPoludnie, BorderLayout.SOUTH);
		
		
		this.pack();
		
		marginesX = getWidth() - widokPlanszy.getWidth();
		marginesY = getHeight() - widokPlanszy.getHeight();
		
		
		JMenuBar bar = new JMenuBar();
	
		JMenu gameMenu = new JMenu("Gra");
		gameMenu.addMenuListener(new MenuListener()
		{
			public void menuCanceled(MenuEvent ev) { }
			public void menuDeselected(MenuEvent ev) { }

			@Override
			public void menuSelected(MenuEvent ev)
			{
				boolean wTrakcie = zarzadzanie.isGameRunning();
				nowa.setEnabled(!wTrakcie);
				start.setEnabled(zarzadzanie.czyUstalonyGracz() && !wTrakcie);
				stop.setEnabled(wTrakcie);
				najlepszeWyniki.setEnabled(zarzadzanie.czyUstalonyGracz() && !wTrakcie);
			}
			
		});
		
		
		
		nowa = new JMenuItem("Gracz");
		nowa.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ev)
			{
				LoginDialog dialog = new LoginDialog(MainWindow.this);
				dialog.setVisible(true);										// tu sie zatrzymuje az okno zostanie zamkniete
				if(!dialog.czyAceptacja())
					return;														// anulowano
				
				zarzadzanie.ustalGracza(dialog.pobierzLogin());
				zarzadzanie.ustalTrybZdalnie(dialog.czyOnLine());
			}
		});
		
		
		start = new JMenuItem("start");
		start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ev)
			{
				zarzadzanie.resetuj();
				zarzadzanie.wczytajPlansze();			
			}
			
		});
		
		stop = new JMenuItem("stop");
		stop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ev) {
				zarzadzanie.zatrzymajGre();
			}
			
		});
		
		najlepszeWyniki = new JMenuItem("Najlepsze wyniki");
		najlepszeWyniki.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ev)
			{
				zarzadzanie.pobierzNajlepsze(MainWindow.this);
			}
		});
		
		this.addWindowListener(new WindowAdapter(){

			@Override
			public void windowClosing(WindowEvent ev) {
					zarzadzanie.zapiszWynikiLokalnie();
			}
		});
		
		this.addKeyListener(new KeyListener()
		{

			@Override
			public void keyPressed(KeyEvent ev) {
				zarzadzanie.pauza();
			}
			@Override
			public void keyReleased(KeyEvent ev) { }

			@Override
			public void keyTyped(KeyEvent ev) { }
			
		});
		
		
		gameMenu.add(nowa);
		gameMenu.add(start);
		gameMenu.add(stop);
		gameMenu.add(najlepszeWyniki);

		bar.add(gameMenu);
		
		this.setJMenuBar(bar);
		
		pack();
		
		this.setLocationRelativeTo(null);
	}
	
	/**
	 * Ustawia rozmiar okna z uwzglêdnieniem marginesów
	 * @param width - szerokoœæ planszy
	 * @param height - wysokoœæ planszy
	 */
	public void setSizze(int width, int height)
	{
		this.setSize(width + marginesX, height + marginesY);
	}

    
	public static void main(String [] args)
	{
		
			String filename = "config.txt";
		/**
		 * Wyswietlenie okna
		 */
		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run() 
			{
				MainWindow window = new MainWindow(filename);
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				window.setVisible(true);

			}
		});		
	}

}






