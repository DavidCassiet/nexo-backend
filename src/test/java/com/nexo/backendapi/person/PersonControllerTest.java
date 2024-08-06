package com.nexo.backendapi.person;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexo.backendapi.controller.PersonController;
import com.nexo.backendapi.dto.request.AddressRequestDTO;
import com.nexo.backendapi.dto.request.PersonRequestDTO;
import com.nexo.backendapi.dto.response.AddressResponseDTO;
import com.nexo.backendapi.dto.response.PersonResponseDTO;
import com.nexo.backendapi.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PersonControllerTest {

    @InjectMocks
    private PersonController personController;

    @Mock
    private PersonService personService;

    @Mock
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(personController).build();
    }

    @Test
    void createPerson_ShouldReturnCreatedPerson() throws Exception {
        Long dni = 123456789L;
        String firstName = "John";
        String lastName = "Doe";
        int age = 30;
        byte[] photo = new byte[]{1, 2, 3};
        Set<AddressRequestDTO> addressRequestSet = new HashSet<>();
        Set<AddressResponseDTO> addressResponseSet = new HashSet<>();
        PersonRequestDTO personRequestDTO = new PersonRequestDTO(dni, firstName, lastName, age, photo, addressRequestSet);
        PersonResponseDTO personResponseDTO = new PersonResponseDTO(dni, firstName, lastName, age, photo, addressResponseSet);

        when(personService.createPerson(any(PersonRequestDTO.class))).thenReturn(personResponseDTO);

        MockMultipartFile photoFile = new MockMultipartFile("photo", "photo.jpg", "image/jpeg", photo);
        String addressSetJson = "[]";

        mockMvc.perform(multipart("/personas")
                        .file(photoFile)
                        .param("dni", dni.toString())
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .param("age", String.valueOf(age))
                        .param("addressSet", addressSetJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.dni").value(dni))
                .andExpect(jsonPath("$.firstName").value(firstName));
    }

    @Test
    void getPeople_ShouldReturnListOfPeople() throws Exception {
        PersonResponseDTO person1 = new PersonResponseDTO(123456789L, "John", "Doe", 30, new byte[]{}, new HashSet<>());
        PersonResponseDTO person2 = new PersonResponseDTO(987654321L, "Jane", "Doe", 25, new byte[]{}, new HashSet<>());
        List<PersonResponseDTO> people = Arrays.asList(person1, person2);

        when(personService.findPeople()).thenReturn(people);

        mockMvc.perform(get("/personas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].dni").value(123456789L))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));
    }

    @Test
    void getPersonByDni_ShouldReturnPerson() throws Exception {
        Long dni = 123456789L;
        PersonResponseDTO personResponseDTO = new PersonResponseDTO(dni, "John", "Doe", 30, new byte[]{}, new HashSet<>());

        when(personService.findPersonByDni(dni)).thenReturn(personResponseDTO);

        mockMvc.perform(get("/personas/{dni}", dni))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.dni").value(dni))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void updatePerson_ShouldReturnUpdatedPerson() throws Exception {
        Long dni = 123456789L;
        PersonRequestDTO personRequestDTO = new PersonRequestDTO(dni, "John", "Doe", 30, new byte[]{}, new HashSet<>());
        PersonResponseDTO personResponseDTO = new PersonResponseDTO(dni, "John", "Doe", 30, new byte[]{}, new HashSet<>());

        when(personService.updatePerson(any(PersonRequestDTO.class))).thenReturn(personResponseDTO);

        mockMvc.perform(put("/personas/{dni}", dni)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(personRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.dni").value(dni))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void deletePerson_ShouldReturnNoContent() throws Exception {
        Long dni = 123456789L;

        doNothing().when(personService).deletePerson(dni);

        mockMvc.perform(delete("/personas/{dni}", dni))
                .andExpect(status().isNoContent());
    }

    @Test
    void exportPeopleToCsv_ShouldReturnCsvFile() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream("DNI,Nombre,Apellido,Edad\n123456789,John,Doe,30\n".getBytes());
        InputStreamResource file = new InputStreamResource(in);

        when(personService.exportPeopleToCsv()).thenReturn(in);

        mockMvc.perform(get("/personas/exportar-csv"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=personas.csv"));
    }
}

