package com.mendes.example.pizza.application;

import com.mendes.example.pizza.application.dto.CreatePizzaRequest;
import com.mendes.example.pizza.application.dto.PizzaResponse;
import com.mendes.example.pizza.application.dto.UpdatePizzaRequest;
import com.mendes.example.pizza.application.mapper.PizzaMapper;
import com.mendes.example.pizza.domain.Pizza;
import com.mendes.example.pizza.infrastructure.PizzaRepository;
import com.mendes.example.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Serviço de aplicação responsável pelas regras de negócio e operações CRUD de pizzas.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PizzaService {

    private final PizzaRepository pizzaRepository;
    private final PizzaMapper pizzaMapper;

    /**
     * Lista as pizzas marcadas como disponíveis.
     */
    @Transactional(readOnly = true)
    public List<PizzaResponse> listAvailablePizzas() {
        List<Pizza> pizzas = pizzaRepository.findByAvailableTrue();
        return pizzaMapper.toResponseList(pizzas);
    }

    /**
     * Recupera uma pizza pelo identificador.
     *
     * @throws ResourceNotFoundException caso a pizza não seja encontrada
     */
    @Transactional(readOnly = true)
    public PizzaResponse getPizzaById(Long id) {
        Pizza pizza = pizzaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Pizza not found with id: " + id
                ));
        return pizzaMapper.toResponse(pizza);
    }

    /**
     * Cria uma nova pizza.
     */
    public PizzaResponse createPizza(CreatePizzaRequest request) {
        Pizza pizza = pizzaMapper.toEntity(request);
        Pizza savedPizza = pizzaRepository.save(pizza);
        return pizzaMapper.toResponse(savedPizza);
    }

    /**
     * Atualiza uma pizza existente.
     *
     * @throws ResourceNotFoundException caso a pizza não seja encontrada
     */
    public PizzaResponse updatePizza(Long id, UpdatePizzaRequest request) {
        Pizza pizza = pizzaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Pizza not found with id: " + id
                ));

        pizzaMapper.updateEntityFromRequest(request, pizza);
        Pizza savedPizza = pizzaRepository.save(pizza);
        return pizzaMapper.toResponse(savedPizza);
    }

    /**
     * Remove uma pizza pelo identificador.
     *
     * @throws ResourceNotFoundException caso a pizza não seja encontrada
     */
    public void deletePizza(Long id) {
        Pizza pizza = pizzaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Pizza not found with id: " + id
                ));
        pizzaRepository.delete(pizza);
    }

    /**
     * Busca pizzas cujo nome contenha o termo informado (case-insensitive).
     */
    @Transactional(readOnly = true)
    public List<PizzaResponse> searchPizzasByName(String name) {
        List<Pizza> pizzas = pizzaRepository.findByNameContainingIgnoreCase(name);
        return pizzaMapper.toResponseList(pizzas);
    }

    // Método interno para uso do OrderService - retorna entidade
    @Transactional(readOnly = true)
    public Pizza getPizzaEntityById(Long id) {
        return pizzaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Pizza not found with id: " + id
                ));
    }
}
