package vue;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import capteurs.Capteurs;
import capteurs.CoordGps;
import capteurs.CoordInterieur;
import capteurs.EnumType;

public class FenSimulation extends JFrame {
	
	JButton bAjoutCapt, bConnexion;
	JTable tListCapt;
	DefaultTableModel dtm;
	JFrame laFen;
	
	List<Capteurs> listeCapteurs = new ArrayList<>();
	int numCapt = 1;
	
	public FenSimulation(String titre) {
		super(titre);
		
		laFen = this;
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
								"Type de donnees",
								"Intervalle"};
		dtm = new DefaultTableModel(nomsColonnes, 0);
		tListCapt = new JTable(dtm);
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
						String localisation;
						CoordGps gps = null;
						CoordInterieur inte = null;
						if(loc == "Interieur") {
							String batiment = JOptionPane.showInputDialog("Batiment");
							Integer etage = Integer.valueOf(JOptionPane.showInputDialog("Etage"));
							String salle = JOptionPane.showInputDialog("Salle");
							String positionRel = JOptionPane.showInputDialog("Position relative");
							
							localisation = "<'" + batiment + "', " + etage + ", '" + salle + "', '" + positionRel + "'>";
							inte = new CoordInterieur(batiment, etage.toString(), salle, positionRel);
						} else {
							Float lat = Float.valueOf(JOptionPane.showInputDialog("Latitude (utilisez un point pour un nombre décimal)"));
							Float longi = Float.valueOf(JOptionPane.showInputDialog("Longitude (utilisez un point pour un nombre décimal)"));
							
							localisation = "(" + lat + ", " + longi + ")";
							gps = new CoordGps(lat, longi);
						}
						
						EnumType enu = EnumType.EAUCHAUDE;
						Object[] typesDonnees = enu.getDeclaringClass().getEnumConstants();
						EnumType type = (EnumType) JOptionPane.showInputDialog(null, "Types de donnees", "Choisissez le type", JOptionPane.DEFAULT_OPTION, null, typesDonnees, "temperature");

						Integer minIntervalle = Integer.valueOf(JOptionPane.showInputDialog("Intervalle min"));
						Integer maxIntervalle = Integer.valueOf(JOptionPane.showInputDialog("Intervalle max"));
						String intervalle = "[" + minIntervalle + " - " + maxIntervalle + "]";
						
						//
						listeCapteurs.add(new Capteurs(id, type, loc, minIntervalle, maxIntervalle, gps, inte));
						
						//Ajout des champs au tableau
						dtm.addRow(new Object[] {numCapt, id, loc, localisation, type, intervalle});
						numCapt++;
					}
				}
			}
		});
		
		
		bConnexion.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				laFen.setVisible(false);
				FenSimulationSuiv fen2 = new FenSimulationSuiv("Interface de simulation", laFen, listeCapteurs);
				fen2.setVisible(true);
				
			}
		});
		
	}
}
