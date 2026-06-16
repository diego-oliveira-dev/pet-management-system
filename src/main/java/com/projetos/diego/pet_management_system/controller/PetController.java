package com.projetos.diego.pet_management_system.controller;

import com.projetos.diego.pet_management_system.domain.Pet;
import com.projetos.diego.pet_management_system.requests.PetPostRequestBody;
import com.projetos.diego.pet_management_system.requests.PetPutRequestBody;
import com.projetos.diego.pet_management_system.service.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("pets")
@RequiredArgsConstructor
public class PetController {
    private final PetService petService;

    @GetMapping
    public ResponseEntity<Page<Pet>> list(Pageable pageable) {
        return ResponseEntity.ok(petService.listAll(pageable));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Pet>> listAll() {
        return ResponseEntity.ok(petService.listAllNonPageable());
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
        return new ResponseEntity<>(petService.save(petPostRequestBody), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody @Valid PetPutRequestBody petPutRequestBody) {
        petService.replace(petPutRequestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable @Valid long id) {
        petService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
