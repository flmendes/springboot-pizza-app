package com.mendes.example.pizza.application.mapper;

import com.mendes.example.pizza.application.dto.CreatePizzaRequest;
import com.mendes.example.pizza.application.dto.PizzaResponse;
import com.mendes.example.pizza.application.dto.UpdatePizzaRequest;
import com.mendes.example.pizza.domain.Pizza;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Mapper MapStruct para conversões entre DTOs de pizza e a entidade {@link Pizza}.
 */
@Mapper(componentModel = "spring")
public interface PizzaMapper {

    /**
     * Converte CreatePizzaRequest para Pizza entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "available", constant = "true")
    Pizza toEntity(CreatePizzaRequest request);

    /**
     * Converte Pizza entity para PizzaResponse
     */
    PizzaResponse toResponse(Pizza pizza);

    /**
     * Converte lista de Pizza entities para lista de PizzaResponse
     */
    List<PizzaResponse> toResponseList(List<Pizza> pizzas);

    /**
     * Atualiza Pizza entity com dados do UpdatePizzaRequest
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "available", ignore = true)
    void updateEntityFromRequest(UpdatePizzaRequest request, @MappingTarget Pizza pizza);
}
