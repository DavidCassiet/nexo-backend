package com.nexo.backendapi.dto.mapper;

import com.nexo.backendapi.dto.request.PersonRequestDTO;
import com.nexo.backendapi.dto.response.PersonResponseDTO;
import com.nexo.backendapi.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface PersonMapper {

    Person requestToPerson(PersonRequestDTO personRequestDTO);

    @Mapping(source = "addressSet", target = "addressResponseSet")
    PersonResponseDTO personToResponse(Person person);

    List<PersonResponseDTO> personToResponseList(List<Person> personList);
}
