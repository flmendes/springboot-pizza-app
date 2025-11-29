package com.mendes.example.customer.application;

import com.mendes.example.customer.application.dto.CreateCustomerRequest;
import com.mendes.example.customer.application.dto.CustomerResponse;
import com.mendes.example.customer.application.dto.UpdateCustomerRequest;
import com.mendes.example.customer.application.mapper.CustomerMapper;
import com.mendes.example.customer.domain.Customer;
import com.mendes.example.customer.infrastructure.CustomerRepository;
import com.mendes.example.shared.exception.ResourceNotFoundException;
import com.mendes.example.shared.exception.InvalidOperationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Serviço de aplicação responsável pelas regras de negócio e operações CRUD de clientes.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    /**
     * Lista todos os clientes cadastrados.
     */
    @Transactional(readOnly = true)
    public List<CustomerResponse> listAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customerMapper.toResponseList(customers);
    }

    /**
     * Recupera um cliente pelo identificador.
     *
     * @throws ResourceNotFoundException caso o cliente não seja encontrado
     */
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Cliente não encontrado com o id: " + id
                ));
        return customerMapper.toResponse(customer);
    }

    /**
     * Recupera um cliente pelo e-mail.
     *
     * @throws ResourceNotFoundException caso o cliente não seja encontrado
     */
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Cliente não encontrado com o e-mail: " + email
                ));
        return customerMapper.toResponse(customer);
    }

    /**
     * Cria um novo cliente. Valida se o e-mail já está em uso.
     *
     * @throws InvalidOperationException caso o e-mail já esteja registrado
     */
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new InvalidOperationException(
                "E-mail já registrado: " + request.getEmail()
            );
        }

        Customer customer = customerMapper.toEntity(request);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toResponse(savedCustomer);
    }

    /**
     * Atualiza um cliente existente, validando conflitos de e-mail.
     *
     * @throws ResourceNotFoundException caso o cliente não seja encontrado
     * @throws InvalidOperationException caso o novo e-mail já esteja em uso por outro cliente
     */
    public CustomerResponse updateCustomer(Long id, UpdateCustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Cliente não encontrado com o id: " + id
                ));

        if (!customer.getEmail().equals(request.getEmail()) &&
                customerRepository.existsByEmail(request.getEmail())) {
            throw new InvalidOperationException(
                "E-mail já registrado: " + request.getEmail()
            );
        }

        customerMapper.updateEntityFromRequest(request, customer);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toResponse(savedCustomer);
    }

    /**
     * Remove um cliente pelo identificador.
     *
     * @throws ResourceNotFoundException caso o cliente não seja encontrado
     */
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Cliente não encontrado com o id: " + id
                ));
        customerRepository.delete(customer);
    }

    // Método interno para uso do OrderService - retorna entidade
    @Transactional(readOnly = true)
    public Customer getCustomerEntityById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Cliente não encontrado com o id: " + id
                ));
    }
}
