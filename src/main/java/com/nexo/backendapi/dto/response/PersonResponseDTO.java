package com.nexo.backendapi.dto.response;

import java.util.Set;

public class PersonResponseDTO {
    private Long dni;
    private String firstName;
    private String lastName;
    private int age;
    private byte[] photo;
    private Set<AddressResponseDTO> addressResponseSet;

    public PersonResponseDTO(Long dni, String firstName, String lastName, int age, byte[] photo, Set<AddressResponseDTO> addressResponseSet) {
        this.dni = dni;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.photo = photo;
        this.addressResponseSet = addressResponseSet;
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

    public Set<AddressResponseDTO> getAddressResponseSet() {
        return addressResponseSet;
    }

    public void setAddressResponseSet(Set<AddressResponseDTO> addressResponseSet) {
        this.addressResponseSet = addressResponseSet;
    }
}
