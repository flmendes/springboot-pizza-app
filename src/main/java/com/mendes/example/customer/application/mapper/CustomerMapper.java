package com.mendes.example.customer.application.mapper;

import com.mendes.example.customer.application.dto.CreateCustomerRequest;
import com.mendes.example.customer.application.dto.CustomerResponse;
import com.mendes.example.customer.application.dto.UpdateCustomerRequest;
import com.mendes.example.customer.domain.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Mapper MapStruct para conversões entre DTOs de cliente e a entidade {@link Customer}.
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper {

    /**
     * Converte CreateCustomerRequest para Customer entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Customer toEntity(CreateCustomerRequest request);

    /**
     * Converte Customer entity para CustomerResponse
     */
    CustomerResponse toResponse(Customer customer);

    /**
     * Converte lista de Customer entities para lista de CustomerResponse
     */
    List<CustomerResponse> toResponseList(List<Customer> customers);

    /**
     * Atualiza Customer entity com dados do UpdateCustomerRequest
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(UpdateCustomerRequest request, @MappingTarget Customer customer);
}
