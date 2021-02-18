public class Locations {
    private int locationID;
    private String locationName;

    public Locations(int locationID, String locationName) {
        this.locationID = locationID;
        this.locationName = locationName;
    }

    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
