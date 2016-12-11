package capteurs;

public class Capteurs {

	private String id ; 
	private EnumType type ;
	private String localisation ;
	private int intervalleMin ; 
	private int intervalleMax ;
	private CoordGps gps ; 
	private CoordInterieur interieur ;
	private boolean saisieValeur ; 
	private float valeur ;
	
	public Capteurs(String id, EnumType type, String localisation, int intervalleMin, 
			int intervalleMax, CoordGps gps, CoordInterieur interieur,boolean saisieValeur, float valeur){
		this.id = id ;
		this.type = type ; 
		this.localisation = localisation ; 
		this.intervalleMin = intervalleMin ; 
		this.intervalleMax = intervalleMax ;
		this.gps = gps ;
		this.interieur = interieur ; 
		
	}

	public String getId() {
		return id;
	}

	public EnumType getType() {
		return type;
	}

	public String getLocalisation() {
		return localisation;
	}

	public int getIntervalleMin() {
		return intervalleMin;
	}

	public int getIntervalleMax() {
		return intervalleMax;
	}

	public CoordGps getGps() {
		return gps;
	}

	public CoordInterieur getInterieur() {
		return interieur;
	}

	public boolean isSaisieValeur() {
		return saisieValeur;
	}

	public float getValeur() {
		return valeur;
	}

	public void setValeur(float valeur) {
		this.valeur = valeur;
	}
	

	
	
	
}
