package com.projetos.diego.pet_management_system.controller;

import com.projetos.diego.pet_management_system.domain.Pet;
import com.projetos.diego.pet_management_system.dto.PetPostRequest;
import com.projetos.diego.pet_management_system.dto.PetPutRequest;
import com.projetos.diego.pet_management_system.dto.PetResponse;
import com.projetos.diego.pet_management_system.mapper.PetMapper;
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
    private final PetMapper petMapper;

    @Operation(summary = "List all pets with pagination",
            description = "Returns a paginated list of registered pets.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All pets were listed with pagination successfully"),
    })
    @GetMapping
    public ResponseEntity<Page<PetResponse>> list(@ParameterObject Pageable pageable) {
        Page<Pet> pets = petService.listAll(pageable);
        Page<PetResponse> responsePage = pets.map(petMapper::toResponse);
        return ResponseEntity.ok(responsePage);
    }

    @Operation(summary = "List all pets",
            description = "Returns a list of all registered pets.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All pets were listed successfully"),
    })
    @GetMapping("/all")
    public ResponseEntity<List<PetResponse>> listAll() {
        List<Pet> pets = petService.listAllNonPageable();
        List<PetResponse> responseList = pets.stream().map(petMapper::toResponse).toList();
        return ResponseEntity.ok(responseList);
    }

    @Operation(summary = "Find a registered pet",
            description = "Returns the pet that has the ID equal to the provided parameter.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search was completed successfully and pet was found"),
            @ApiResponse(responseCode = "404", description = "Pet does not exist in the database"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<PetResponse> findById(
            @Parameter(name = "id", description = "This is the ID of the pet", example = "42")
            @PathVariable long id) {
        Pet pet = petService.findPetsById(id);
        PetResponse response = petMapper.toResponse(pet);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<PetResponse>> findByOwnerId(
            @Parameter(name = "ownerId", description = "This is the ID of the pet owner", example = "12")
            @PathVariable long ownerId) {
        List<Pet> pets = petService.findPetsByOwnerId(ownerId);
        List<PetResponse> responseList = pets.stream().map(petMapper::toResponse).toList();
        return ResponseEntity.ok(responseList);
    }

    @Operation(summary = "List pets with similar names",
            description = "Returns a list of pets with names similar to the provided parameter.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search was completed successfully, even if pet is not found")
    })
    @GetMapping("/find")
    public ResponseEntity<List<PetResponse>> findByName(
            @Parameter(description = "Pet name used in the search", example = "Rex")
            @RequestParam String name) {
        List<Pet> pets = petService.findByName(name);
        List<PetResponse> responseList = pets.stream().map(petMapper::toResponse).toList();
        return ResponseEntity.ok(responseList);
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
    public ResponseEntity<PetResponse> save(@RequestBody @Valid PetPostRequest request) {
        Pet savedPet = petService.save(request);
        PetResponse response = petMapper.toResponse(savedPet);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Replaces a pet",
            description = "Replaces a registered pet using the provided information and keeping its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pet was updated successfully"),
            @ApiResponse(responseCode = "404", description = "Pet does not exist in the database")
    })
    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody @Valid PetPutRequest request) {
        petService.replace(request);
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
