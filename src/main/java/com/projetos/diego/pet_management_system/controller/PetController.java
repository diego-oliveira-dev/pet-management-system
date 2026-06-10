package com.projetos.diego.pet_management_system.controller;

import com.projetos.diego.pet_management_system.domain.Pet;
import com.projetos.diego.pet_management_system.requests.PetPostRequestBody;
import com.projetos.diego.pet_management_system.service.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/find")
    public ResponseEntity<List<Pet>> findByName(@RequestParam String name) {
        return ResponseEntity.ok(petService.findByName(name));
    }

    @PostMapping
    public ResponseEntity<Pet> save(@RequestBody @Valid PetPostRequestBody petPostRequestBody) {
        return ResponseEntity.ok(petService.save(petPostRequestBody));
    }
}
