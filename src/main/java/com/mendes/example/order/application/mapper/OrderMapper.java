package com.mendes.example.order.application.mapper;

import com.mendes.example.order.application.dto.OrderResponse;
import com.mendes.example.order.domain.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {

    /**
     * Converte Order entity para OrderResponse
     */
    @Mapping(source = "customer.id", target = "customerId")
    OrderResponse toResponse(Order order);

    /**
     * Converte lista de Order entities para lista de OrderResponse
     */
    List<OrderResponse> toResponseList(List<Order> orders);
}
