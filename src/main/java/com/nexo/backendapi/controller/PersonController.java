package com.nexo.backendapi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexo.backendapi.dto.request.AddressRequestDTO;
import com.nexo.backendapi.dto.request.PersonRequestDTO;
import com.nexo.backendapi.dto.response.PersonResponseDTO;
import com.nexo.backendapi.exception.ResourceNotFoundException;
import com.nexo.backendapi.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/personas")
public class PersonController {

    private final PersonService personService;
    private final ObjectMapper objectMapper;

    public PersonController(PersonService personService, ObjectMapper objectMapper) {
        this.personService = personService;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    @Operation(summary = "Crea una persona", description = "Crea una nueva persona con los datos proporcionados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Persona creada exitosamente",
                    content = @Content(schema = @Schema(implementation = PersonResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "La solicitud es inválida debido a datos incorrectos.")
    })
    public ResponseEntity<PersonResponseDTO> createPerson(
            @RequestParam("dni") Long dni,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("age") int age,
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("addressSet") String addressSetJson) throws IOException {

        Set<AddressRequestDTO> addressSet = new HashSet<>();

        if (addressSetJson != null && !addressSetJson.isEmpty()) {
            addressSet = objectMapper.readValue(addressSetJson, new TypeReference<>() {
            });
        }
        PersonRequestDTO personRequestDTO = new PersonRequestDTO(dni, firstName, lastName, age, photo.getBytes(), addressSet);
        PersonResponseDTO createdPerson = personService.createPerson(personRequestDTO);

        return new ResponseEntity<>(createdPerson, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Obtiene todas las personas", description = "Obtiene una lista de todas las personas registradas en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud exitosa, lista de personas",
                    content = @Content(schema = @Schema(implementation = PersonResponseDTO.class)))
    })
    public ResponseEntity<List<PersonResponseDTO>> getPeople() {
        return ResponseEntity.ok(personService.findPeople());
    }

    @GetMapping("/{dni}")
    @Operation(summary = "Obtiene una persona por DNI",
            description = "Obtiene una persona específica utilizando su DNI.",
            parameters = {@Parameter(name = "dni", description = "DNI de la persona", example = "12345678")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud exitosa, persona obtenida por DNI",
                    content = @Content(schema = @Schema(implementation = PersonResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Persona no encontrada")
    })
    public ResponseEntity<PersonResponseDTO> getPersonByDni(@PathVariable("dni") Long dni) throws ResourceNotFoundException {
        return ResponseEntity.ok(personService.findPersonByDni(dni));
    }

    @GetMapping("/buscar")
    @Operation(summary = "Obtiene personas por filtro",
            description = "Obtiene una lista de personas filtradas por DNI, nombre o edad.",
            parameters = {
                    @Parameter(name = "dni", description = "DNI de la persona", example = "12345678"),
                    @Parameter(name = "firstName", description = "Nombre de la persona", example = "Juan"),
                    @Parameter(name = "age", description = "Edad de la persona", example = "30")
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud exitosa, lista de personas obtenida por filtro",
                    content = @Content(schema = @Schema(implementation = PersonResponseDTO.class)))
    })
    public ResponseEntity<List<PersonResponseDTO>> getPeopleByFilter(
            @RequestParam(required = false) String dni,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) Integer age) {
        return ResponseEntity.ok(personService.findPeopleByFilter(dni, firstName, age));
    }

    @PutMapping("/{dni}")
    @Operation(summary = "Actualiza una persona",
            description = "Actualiza la información de una persona existente utilizando su DNI.",
            parameters = {@Parameter(name = "dni", description = "DNI de la persona", example = "12345678")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud exitosa, devuelve la información actualizada de la persona",
                    content = @Content(schema = @Schema(implementation = PersonResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Persona no encontrada")
    })
    public ResponseEntity<PersonResponseDTO> updatePerson(
            @PathVariable("dni") Long dni,
            @RequestBody @Valid PersonRequestDTO personRequestDTO)
            throws ResourceNotFoundException {
        personRequestDTO.setDni(dni);
        return ResponseEntity.ok(personService.updatePerson(personRequestDTO));
    }

    @DeleteMapping("/{dni}")
    @Operation(summary = "Elimina una persona",
            description = "Borra una persona utilizando su DNI.",
            parameters = {@Parameter(name = "dni", description = "DNI de la persona", example = "12345678")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Borrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Persona no encontrada")
    })
    public ResponseEntity<Void> deletePerson(@PathVariable("dni") Long dni) throws ResourceNotFoundException {
        personService.deletePerson(dni);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exportar-csv")
    @Operation(summary = "Exporta personas a CSV",
            description = "Exporta una lista de todas las personas a un archivo CSV.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Archivo CSV generado exitosamente",
                    content = @Content(mediaType = "application/octet-stream"))
    })
    public ResponseEntity<InputStreamResource> exportPeopleToCsv() {
        ByteArrayInputStream in = personService.exportPeopleToCsv();
        InputStreamResource file = new InputStreamResource(in);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=personas.csv")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }
}
