import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

/**
 * Klasa zajmuj¹ca siê obs³ug¹ myszy
 * @author Jakub Stanis³aw
 *
 */
class OblugaMyszy implements MouseMotionListener, MouseListener
{
	private boolean wcisniete;
	private Plansza planszaHnd;
	private Pojazd pojazdHnd;
	private Point2D poprzedniePolozenie;
	
	
	public OblugaMyszy(Plansza plansza)
	{
		planszaHnd = plansza;
		wcisniete = false;
		poprzedniePolozenie = new Point2D.Double(0, 0);
		
	}
	
	/**
	 * Funkcja odpowiada za zresetowanie myszy
	 */
	public void reset()
	{
		wcisniete = false;
	}
	
	/**
	 * Funkcja opisuje zachowanie pojazdu kiedy kursor znajduje siê na pojezdzie i wcisniety jest lewy przycisk myszy
	 */
	public void mouseDragged(MouseEvent ev) 
	{
		if(pojazdHnd == null)					// jesli brak pojazdu
			return;
		
		if(pojazdHnd.dajDostep().czyPauza())
			return;
		
		
		if(!pojazdHnd.dajDostep().isGameRunning())
			return;
		
		if(wcisniete)
		{
			double mouseX = (double)ev.getX() / planszaHnd.skala();
			double mouseY = (double)ev.getY() / planszaHnd.skala();
			
			double dx = mouseX - poprzedniePolozenie.getX();
			double dy = mouseY - poprzedniePolozenie.getY();
			pojazdHnd.przesun(dx, dy);
			poprzedniePolozenie.setLocation(dx + poprzedniePolozenie.getX(), dy + poprzedniePolozenie.getY());
			
			ZarzadzanieGra z = planszaHnd.dostepDoZarzadcy();
			
			
			z.numerSciezki();				// ustawia numer sciezki u zarzadcy
			z.czyKolizja();
			z.sprawdzBonusy();
			
			boolean czyKoncowka = planszaHnd.dostepDoZarzadcy().koncowka();
			if(czyKoncowka)
			{
				if(pojazdHnd.getY() >= Plansza.boardHeight)
				{
					z.wczytajKolejnyPoziom();
				}
			}
			planszaHnd.repaint();
		}
		
	}

	public void mouseMoved(MouseEvent ev) { }
	public void mouseClicked(MouseEvent ev){ }
	public void mouseEntered(MouseEvent ev) { }
	public void mouseExited(MouseEvent ev) {}

	public void mousePressed(MouseEvent ev) 
	{
		pojazdHnd = planszaHnd.dostepDoZarzadcy().dostepDoPojazdu();
		
		if(pojazdHnd == null)					// jesli brak pojazdu
			return;
		
		if(pojazdHnd.dajDostep().czyPauza())
			return;
			
		if(!pojazdHnd.dajDostep().isGameRunning())
			return;
		
		double mouseX = (double)ev.getX() / planszaHnd.skala();
		double mouseY = (double)ev.getY() / planszaHnd.skala();
		
		if(pojazdHnd.czyWewnatrzPojazdu(mouseX, mouseY))
		{
			wcisniete = true;
			poprzedniePolozenie.setLocation(mouseX, mouseY);
		}
		
	}

	/**
	 * Funkcja opisuje zachowanie pojazdu gdy lewy przycisk myszy nie jest wciœniêty
	 */
	@Override
	public void mouseReleased(MouseEvent ev) 
	{
		pojazdHnd = planszaHnd.dostepDoZarzadcy().dostepDoPojazdu();
		if(pojazdHnd == null)					// jesli brak pojazdu
			return;
		
		if(pojazdHnd.dajDostep().czyPauza())
			return;
		
		
		if(!pojazdHnd.dajDostep().isGameRunning())
			return;
		
		if(wcisniete)
		{
			pojazdHnd = planszaHnd.dostepDoZarzadcy().dostepDoPojazdu();
			double mouseX = (double)ev.getX() / planszaHnd.skala();
			double mouseY = (double)ev.getY() / planszaHnd.skala();
			
			double dx = mouseX - poprzedniePolozenie.getX();
			double dy = mouseY - poprzedniePolozenie.getY();
			
			pojazdHnd.przesun(dx, dy);
			poprzedniePolozenie.setLocation(mouseX, mouseY);
			
			wcisniete = false;
			planszaHnd.repaint();
			
		}
	}
}

