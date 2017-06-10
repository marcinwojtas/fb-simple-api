package pl.wojtas.fb.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class City {
    private String countryName;
    private String cityName;
    private String stateName;
    private Coords coords;

    protected City() {}

    public City(String countryName, String cityName, String stateName, Coords coords) {
        this.countryName = countryName;
        this.cityName = cityName;
        this.stateName = stateName;
        this.coords = coords;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public Coords getCoords() {
        return coords;
    }

    public void setCoords(Coords coords) {
        this.coords = coords;
    }

    @Override
    public boolean equals(Object candidate) {
        return EqualsBuilder.reflectionEquals(this, candidate);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
