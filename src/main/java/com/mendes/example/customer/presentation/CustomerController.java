package com.mendes.example.customer.presentation;

import com.mendes.example.customer.application.CustomerService;
import com.mendes.example.customer.application.dto.CreateCustomerRequest;
import com.mendes.example.customer.application.dto.CustomerResponse;
import com.mendes.example.customer.application.dto.UpdateCustomerRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para gerenciar clientes.
 *
 * Fornece endpoints para CRUD de clientes e busca por e-mail. A versão da API
 * é definida via atributo {@code version} nas anotações de mapeamento.
 */
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    /**
     * Lista todos os clientes.
     */
    @GetMapping(version = "1")
    public ResponseEntity<List<CustomerResponse>> listAllCustomers() {
        List<CustomerResponse> responses = customerService.listAllCustomers();
        return ResponseEntity.ok(responses);
    }

    /**
     * Recupera um cliente pelo identificador.
     */
    @GetMapping(path = "/{id}", version = "1")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id) {
        CustomerResponse response = customerService.getCustomerById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Recupera um cliente pelo e-mail.
     */
    @GetMapping(path = "/email/{email}", version = "1")
    public ResponseEntity<CustomerResponse> getCustomerByEmail(@PathVariable String email) {
        CustomerResponse response = customerService.getCustomerByEmail(email);
        return ResponseEntity.ok(response);
    }

    /**
     * Cria um novo cliente.
     */
    @PostMapping(version = "1")
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody CreateCustomerRequest request) {
        CustomerResponse response = customerService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza um cliente existente.
     */
    @PutMapping(path = "/{id}", version = "1")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable Long id,
            @RequestBody UpdateCustomerRequest request) {
        CustomerResponse response = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Exclui um cliente pelo identificador.
     */
    @DeleteMapping(path = "/{id}", version = "1")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
