package com.nexo.backendapi.service;

import com.nexo.backendapi.dto.request.PersonRequestDTO;
import com.nexo.backendapi.dto.response.PersonResponseDTO;
import com.nexo.backendapi.exception.ResourceNotFoundException;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface PersonService {
    PersonResponseDTO createPerson(PersonRequestDTO personRequestDTO);

    PersonResponseDTO findPersonByDni(Long dni) throws ResourceNotFoundException;

    List<PersonResponseDTO> findPeople();

    List<PersonResponseDTO> findPeopleByFilter(String dni, String firstName, Integer age);

    PersonResponseDTO updatePerson(PersonRequestDTO personRequestDTO) throws ResourceNotFoundException;

    ByteArrayInputStream exportPeopleToCsv();

    void deletePerson(Long dni) throws ResourceNotFoundException;
}
