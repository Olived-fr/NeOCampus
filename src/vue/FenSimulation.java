package vue;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class FenSimulation extends JFrame {
	
	JButton bAjoutCapt, bConnexion;
	JTable tListCapt;
	
	public FenSimulation(String titre) {
		super(titre);
		
		//Définition de la fenetre
		this.setBounds(new Rectangle(800, 400));
		this.setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		//Création et ajout des composant à la fenetre		
		bAjoutCapt = new JButton("Ajouter un capteur");
		add(bAjoutCapt, BorderLayout.NORTH);
		
			//Tableau d'affichage des capteurs ajoutés
		String[] nomsColonnes = { "N°",
								"Identifiant",
								"Type de localisation",
								"Localisation",
								"Type de donn�es",
								"Intervalle"};
		tListCapt = new JTable(new DefaultTableModel(nomsColonnes, 0));
		JScrollPane scroll = new JScrollPane(tListCapt);
		tListCapt.setFillsViewportHeight(true);
		add(scroll, BorderLayout.CENTER);
		
		bConnexion = new JButton("Connexion");
		add(bConnexion, BorderLayout.SOUTH);
		
		
		// Evenements
		bAjoutCapt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String id = JOptionPane.showInputDialog("Identifiant du capteur");
				
				if(id != null && id != "") {
					String[] liste = {"Interieur", "Exterieur"};
					String loc = (String) JOptionPane.showInputDialog(null, "Localisation", "Localisation", JOptionPane.DEFAULT_OPTION, null, liste, "Interieur");
					
					if(loc != null) {
						if(loc == "Interieur") {
							String batiment = JOptionPane.showInputDialog("Batiment");
							Integer etage = Integer.valueOf(JOptionPane.showInputDialog("Etage"));
							String salle = JOptionPane.showInputDialog("Salle");
							String positionRel = JOptionPane.showInputDialog("Position relative");
						} else {
							Float lat = Float.valueOf(JOptionPane.showInputDialog("Latitude (utilisez un point pour un nombre décimal)"));
							Float longi = Float.valueOf(JOptionPane.showInputDialog("Longitude (utilisez un point pour un nombre décimal)"));
						}

						Integer minIntervalle = Integer.valueOf(JOptionPane.showInputDialog("Intervalle min"));
						Integer maxIntervalle = Integer.valueOf(JOptionPane.showInputDialog("Intervalle max"));
						
						
						//TODO Ajouter champs au tableau
						
						// Peut-etre que sauvegarder les capteurs dans un fichier sera plus simple pour les récupérer dans l'autre fenetre
					}
				}
			}
		});
		
		
		bConnexion.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				bConnexion.getTopLevelAncestor().setVisible(false);
				FenSimulationSuiv fen2 = new FenSimulationSuiv("Interface de simulation");
				fen2.setVisible(true);
				
			}
		});
		
	}
}
