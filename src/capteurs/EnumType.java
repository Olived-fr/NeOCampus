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
	
	public static String[] noms() {
		EnumType[] types = values();
		String[] noms = new String[types.length];
		
		for(int i = 0; i < types.length; i++) {
			noms[i] = types[i].name();
		}
		
		return noms;
	}
}
