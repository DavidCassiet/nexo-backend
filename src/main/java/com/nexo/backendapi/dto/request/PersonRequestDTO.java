package com.nexo.backendapi.dto.request;

import java.util.Set;

public class PersonRequestDTO {
    private Long dni;
    private String firstName;
    private String lastName;
    private int age;
    private byte[] photo;
    private Set<AddressRequestDTO> addressSet;

    public PersonRequestDTO(Long dni, String firstName, String lastName, int age, byte[] photo, Set<AddressRequestDTO> addressSet) {
        this.dni = dni;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.photo = photo;
        this.addressSet = addressSet;
    }

    public Long getDni() {
        return dni;
    }

    public void setDni(Long dni) {
        this.dni = dni;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public Set<AddressRequestDTO> getAddressSet() {
        return addressSet;
    }

    public void setAddressSet(Set<AddressRequestDTO> addressSet) {
        this.addressSet = addressSet;
    }
}
