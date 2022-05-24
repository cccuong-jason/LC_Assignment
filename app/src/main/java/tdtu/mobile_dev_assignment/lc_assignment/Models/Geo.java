package tdtu.mobile_dev_assignment.lc_assignment.Models;

public class Geo {
    private double lat;
    private double lon;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public Geo() {}

    public Geo(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }
}
