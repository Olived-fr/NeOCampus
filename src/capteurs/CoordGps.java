package capteurs;

public class CoordGps {

	private float latitude ; 
	private float longitude ;
	
	public CoordGps(float latitude, float longitude){
		this.latitude = latitude ; 
		this.longitude = longitude ;	
	}

	public float getLatitude() {
		return latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	@Override
	public String toString() {
		return "(" + latitude + ", " + longitude + ")";
	}
	
	
	
}
