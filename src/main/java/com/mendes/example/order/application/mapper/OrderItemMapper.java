package com.mendes.example.order.application.mapper;

import com.mendes.example.order.application.dto.OrderItemResponse;
import com.mendes.example.order.domain.OrderItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    /**
     * Converte OrderItem entity para OrderItemResponse
     */
    OrderItemResponse toResponse(OrderItem orderItem);

    /**
     * Converte lista de OrderItem entities para lista de OrderItemResponse
     */
    List<OrderItemResponse> toResponseList(List<OrderItem> orderItems);
}
