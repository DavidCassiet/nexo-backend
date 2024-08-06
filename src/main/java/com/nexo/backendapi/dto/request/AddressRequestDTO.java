package com.nexo.backendapi.dto.request;


public class AddressRequestDTO {
    private Long id;
    private String street;
    private String streetNumber;
    private String city;

    public AddressRequestDTO(Long id, String street, String streetNumber, String city) {
        this.id = id;
        this.street = street;
        this.streetNumber = streetNumber;
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
