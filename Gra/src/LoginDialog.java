import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * Klasa odpowiedzialna za przekazanie loginu gracza oraz trybu gry(online, offline) 
 * @author Jakub Stanis³aw
 *
 */

public class LoginDialog extends JDialog
{
	private static final long serialVersionUID = 3956605409047920202L;
	private JRadioButton onlineButton;
	private JRadioButton onfflineButton;
	
	private JButton okButton;
	private JButton anulujButton;
	
	private JLabel loginLabel;
	private JLabel trybLabel;
	private JTextField textLogin;
	
	private boolean accept;	
	
	/**
	 * 
	 * @param mainWin - okno aplikacji
	 */
	public LoginDialog(MainWindow mainWin)
	{
		super(mainWin, true);					
		this.setTitle("Wybor loginu i trybu");
		
		loginLabel = new JLabel("Twoj login:");
		textLogin = new JTextField(20);
		JPanel northPanel = new JPanel();
		northPanel.add(loginLabel);
		northPanel.add(textLogin);
		
		trybLabel = new JLabel("Tryb:");
		onlineButton = new JRadioButton("On-line");
		onfflineButton = new JRadioButton("Off-line");
		onlineButton.setSelected(true);
		onfflineButton.setSelected(false);
		
		ButtonGroup group = new ButtonGroup();
		group.add(onlineButton);
		group.add(onfflineButton);
		
		
		JPanel centerPanel = new JPanel();
		centerPanel.add(trybLabel);
		centerPanel.add(onlineButton);
		centerPanel.add(onfflineButton);
		
		okButton = new JButton("Wprowadz");
		anulujButton = new JButton("Anuluj");
		JPanel southPanel = new JPanel();
		southPanel.add(okButton);
		southPanel.add(anulujButton);
		
		this.setLayout(new BorderLayout());
		this.add(northPanel, BorderLayout.NORTH);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(southPanel, BorderLayout.SOUTH);
		
		accept = false;
		
		textLogin.addKeyListener(new KeyAdapter()
		{

			@Override
			public void keyReleased(KeyEvent ev)
			{
				okButton.setEnabled(!textLogin.getText().isEmpty());
				
			}
			
		});
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				accept = false;
				LoginDialog.this.setVisible(false);
			}
		});
		
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ev) {
				accept = true;	
				LoginDialog.this.setVisible(false);
			}
		});
		
		anulujButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ev) {
				accept = false;
				LoginDialog.this.setVisible(false);
				
			}
		});
		
		okButton.setEnabled(!textLogin.getText().isEmpty());
		
		
		this.setLocationRelativeTo(null);
		this.pack();
	}
	
	/**
	 *Funkcja sprawdza czy opcje w oknie dialogowym zostaly zakceptowane
	 * @return czy akceptacja
	 */
	public boolean czyAceptacja()
	{
		return accept;
	}
	
	/**
	 * Sprawdza czy preferowany tryb to online
	 * @return czy online
	 */
	public boolean czyOnLine()
	{
		return onlineButton.isSelected();
	}
	
	/**
	 * Funkcja pobiera login z okna dialogowego
	 * @return login
	 */
	public String pobierzLogin()
	{
		return textLogin.getText();
	}
	
	
}
