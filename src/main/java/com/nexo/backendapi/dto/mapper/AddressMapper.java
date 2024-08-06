package com.nexo.backendapi.dto.mapper;

import com.nexo.backendapi.dto.request.AddressRequestDTO;
import com.nexo.backendapi.dto.response.AddressResponseDTO;
import com.nexo.backendapi.model.Address;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface AddressMapper {

    Address requestToAddress(AddressRequestDTO addressRequestDTO);

    AddressResponseDTO addressToResponse(Address address);

    List<AddressResponseDTO> addressToResponseList(List<Address> addressList);
}
