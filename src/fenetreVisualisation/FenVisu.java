
package fenetreVisualisation;

import reseau.Reseau;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import static sun.jvm.hotspot.runtime.PerfMemory.start;

public class FenVisu extends JFrame {
	
	JPanel pTop, pMid, pMid1, pMid2, pBot, pBot1, pBot2;
	JButton bChargerCapt, bConnexion, bDeconnexion, bValiderContrainte, bSuppContrainte;
	JComboBox<String> cbFiltreTypeMesure, cbContrainteTypeMesure;
	JList<String> listContraintes;
	JTextField tIntervalleMin, tIntervalleMax;
	JTable tListCapt;
	DefaultTableModel dtm;
	public static Reseau reseau = new Reseau();
	public static Date date = new Date();
	public static Fichier fichier = new Fichier();
	public static Runnable tache = new TacheThread();
	public static Thread thread = new Thread(tache);
	
	public FenVisu(String titre) {
		super(titre);
		
		this.setBounds(new Rectangle(700, 600));
		this.setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// TOP
		bChargerCapt = new JButton("Charger capteur");
		bConnexion = new JButton("Connexion serveur");
		bDeconnexion = new JButton("Deconnexion serveur");
		bDeconnexion.setEnabled(false);
		
		pTop = new JPanel(new GridLayout(1, 3, 1, 1));
		pTop.add(bChargerCapt);
		pTop.add(bConnexion);
		pTop.add(bDeconnexion);
		add(pTop, BorderLayout.NORTH);
		
		// MID
		pMid1 = new JPanel();
		pMid1.add(new JLabel("JTree"));
		
		
		String[] typesMesures = {"A", "Modifier", "Modifier", "Modifier", "Modifier", "Modifier", "Modifier", "Modifier", "Modifier", "Modifier", "Modifier"};
		cbFiltreTypeMesure = new JComboBox<>(typesMesures);
		String[] nomsColonnes = {"Identifiant",
								"Type de mesure",
								"Localisation",
								"Valeur"};
		dtm = new DefaultTableModel(nomsColonnes, 0);
		tListCapt = new JTable(dtm);
		JScrollPane tableScroll = new JScrollPane(tListCapt);
		tableScroll.setPreferredSize(new Dimension(580, 150));
		
		tListCapt.setFillsViewportHeight(true);
		
		pMid2 = new JPanel();
		pMid2.add(new JLabel("Filtre :"));
		pMid2.add(cbFiltreTypeMesure);
		pMid2.add(tableScroll);
		
		pMid = new JPanel(new GridLayout(2, 1));
		pMid.add(pMid1);
		pMid.add(pMid2);
		add(pMid, BorderLayout.CENTER);
		
		
		// BOTTOM
		cbContrainteTypeMesure = new JComboBox<>(typesMesures);
		tIntervalleMin = new JTextField(4);
		tIntervalleMax = new JTextField(4);
		bValiderContrainte = new JButton("Valider");
		
		pBot1 = new JPanel();
		pBot1.add(new JLabel("Contraintes :"));
		pBot1.add(cbContrainteTypeMesure);
		pBot1.add(new JLabel("Intervalle :"));
		pBot1.add(tIntervalleMin);
		pBot1.add(new JLabel("-"));
		pBot1.add(tIntervalleMax);
		pBot1.add(bValiderContrainte);
		
		
		listContraintes = new JList<>(typesMesures); // A modifier
		listContraintes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane listScroll = new JScrollPane(listContraintes);
		listScroll.setPreferredSize(new Dimension(250, 70));
		bSuppContrainte = new JButton("Supprimer contrainte");
		
		pBot2 = new JPanel();
		pBot2.add(new JLabel("Liste Contraintes :"));
		pBot2.add(listScroll);
		pBot2.add(bSuppContrainte);
		
		pBot = new JPanel(new GridLayout(2, 0));
		pBot.add(pBot1);
		pBot.add(pBot2);
		add(pBot, BorderLayout.SOUTH);
		
		
		
		// Evenements
		bConnexion.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				bDeconnexion.setEnabled(true);
				bConnexion.setEnabled(false);
				reseau.connexionVisu();
				if (thread.getState() == Thread.State.TERMINATED)
					thread = new Thread(tache);
				thread.start();
			}
		});
		
		bDeconnexion.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				bConnexion.setEnabled(true);
				bDeconnexion.setEnabled(false);
				thread.stop();
				reseau.deconnexionVisu();
			}
		});

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				thread.stop();
				reseau.deconnexionVisu();
				e.getWindow().dispose();
			}
		});
		
	}

	public static void traitement(String chaineCapteur) {
		System.out.println(chaineCapteur);
		if (!(chaineCapteur.equals("ConnexionOK") || chaineCapteur.equals("ConnexionKO") || chaineCapteur.equals("DeconnexionOK") || chaineCapteur.equals("DeconnexionKO"))) {
			StringTokenizer Tok = new StringTokenizer(chaineCapteur, ";");
			String type = (String) Tok.nextElement();
			String name = (String) Tok.nextElement();

			switch (type) {

				case "CapteurPresent":
					fichier.nouveauFichier(chaineCapteur);
					String infos = "//";
					while (Tok.hasMoreElements()) {
						infos = infos + (String) Tok.nextElement() + ";";
					}
					fichier.ajoutChaine(name, infos);
					break;

				case "InscriptionCapteurKO":
				case "DesinscriptionCapteurKO":
				case "CapteurDeco":
					break;

				case "ValeurCapteur":
					String val = "--";
					while (Tok.hasMoreElements()) {
						val = val + (String) Tok.nextElement() + ";";
					}
					val = val + date + ";";
					fichier.ajoutChaine(name, val);
					break;

				default:
					break;
			}
		}
	}
}
