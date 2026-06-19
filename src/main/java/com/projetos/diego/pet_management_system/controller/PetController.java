package com.projetos.diego.pet_management_system.controller;

import com.projetos.diego.pet_management_system.domain.Pet;
import com.projetos.diego.pet_management_system.dto.PetPostRequest;
import com.projetos.diego.pet_management_system.dto.PetPutRequest;
import com.projetos.diego.pet_management_system.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
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

    @Operation(summary = "List all pets with pagination",
            description = "Returns a paginated list of registered pets.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All pets were listed with pagination successfully"),
    })
    @GetMapping
    public ResponseEntity<Page<Pet>> list(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(petService.listAll(pageable));
    }

    @Operation(summary = "List all pets",
            description = "Returns a list of all registered pets.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All pets were listed successfully"),
    })
    @GetMapping("/all")
    public ResponseEntity<List<Pet>> listAll() {
        return ResponseEntity.ok(petService.listAllNonPageable());
    }

    @Operation(summary = "Find a registered pet",
            description = "Returns the pet that has the ID equal to the provided parameter.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search was completed successfully and pet was found"),
            @ApiResponse(responseCode = "404", description = "Pet does not exist in the database"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Pet> findById(
            @Parameter(name = "id", description = "This is the ID of the pet", example = "42")
            @PathVariable long id) {
        return ResponseEntity.ok(petService.findByIdOrThrowResourceNotFoundException(id));
    }

    @Operation(summary = "List pets with similar names",
            description = "Returns a list of pets with names similar to the provided parameter.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search was completed successfully, even if pet is not found")
    })
    @GetMapping("/find")
    public ResponseEntity<List<Pet>> findByName(
            @Parameter(description = "Pet name used in the search", example = "Rex")
            @RequestParam String name) {
        return ResponseEntity.ok(petService.findByName(name));
    }

    @Operation(summary = "Create a new pet",
            description = "Creates a new pet using the provided information and returns the created resource.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pet was saved successfully"),
            @ApiResponse(responseCode = "400", description = "One or more fields in the request body is not valid"),
            @ApiResponse(responseCode = "404", description = "Provided postal code has a valid format but does not exists"),
            @ApiResponse(responseCode = "500", description = "Address lookup service was not available"),
            @ApiResponse(responseCode = "502", description = "Address lookup service did not function correctly")
    })
    @PostMapping
    public ResponseEntity<Pet> save(@RequestBody @Valid PetPostRequest petPostRequest) {
        return new ResponseEntity<>(petService.save(petPostRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "Replaces a pet",
            description = "Replaces a registered pet using the provided information and keeping its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pet was updated successfully"),
            @ApiResponse(responseCode = "404", description = "Pet does not exist in the database")
    })
    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody @Valid PetPutRequest petPutRequest) {
        petService.replace(petPutRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Deletes a pet",
            description = "Deletes a registered pet that has the ID equal to the provided parameter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pet was deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Pet does not exist in the database")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(
            @Parameter(name = "id", description = "This is the ID of the pet", example = "42")
            @PathVariable @Valid long id) {
        petService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
