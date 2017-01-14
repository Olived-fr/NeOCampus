package capteurs;

public enum EnumType {
	TEMPERATURE("TEMPERATURE"), HUMIDITE("HUMIDITE"), LUMINOSITE("LUMINOSITE"),VOLUMESONORE("VOLUMESONORE"),CONSOECLAIRAGE("CONSOECLAIRAGE"), EAUFROIDE("EAUFROIDE"), EAUCHAUDE("EAUCHAUDE"), VITESSEVENT("VITESSEVENT"), PRESSION("PRESSION");
	
	private String type ; 
	
	private EnumType(String type){
		this.type = type ;
	}
	
	public String getType(){
		return this.type ;
	}
}
