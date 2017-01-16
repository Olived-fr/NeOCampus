package fenetreVisualisation;

import java.util.Date;

public class ValeursTempsCapteurs {
	private float valeurs ; 
	private Date temps ;
	private int heures ;
	private int minutes ; 
	private int secondes ;
	
	public ValeursTempsCapteurs (float valeurs, Date temps, int heures, int minutes, int secondes){
		this.valeurs = valeurs ; 
		this.temps = temps ; 
		this.heures = heures ; 
		this.minutes = minutes ; 
		this.secondes = secondes ; 
	}

	public int getHeures() {
		return heures;
	}

	public void setHeures(int heures) {
		this.heures = heures;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public int getSecondes() {
		return secondes;
	}

	public void setSecondes(int secondes) {
		this.secondes = secondes;
	}

	public float getValeurs() {
		return valeurs;
	}

	public void setValeurs(float valeurs) {
		this.valeurs = valeurs;
	}

	public Date getTemps() {
		return temps;
	}

	public void setTemps(Date temps) {
		this.temps = temps;
	}
	
	

}
