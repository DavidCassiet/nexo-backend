package com.nexo.backendapi.controller;

import com.nexo.backendapi.dto.request.PersonRequestDTO;
import com.nexo.backendapi.dto.response.PersonResponseDTO;
import com.nexo.backendapi.exception.ResourceNotFoundException;
import com.nexo.backendapi.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/personas")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public ResponseEntity<PersonResponseDTO> createPerson(@RequestBody @Valid PersonRequestDTO personRequestDTO) {
        return new ResponseEntity<>(personService.createPerson(personRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PersonResponseDTO>> getPeople() {
        return ResponseEntity.ok(personService.findPeople());
    }

    @GetMapping("/{dni}")
    public ResponseEntity<PersonResponseDTO> getPersonByDni(@PathVariable("dni") Long dni) throws ResourceNotFoundException {
        return ResponseEntity.ok(personService.findPersonByDni(dni));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<PersonResponseDTO>> getPeopleByFilter(
            @RequestParam(required = false) String dni,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) Integer age) {
        return ResponseEntity.ok(personService.findPeopleByFilter(dni, firstName, age));
    }

    @PutMapping("/{dni}")
    public ResponseEntity<PersonResponseDTO> updatePerson(
            @PathVariable("dni") Long dni,
            @RequestBody @Valid PersonRequestDTO personRequestDTO) throws ResourceNotFoundException {
        personRequestDTO.setDni(dni);
        return ResponseEntity.ok(personService.updatePerson(personRequestDTO));
    }

    @DeleteMapping("/{dni}")
    public ResponseEntity<Void> deletePerson(@PathVariable("dni") Long dni) throws ResourceNotFoundException {
        personService.deletePerson(dni);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exportar-csv")
    public ResponseEntity<InputStreamResource> exportPeopleToCsv() {
        ByteArrayInputStream in = personService.exportPeopleToCsv();
        InputStreamResource file = new InputStreamResource(in);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=personas.csv")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }
}
