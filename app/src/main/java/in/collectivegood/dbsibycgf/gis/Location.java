package in.collectivegood.dbsibycgf.gis;


public class Location {
    private double lat;
    private double lon;

    public Location(double lat, double lon) {

        this.lat = lat;
        this.lon = lon;
    }

    public Location() {

    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    @Override
    public String toString() {
        return "Latitude: " + lat +
                "\nLongitude: " + lon;
    }
}
