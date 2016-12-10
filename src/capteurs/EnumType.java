package capteurs;

public enum EnumType {
	TEMPERATURE("temperature"), HUMIDITE("humidite"), LUMINOSITE("luminosite"),VOLUMESONORE("volume sonore"),CONSOECLAIRAGE("consommation eclairage"), EAUFROIDE("eau froide"), EAUCHAUDE("eau chaude"), VITESSEVENT("vitesse vent"), PRESSION("pression atmospherique");
	
	private String type ; 
	
	private EnumType(String type){
		this.type = type ;
	}
	
	public String getType(){
		return this.type ;
	}
}
