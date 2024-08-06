package com.nexo.backendapi.service.implementation;

import com.nexo.backendapi.dto.mapper.PersonMapper;
import com.nexo.backendapi.dto.request.PersonRequestDTO;
import com.nexo.backendapi.dto.response.PersonResponseDTO;
import com.nexo.backendapi.exception.ResourceAlreadyExistsException;
import com.nexo.backendapi.exception.ResourceNotFoundException;
import com.nexo.backendapi.model.Address;
import com.nexo.backendapi.model.Person;
import com.nexo.backendapi.repository.PersonRepository;
import com.nexo.backendapi.service.PersonService;
import com.opencsv.CSVWriter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    public PersonServiceImpl(PersonRepository personRepository, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    @Override
    @Transactional
    public PersonResponseDTO createPerson(PersonRequestDTO personRequestDTO) {
        personRepository.findByDni(personRequestDTO.getDni())
                .ifPresent((person) -> {
                    throw new ResourceAlreadyExistsException("Persona");
                });

        Person person = personMapper.requestToPerson(personRequestDTO);

        for (Address address : person.getAddressSet()) {
            address.setPerson(person);
        }
        person = personRepository.save(person);

        return personMapper.personToResponse(person);
    }

    @Override
    public PersonResponseDTO findPersonByDni(Long dni) throws ResourceNotFoundException {
        Person person = findByDni(dni);
        return personMapper.personToResponse(person);
    }

    @Override
    public List<PersonResponseDTO> findPeople() {
        List<Person> people = personRepository.findAll();
        return personMapper.personToResponseList(people);
    }

    @Override
    public List<PersonResponseDTO> findPeopleByFilter(String dni, String firstName, Integer age) {
        List<Person> people = personRepository.findPeopleByFilter(dni, firstName, age);
        return personMapper.personToResponseList(people);
    }

    @Override
    @Transactional
    public PersonResponseDTO updatePerson(PersonRequestDTO personRequestDTO) throws ResourceNotFoundException {
        findByDni(personRequestDTO.getDni());
        Person updatedPerson = personMapper.requestToPerson(personRequestDTO);

        for (Address address : updatedPerson.getAddressSet()) {
            address.setPerson(updatedPerson);
        }

        updatedPerson = personRepository.save(updatedPerson);

        return personMapper.personToResponse(updatedPerson);
    }

    @Override
    public ByteArrayInputStream exportPeopleToCsv() {
        List<Person> people = personRepository.findAll();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(outputStream))) {
            writer.writeNext(new String[]{"DNI", "Nombre", "Apellido", "Edad"});

            for (Person person : people) {
                writer.writeNext(new String[]{
                        person.getDni().toString(),
                        person.getFirstName(),
                        person.getLastName(),
                        String.valueOf(person.getAge()),
                });
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al crear archivo CSV", e);
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    @Override
    public void deletePerson(Long dni) throws ResourceNotFoundException {
        Person person = findByDni(dni);
        personRepository.delete(person);
    }

    private Person findByDni(Long dni) throws ResourceNotFoundException {
        return personRepository.findByDni(dni).orElseThrow(() -> new ResourceNotFoundException("Persona"));
    }
}
