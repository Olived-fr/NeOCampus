package capteurs;

public class CoordInterieur {

	private String batiment ; 
	private String etage ; 
	private String salle ; 
	private String commentaire ; 
	
	public CoordInterieur(String batiment, String etage, String salle, String commentaire){
		this.batiment = batiment ; 
		this.etage = etage ; 
		this.salle = salle ; 
		this.commentaire = commentaire ; 
	}

	public String getBatiment() {
		return batiment;
	}

	public String getEtage() {
		return etage;
	}

	public String getSalle() {
		return salle;
	}

	public String getCommentaire() {
		return commentaire;
	}
	
	
}
