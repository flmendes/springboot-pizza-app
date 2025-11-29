package com.mendes.example.customer.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCustomerRequest {
    private String name;
    private String email;
    private String phone;
    private String address;
    private String zipCode;
    private String city;
    private String state;
}

