
package fenetreVisualisation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.jfree.ui.RefineryUtilities;

import capteurs.Capteurs;
import capteurs.CoordGps;
import capteurs.CoordInterieur;
import capteurs.EnumType;
import reseau.Reseau;


public class FenVisu extends JFrame {
	
	private JPanel pTop, pMid, pMid1, pMid2, pBot, pBot1, pBot2;
	private JButton bChargerCapt, bConnexion, bDeconnexion, bValiderContrainte, bSuppContrainte, bRechargerArbre, bGraphe;
	private JComboBox<EnumType> cbContrainteTypeMesure;
	private JComboBox<String> cbFiltreTypeMesure;
	private JList<String> listContraintes;
	private DefaultListModel<String> dlm;
	private JTextField tIntervalleMin, tIntervalleMax;
	private JTable tListCapt;
	private static DefaultTableModel dtm;
	private JTree tree;
	private final JFileChooser fc = new JFileChooser();
	private int indexLigneSelect ;
	
	public static Reseau reseau;
	public static Fichier fichier = new Fichier();
	public static Runnable tache = new TacheThread();
	public static Thread thread = new Thread(tache);

	private List<Capteurs> captExt = new ArrayList<>();
	private List<Capteurs> captInt = new ArrayList<>();
	private static Map<EnumType, float[]> mapContraintes = new HashMap<>();

	private static ImageIcon iconWarning = new ImageIcon("warning.png");
	
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

		bRechargerArbre = new JButton("Recharger");
		bRechargerArbre.setEnabled(false);
		
		pMid1.add(treeScroll);
		pMid1.add(bRechargerArbre);
		
		bGraphe = new JButton("Graphique");
		String[] types = EnumType.noms();
		cbFiltreTypeMesure = new JComboBox<>(types);
		cbFiltreTypeMesure.insertItemAt("--", 0);
		cbFiltreTypeMesure.setSelectedIndex(0);
		String[] nomsColonnes = {"Identifiant",
								"Type de mesure",
								"Localisation",
								"Valeur",
								""};
		dtm = new DefaultTableModel(nomsColonnes, 0) {
			@Override
			public Class<?> getColumnClass(int column) {
				switch (column) {
					case 1: return EnumType.class;
					case 4: return ImageIcon.class;
					default: return String.class;
				}
			}
		};
		tListCapt = new JTable(dtm);
		tListCapt.getColumnModel().getColumn(4).setMaxWidth(30);
		tListCapt.getColumnModel().getColumn(4).setMinWidth(30);
		JScrollPane tableScroll = new JScrollPane(tListCapt);
		tableScroll.setPreferredSize(new Dimension(580, 150));
		
		tListCapt.setFillsViewportHeight(true);
		
		pMid2 = new JPanel();
		pMid2.add(new JLabel("Filtre :"));
		pMid2.add(cbFiltreTypeMesure);
		pMid2.add(bGraphe);
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
		bChargerCapt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int valRetour = fc.showOpenDialog(FenVisu.this);

				if(valRetour == JFileChooser.APPROVE_OPTION) {
					File fichier = fc.getSelectedFile();
					creerListesCapteursDepuisFichier(false, fichier);
					construireArbre();
				}
			}
		});
		
		bGraphe.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				indexLigneSelect = tListCapt.getSelectedRow();
				if(indexLigneSelect == -1){
					JOptionPane.showMessageDialog(null, "Veuillez selectionner une ligne afin d'afficher le graphe correspondant", "Attention", JOptionPane.WARNING_MESSAGE);
				}
				else {
					
					String idCapteur =tListCapt.getValueAt(indexLigneSelect, 0).toString() ;
					
					File folder = new File("./");
					List<File> files = Arrays.asList(folder.listFiles());
			        Iterator<File> fileIterator = files.iterator();
			        //System.out.println("fichiers : " + files.toString());
			        boolean find = false;
			        String filename = ""; 
			        while(fileIterator.hasNext() &&  !find){
			        	File tmp = fileIterator.next();
			        	if(!tmp.isDirectory()){
				        	filename = tmp.getName();
				        	System.out.println("filename : " + filename);
				        	if(filename.equals(idCapteur+".txt"))
				        		find = true ; 
			        	}
			        	
			        }
			        if(find){
			        	System.out.println("finded ! ");
			        	List<ValeursTempsCapteurs> liste =  new ArrayList();
			        	
			        		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

			                try
			                {
			                BufferedReader br = new BufferedReader(new FileReader(filename));
			                String line=br.readLine();
			                //line = br.readLine();
			                StringTokenizer st=new StringTokenizer(line, ";");
			                //StringTokenizer space=new StringTokenizer(line, " ");
			                int counter = 0,heure=0, minutes=0,secondes=0 ;
			                float valeur= 0.0F ;
			                Date date = null;
			                String tmp ;
			                while ((line = br.readLine()) != null) {
			                	counter = 0 ;
			                st=new StringTokenizer(line, ";");
			                
			                while(st.hasMoreTokens()){
			                	if(counter == 0){
			                		tmp = st.nextToken(); 
			                	if(tmp.length()==6)
			                		valeur = Float.parseFloat(tmp.substring(2, 6));
			                	else if(tmp.length()==7)
			                		valeur = Float.parseFloat(tmp.substring(2, 7));
			                	else if(tmp.length()==5)
			                		valeur = Float.parseFloat(tmp.substring(2, 5));
			                	}else if(counter ==1){
			                		tmp = st.nextToken();
			                		//System.out.println(tmp.substring(8, 10) +"-"+tmp.substring(5, 7)+"-"+tmp.substring(0, 4));
			                		date = sdf.parse(tmp.substring(8, 10) +"-"+tmp.substring(5, 7)+"-"+tmp.substring(0, 4));
			                		heure = Integer.parseInt(tmp.substring(11, 13));
			                		minutes = Integer.parseInt(tmp.substring(14, 16));
			                		secondes = Integer.parseInt(tmp.substring(17, 19));
			                	}
			                	
			                	counter ++; 
			                } 
			               // System.out.println("valeur : " + valeur + " date : " + date.toString() + " heure : "+ heure + " minutes : " +minutes);
			                liste.add(new ValeursTempsCapteurs(valeur, date, heure, minutes, secondes)) ;
			                //line = br.readLine(); // ATTTENTION APPAREMMENT BESOIN DE 2 FOIS POUR PASSER A LIGNE SUIVANTE
			                }
			                br.close();
			                }
			                catch (Exception et){
			                et.printStackTrace();
			                }
			        	
			        	CreationGraphique graphe = new CreationGraphique("Données du capteur sélectionné par rapport à la date/heure", " Données Capteur " + idCapteur, idCapteur, liste);
			        	 
			        	graphe.pack( );
			            RefineryUtilities.centerFrameOnScreen( graphe );          
			            graphe.setVisible( true ); 
			            graphe.addWindowListener(new WindowAdapter(){
			            	public void windowClosing(){
			            		System.out.println("ON ARRIVE LA OU PAS ? ");
			            		graphe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			            	}
			            });
			            System.out.println("NORMALEMENT C BON");
			        	
			        }else {
			        	JOptionPane.showMessageDialog(null, "Pas de fichier correspondant à ce capteur", "Information", JOptionPane.INFORMATION_MESSAGE);
			        }
				}
			
			
			
			}
		});
		
		

		bConnexion.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (InetAddress.getByName("127.0.0.1").isReachable(3000)) {
						bDeconnexion.setEnabled(true);
						bRechargerArbre.setEnabled(true);
						bConnexion.setEnabled(false);
						bChargerCapt.setEnabled(false);
						reseau = new Reseau();
						if (reseau.getSocket() != null) {
							reseau.connexionVisu();
							if (thread.getState() == Thread.State.TERMINATED)
								thread = new Thread(tache);
							thread.start();

							creerListesCapteursDepuisFichier(true, null);
							construireArbre();
						}
					}
					else {
						JOptionPane.showMessageDialog(new JFrame(), "Aucun serveur connectÃ©", "Erreur serveur", JOptionPane.ERROR_MESSAGE);
					}
				}catch (UnknownHostException ex) {
					ex.printStackTrace();
				} catch (IOException ex) {
					ex.printStackTrace();
				}

			}
		});
		
		bDeconnexion.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				bConnexion.setEnabled(true);
				bDeconnexion.setEnabled(false);
				bRechargerArbre.setEnabled(false);
				bChargerCapt.setEnabled(true);
				thread.stop();
				reseau.deconnexionVisu();
			}
		});
		
		bValiderContrainte.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				float[] intervalle = new float[2];
				EnumType type = (EnumType) cbContrainteTypeMesure.getSelectedItem();
				float intMin = Float.valueOf(tIntervalleMin.getText());
				float intMax = Float.valueOf(tIntervalleMax.getText());

				if(!mapContraintes.containsKey(type)) {
					intervalle[0] = intMin;
					intervalle[1] = intMax;
					mapContraintes.put(type, intervalle);

					dlm.addElement(type.toString() + "  |  [" + intMin + "-" + intMax + "]");
				}

			}
		});
		
		bSuppContrainte.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = listContraintes.getSelectedIndex();
				
				if(index != -1) {
					String contrainte = dlm.getElementAt(index);
					contrainte = contrainte.replace(" ","");
					StringTokenizer tok = new StringTokenizer(contrainte,"|");
					String typeStr = (String)tok.nextElement();
					mapContraintes.remove(EnumType.valueOf(typeStr));
					dlm.remove(index);
				}
			}
		});
		
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				remplirTableau();
			}
		});

		cbFiltreTypeMesure.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					remplirTableau();

				}
			}
		});

		bRechargerArbre.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				creerListesCapteursDepuisFichier(true, null);
				construireArbre();
			}
		});

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				thread.stop();
				if (reseau != null && reseau.getSocket() != null)
					reseau.deconnexionVisu();
				e.getWindow().dispose();
			}
		});
		
	}
	

	public static void traitement(String chaineCapteur) {
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
					String timeStamp = new SimpleDateFormat("yyyy MM dd HH mm ss").format(Calendar.getInstance().getTime());
					val = val + timeStamp + ";";
					fichier.ajoutChaine(name, val);
					FenVisu.modifierValeurCapteur(name, val);
					break;

				default:
					break;
			}
		}
	}
	
	private void construireArbre() {
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		root.removeAllChildren();
		model.reload();
		
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
	
	private void creerListesCapteursDepuisFichier(boolean connecte, File fic) {
		File dossier;
		File[] listeFichiers;

		if(fic == null) {
			dossier = new File(".");
			listeFichiers = dossier.listFiles();
		} else {
			File[] tmp = new File[1];
			tmp[0] = fic;
			listeFichiers = tmp;
		}

		if(connecte) {
			captExt.clear();
			captInt.clear();
		}

		
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

	private void remplirTableau() {
		DefaultMutableTreeNode node =  (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

		if(node == null)
			return;

		if(dtm.getRowCount() > 0) {
			for (int i = dtm.getRowCount()-1; i > -1; i--) {
				dtm.removeRow(i);
			}
		}

		String filtre = (String) cbFiltreTypeMesure.getSelectedItem();
		boolean filtrer = !filtre.equals("--");

		String nodeNom = node.getUserObject().toString();
		TreeNode[] nodes = node.getPath();
		if(nodeNom.equalsIgnoreCase("capteurs")) {
			for (Capteurs capteurs : captExt) {
				if(!filtrer || capteurs.getType().toString().equals(filtre))
					dtm.addRow(new Object[] {capteurs.getId(), capteurs.getType(), capteurs.getGps()});
			}

			for (Capteurs capteurs : captInt) {
				if(!filtrer || capteurs.getType().toString().equals(filtre))
					dtm.addRow(new Object[] {capteurs.getId(), capteurs.getType(), capteurs.getInterieur()});
			}
		} else if(nodes[1].toString().equalsIgnoreCase("exterieur")) {
			boolean captSelect = nodeNom.equalsIgnoreCase(nodes[1].toString());
			for (Capteurs capteurs : captExt) {
				if(captSelect || capteurs.getId().equalsIgnoreCase(nodeNom)
						&& (!filtrer || capteurs.getType().toString().equals(filtre))) {
					dtm.addRow(new Object[] {capteurs.getId(), capteurs.getType(), capteurs.getGps()});
				}

			}
		} else if(nodes[1].toString().equalsIgnoreCase("interieur")) {
			int profondeur = nodes.length;

			for (Capteurs capteurs : captInt) {
				CoordInterieur interieur = capteurs.getInterieur();
				if((profondeur == 2 || (profondeur == 3 && interieur.getBatiment().equalsIgnoreCase(nodeNom))
						|| (profondeur == 4 && interieur.getBatiment().equalsIgnoreCase(nodes[2].toString())
						&& interieur.getEtage().equalsIgnoreCase(nodeNom))
						|| (profondeur == 5 && interieur.getBatiment().equalsIgnoreCase(nodes[2].toString())
						&& interieur.getEtage().equalsIgnoreCase(nodes[3].toString())
						&& interieur.getSalle().equalsIgnoreCase(nodeNom))
						|| (profondeur == 6 && interieur.getBatiment().equalsIgnoreCase(nodes[2].toString())
						&& interieur.getEtage().equalsIgnoreCase(nodes[3].toString())
						&& interieur.getSalle().equalsIgnoreCase(nodes[4].toString())
						&& capteurs.getId().equalsIgnoreCase(nodeNom)))
						&& (!filtrer || capteurs.getType().toString().equals(filtre))) {
					dtm.addRow(new Object[] {capteurs.getId(), capteurs.getType(), capteurs.getInterieur()});
				}

			}
		}
	}

	private static void modifierValeurCapteur(String nomCapteur, String infos) {
		int index;

		infos = infos.replace("--","");
		String valeur = infos.split(";")[0];
		for (int i = dtm.getRowCount()-1; i >= 0; i--) {
			if(dtm.getValueAt(i,0).equals(nomCapteur)) {
				dtm.setValueAt(valeur, i, 3);
				EnumType type = (EnumType) dtm.getValueAt(i,1);
				if (mapContraintes.containsKey(type)) {
					if (Float.valueOf(valeur) < mapContraintes.get(type)[0] || Float.valueOf(valeur) > mapContraintes.get(type)[1])
					{
						dtm.setValueAt(iconWarning, i, 4);
					} else {
						dtm.setValueAt("", i, 4);
					}
				}
			}
		}
	}
}
