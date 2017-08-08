package in.collectivegood.dbsibycgf.gis;


public class Location {
    private double lat;
    private double lon;

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public Location(double lat, double lon) {

        this.lat = lat;
        this.lon = lon;
    }

    public Location() {

    }
}
