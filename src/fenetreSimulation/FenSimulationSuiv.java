package fenetreSimulation;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import capteurs.Capteurs;

import static java.awt.SystemColor.text;


public class FenSimulationSuiv extends JFrame {
	
	JTable tListe;
	DefaultTableModel dtm;
	JLabel lFreq;
	JPanel panelMain, panelCenter, panelBot;
	JTextField tFrequence;
	JButton bDeconnexion;
	JButton tFrequenceButton ;
	Integer champFrequence = 0;
	JFrame laFen, fenPrec;
	Timer timer = null;

	public FenSimulationSuiv(String titre, JFrame prec, List<Capteurs> listeCapteurs) {
		super(titre);
		
		laFen = this;
		fenPrec = prec;
		// Definition de la fenetre
		this.setBounds(new Rectangle(800, 400));
		this.setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		// Création et ajout des composants à la fenetre
		panelMain = new JPanel(new GridLayout(2,0,2,2));
		
		String[] nomsColonnes = {"Identifiant",
								"Valeur mesuree",
								"Type de donnees"};
		dtm = new DefaultTableModel(nomsColonnes, 0);
		for(Capteurs capt : listeCapteurs) {
			// TODO Générer valeur aléatoire pour chaque capteur compris dans leur intervalle
			
			float valGeneree = capt.getValeur();
			
			//Ajout ligne au tableau
			dtm.addRow(new Object[] {capt.getId(), valGeneree, capt.getType()});
		}
		
		tListe = new JTable(dtm);
		JScrollPane scroll = new JScrollPane(tListe);
		tListe.setFillsViewportHeight(true);
		panelMain.add(scroll);
		
		
		panelCenter = new JPanel();
		
		lFreq = new JLabel("Frequence d'envoi (s)");
		panelCenter.add(lFreq, BorderLayout.EAST);
		
		tFrequence = new JTextField(5);
				
		panelCenter.add(tFrequence, BorderLayout.CENTER);
		
		
		tFrequenceButton = new JButton("Appliquer");
		panelCenter.add(tFrequenceButton, BorderLayout.CENTER);
		
		panelMain.add(panelCenter);
		
		add(panelMain, BorderLayout.CENTER);
		
		bDeconnexion = new JButton("Deconnexion");
		add(bDeconnexion, BorderLayout.SOUTH);
		
		
		// Evenement
		bDeconnexion.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for(Capteurs capt : listeCapteurs) {
					capt.deconnexionCapteur();
				}
				if (timer != null && timer.isRunning())
					timer.stop();
				laFen.dispose();
				fenPrec.setVisible(true);
			}
		});
		
		tFrequenceButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					if (timer != null && timer.isRunning())
						timer.stop();
					String text = tFrequence.getText();
					champFrequence = Integer.parseInt(text) ;
					ActionListener action = new ActionListener() {
					      public void actionPerformed(ActionEvent evt) {
					          for(Capteurs cpt : listeCapteurs) {
								  if (!cpt.getReseau().getSocket().isClosed()) {
									  if (!cpt.isSaisieValeur()) {
										  Random rand = new Random();
										  float val = rand.nextFloat() * (cpt.getIntervalleMax() - cpt.getIntervalleMin()) + cpt.getIntervalleMin();
										  val = Math.round(val*(float)100.0) / (float)100.0;
										  cpt.setValeur(val);
										  dtm.setValueAt(cpt.getValeur(), listeCapteurs.indexOf(cpt), 1);
									  }
									  if (text != "")
									  cpt.transmissionValeur();
								  }
							  }

					      }
					  };
					  //atention timer en miliseconde
					timer = new Timer((champFrequence*1000), action);
					timer.start();
				}
				catch(NumberFormatException E){
					
				}
			}
		});
		
		 
		
		  

	
		
		
	}
}
