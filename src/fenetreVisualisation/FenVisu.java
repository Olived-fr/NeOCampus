
package fenetreVisualisation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import capteurs.Capteurs;
import capteurs.CoordGps;
import capteurs.CoordInterieur;
import capteurs.EnumType;
import reseau.Reseau;

//import static sun.jvm.hotspot.runtime.PerfMemory.start;

public class FenVisu extends JFrame {
	
	private JPanel pTop, pMid, pMid1, pMid2, pBot, pBot1, pBot2;
	private JButton bChargerCapt, bConnexion, bDeconnexion, bValiderContrainte, bSuppContrainte;
	private JComboBox<EnumType> cbContrainteTypeMesure;
	private JComboBox<String> cbFiltreTypeMesure;
	private JList<String> listContraintes;
	private DefaultListModel<String> dlm;
	private JTextField tIntervalleMin, tIntervalleMax;
	private JTable tListCapt;
	private DefaultTableModel dtm;
	private JTree tree;
	
	public static Reseau reseau = new Reseau();
	public static Date date = new Date();
	public static Fichier fichier = new Fichier();
	public static Runnable tache = new TacheThread();
	public static Thread thread = new Thread(tache);

	private List<Capteurs> captExt = new ArrayList<>();
	private List<Capteurs> captInt = new ArrayList<>();
	
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
		// arbre
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Capteurs");
		DefaultTreeModel treeModel = new DefaultTreeModel(root);
		tree = new JTree(treeModel);
		
		tree.setEditable(true);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setShowsRootHandles(true);
		
		JScrollPane treeScroll = new JScrollPane(tree);
		treeScroll.setPreferredSize(new Dimension(400, 170));
		
		//---------------------
		creerListesCapteursDepuisFichier();
		construireArbre();
		//--------------------
		
		pMid1.add(treeScroll);
		
		
		String[] types = EnumType.noms();
		cbFiltreTypeMesure = new JComboBox<>(types);
		cbFiltreTypeMesure.insertItemAt("--", 0);
		cbFiltreTypeMesure.setSelectedIndex(0);
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
		EnumType enu = EnumType.EAUCHAUDE;
		EnumType[] typesMesures = enu.getDeclaringClass().getEnumConstants();
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
		
		dlm = new DefaultListModel<>();
		listContraintes = new JList<>(dlm);
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
		
		bValiderContrainte.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				EnumType type = (EnumType) cbContrainteTypeMesure.getSelectedItem();
				float intMin = Float.valueOf(tIntervalleMin.getText());
				float intMax = Float.valueOf(tIntervalleMax.getText());
				
				dlm.addElement(type.toString() + "  |  [" + intMin + "-" + intMax + "]");
			}
		});
		
		bSuppContrainte.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = listContraintes.getSelectedIndex();
				
				if(index != -1) {
					dlm.remove(index);
				}
			}
		});
		
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node =  (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				
				if(node == null)
					return;
				
				if(dtm.getRowCount() > 0) {
					for (int i = dtm.getRowCount()-1; i > -1; i--) {
						dtm.removeRow(i);
					}
				}
				
				String nodeNom = node.getUserObject().toString();
				TreeNode[] nodes = node.getPath();
				if(nodeNom.equalsIgnoreCase("capteurs")) {
					for (Capteurs capteurs : captExt) {
						dtm.addRow(new Object[] {capteurs.getId(), capteurs.getType(), capteurs.getGps()});
					}
					
					for (Capteurs capteurs : captInt) {
						dtm.addRow(new Object[] {capteurs.getId(), capteurs.getType(), capteurs.getInterieur()});
					}
				} else if(nodes[1].toString().equalsIgnoreCase("exterieur")) {
					boolean captSelect = nodeNom.equalsIgnoreCase(nodes[1].toString());
					for (Capteurs capteurs : captExt) {
						if(captSelect || capteurs.getId().equalsIgnoreCase(nodeNom)) {
							dtm.addRow(new Object[] {capteurs.getId(), capteurs.getType(), capteurs.getGps()});
						}
							
					}
				} else if(nodes[1].toString().equalsIgnoreCase("interieur")) {
					int profondeur = nodes.length;
					
					for (Capteurs capteurs : captInt) {
						CoordInterieur interieur = capteurs.getInterieur();
						if(profondeur == 2 || (profondeur == 3 && interieur.getBatiment().equalsIgnoreCase(nodeNom))
								|| (profondeur == 4 && interieur.getBatiment().equalsIgnoreCase(nodes[2].toString()) 
													&& interieur.getEtage().equalsIgnoreCase(nodeNom))
								|| (profondeur == 5 && interieur.getBatiment().equalsIgnoreCase(nodes[2].toString())
													&& interieur.getEtage().equalsIgnoreCase(nodes[3].toString())
													&& interieur.getSalle().equalsIgnoreCase(nodeNom))
								|| (profondeur == 6 && interieur.getBatiment().equalsIgnoreCase(nodes[2].toString())
													&& interieur.getEtage().equalsIgnoreCase(nodes[3].toString())
													&& interieur.getSalle().equalsIgnoreCase(nodes[4].toString())
													&& capteurs.getId().equalsIgnoreCase(nodeNom))) {
							dtm.addRow(new Object[] {capteurs.getId(), capteurs.getType(), capteurs.getInterieur()});
						}
						
					}
				}
				
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
		System.out.println("chaineCapteur="+chaineCapteur);
		if (!(chaineCapteur.equals("ConnexionOK") || chaineCapteur.equals("ConnexionKO") || chaineCapteur.equals("DeconnexionOK") || chaineCapteur.equals("DeconnexionKO") || chaineCapteur.equals("InscriptionCapteurOK"))) {
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
					reseau.inscriptionCapteur(name);
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
		System.out.println("fin");
	}
	
	private void construireArbre() {
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		
		if(captExt.size() != 0) {
			DefaultMutableTreeNode exte = ajouterNode(root, "Exterieur", model);
			
			//DefaultMutableTreeNode exte = trouverNode(root, "Exterieur");
			for (Capteurs capteurs : captExt) {
				ajouterNode(exte, capteurs.getId(), model);
			}
		}
		
		if(captInt.size() != 0) {
			DefaultMutableTreeNode inte = ajouterNode(root, "Interieur", model);
			
			for (Capteurs capteurs : captInt) {
				String batimentString = capteurs.getInterieur().getBatiment();
				String etageString = capteurs.getInterieur().getEtage();
				String salleString = capteurs.getInterieur().getSalle();
				
				DefaultMutableTreeNode batiment = trouverNode(inte, batimentString);
				if(batiment == null) {
					batiment = ajouterNode(inte, batimentString, model);
				}
				
				DefaultMutableTreeNode etage = trouverNode(batiment, etageString);
				if(etage == null) {
					etage = ajouterNode(batiment, etageString, model);
				}
				
				DefaultMutableTreeNode salle = trouverNode(etage, salleString);
				if(salle == null) {
					salle = ajouterNode(etage, salleString, model);
				}
				
				ajouterNode(salle, capteurs.getId(), model);
			}
		}
	}
	
	private DefaultMutableTreeNode trouverNode(DefaultMutableTreeNode root, String nom) {
		@SuppressWarnings("unchecked")
		Enumeration<DefaultMutableTreeNode> e = root.depthFirstEnumeration();
		while(e.hasMoreElements()) {
			DefaultMutableTreeNode node = e.nextElement();
			if(node.toString().equalsIgnoreCase(nom)) {
				return node;
			}
		}
		return null;
	}
	
	private DefaultMutableTreeNode ajouterNode(DefaultMutableTreeNode parent, String nouvelleNode, DefaultTreeModel model) {
		DefaultMutableTreeNode enfant = new DefaultMutableTreeNode(nouvelleNode);
		model.insertNodeInto(enfant, parent, parent.getChildCount());
		//tree.scrollPathToVisible(new TreePath(enfant.getPath()));
		
		return enfant;
	}
	
	private void creerListesCapteursDepuisFichier() {
		File dossier = new File(".");
		File[] listeFichiers = dossier.listFiles();
		
		for (int i = 0; i < listeFichiers.length; i++) {
			File fichier = listeFichiers[i];
			if(fichier.isFile() && fichier.getName().endsWith(".txt")) {
				FileInputStream fis = null;
				BufferedReader reader = null;
				
				try {
					fis = new FileInputStream(fichier);
					reader = new BufferedReader(new InputStreamReader(fis));
					
					String ligne = reader.readLine().replace("/", "");
					String[] infos = ligne.split(";");
					
					if(infos.length == 3) {
						captExt.add(new Capteurs(fichier.getName().replace(".txt", ""), EnumType.valueOf(infos[0]), "Exterieur", .0f, .0f, new CoordGps(new Float(infos[1]), new Float(infos[2])), null, false, .0f));
					} else if(infos.length == 5) {
						captInt.add(new Capteurs(fichier.getName().replace(".txt", ""), EnumType.valueOf(infos[0]), "Interieur", .0f, .0f, null, new CoordInterieur(infos[1], infos[2], infos[3], infos[4]), false, .0f));
					}
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						reader.close();
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
					
			}
		}
	}
}
