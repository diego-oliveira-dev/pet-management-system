package com.projetos.diego.pet_management_system.controller;

import com.projetos.diego.pet_management_system.domain.Pet;
import com.projetos.diego.pet_management_system.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("pets")
@RequiredArgsConstructor
public class PetController {
    private final PetService petService;

    @GetMapping
    public ResponseEntity<List<Pet>> list() {
        return ResponseEntity.ok(petService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> findById(@PathVariable long id) {
        return ResponseEntity.ok(petService.findByIdOrThrowResourceNotFoundException(id));
    }
}
