package model;

public class Facility {

    private int id;
    private String facilityName;
    private String timing;
    private String description;

    public Facility() {
    }

    public Facility(int id,
                    String facilityName,
                    String timing,
                    String description) {

        this.id = id;
        this.facilityName = facilityName;
        this.timing = timing;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}