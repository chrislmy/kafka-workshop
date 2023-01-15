package com.example.kafkaworkshop.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Location {

    @JsonProperty("city")
    private String city;

    @JsonProperty("lat")
    private double latitude;

    @JsonProperty("lng")
    private double longitude;

    @JsonProperty("country")
    private String country;

    @JsonProperty("countryCode")
    private String countryCode;

    public Location(String city, double latitude, double longitude, String country, String countryCode) {
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.countryCode = countryCode;
    }

    public Location() {
    }

    public String getCity() {
        return city;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getCountryCode() {
        return countryCode;
    }
}
