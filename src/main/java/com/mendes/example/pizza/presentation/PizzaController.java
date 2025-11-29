package com.mendes.example.pizza.presentation;

import com.mendes.example.pizza.application.PizzaService;
import com.mendes.example.pizza.application.dto.CreatePizzaRequest;
import com.mendes.example.pizza.application.dto.PizzaResponse;
import com.mendes.example.pizza.application.dto.UpdatePizzaRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para gerenciar pizzas.
 *
 * Fornece endpoints para listar, buscar, criar, atualizar e excluir pizzas,
 * assim como busca por nome. A versão da API é definida via atributo {@code version}.
 */
@RestController
@RequestMapping("/pizzas")
@RequiredArgsConstructor
public class PizzaController {

    private final PizzaService pizzaService;

    /**
     * Lista todas as pizzas disponíveis.
     */
    @GetMapping(version = "1")
    public ResponseEntity<List<PizzaResponse>> listAvailablePizzas() {
        List<PizzaResponse> responses = pizzaService.listAvailablePizzas();
        return ResponseEntity.ok(responses);
    }

    /**
     * Recupera uma pizza pelo identificador.
     */
    @GetMapping(path = "/{id}", version = "1")
    public ResponseEntity<PizzaResponse> getPizzaById(@PathVariable Long id) {
        PizzaResponse response = pizzaService.getPizzaById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Cria uma nova pizza.
     */
    @PostMapping(version = "1")
    public ResponseEntity<PizzaResponse> createPizza(@RequestBody CreatePizzaRequest request) {
        PizzaResponse response = pizzaService.createPizza(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza uma pizza existente.
     */
    @PutMapping(path = "/{id}", version = "1")
    public ResponseEntity<PizzaResponse> updatePizza(
            @PathVariable Long id,
            @RequestBody UpdatePizzaRequest request) {
        PizzaResponse response = pizzaService.updatePizza(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Exclui uma pizza pelo identificador.
     */
    @DeleteMapping(path = "/{id}", version = "1")
    public ResponseEntity<Void> deletePizza(@PathVariable Long id) {
        pizzaService.deletePizza(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Busca pizzas por parte do nome (case-insensitive).
     */
    @GetMapping(path = "/search", version = "1")
    public ResponseEntity<List<PizzaResponse>> searchPizzas(@RequestParam String name) {
        List<PizzaResponse> responses = pizzaService.searchPizzasByName(name);
        return ResponseEntity.ok(responses);
    }
}
