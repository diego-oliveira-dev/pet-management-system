package com.projetos.diego.pet_management_system.controller;

import com.projetos.diego.pet_management_system.domain.pet.Pet;
import com.projetos.diego.pet_management_system.dto.request.PetPostRequest;
import com.projetos.diego.pet_management_system.dto.request.PetPutRequest;
import com.projetos.diego.pet_management_system.dto.response.PetResponse;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("pets")
@RequiredArgsConstructor
public class PetController {
    private final PetService petService;
    private final PetMapper petMapper;

    @Operation(summary = "List pets registered by the user",
            description = "Returns a list of all pets registered by the user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pets were successfully retrieved")
    })
    @GetMapping
    public ResponseEntity<List<PetResponse>> list(@AuthenticationPrincipal Jwt jwt) {
        long userId = Long.parseLong(jwt.getSubject());
        List<Pet> pets = petService.findPetsByOwnerId(userId);
        List<PetResponse> responseList = pets.stream().map(petMapper::toResponse).toList();
        return ResponseEntity.ok(responseList);
    }

    @Operation(summary = "List all pets with pagination",
            description = "Returns a paginated list of all registered pets. Limited to ADMIN users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pets were successfully retrieved"),
            @ApiResponse(responseCode = "403", description = "User does not have permission to use this endpoint")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<Page<PetResponse>> listAll(@ParameterObject Pageable pageable) {
        Page<Pet> pets = petService.listAll(pageable);
        Page<PetResponse> responsePage = pets.map(petMapper::toResponse);
        return ResponseEntity.ok(responsePage);
    }

    @Operation(summary = "Find a registered pet",
            description = "Returns the pet that has the ID equal to the provided parameter.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pet was successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Pet does not exist in the database"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<PetResponse> findById(
            @Parameter(name = "id", description = "This is the ID of the pet", example = "42")
            @PathVariable long id, @AuthenticationPrincipal Jwt jwt) {
        long userId = Long.parseLong(jwt.getSubject());
        Pet pet = petService.findPetByIdAndPetOwnerId(id, userId);
        PetResponse response = petMapper.toResponse(pet);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "List pets with similar names",
            description = "Returns a list of pets with names similar to the provided parameter.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search was completed successfully, even if pet is not found")
    })
    @GetMapping("/find")
    public ResponseEntity<List<PetResponse>> findByName(
            @Parameter(description = "Pet name used in the search", example = "Rex")
            @RequestParam String name,
            @AuthenticationPrincipal Jwt jwt) {
        long userId = Long.parseLong(jwt.getSubject());
        List<Pet> pets = petService.findPetsByName(name, userId);
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
    public ResponseEntity<PetResponse> save(
            @RequestBody @Valid PetPostRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        long userId = Long.parseLong(jwt.getSubject());
        Pet savedPet = petService.save(request, userId);
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
    public ResponseEntity<Void> replace(
            @RequestBody @Valid PetPutRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        long userId = Long.parseLong(jwt.getSubject());
        petService.replace(request, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Deletes a pet",
            description = "Deletes a registered pet that has the ID equal to the provided parameter. Limited to ADMIN users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pet was deleted successfully"),
            @ApiResponse(responseCode = "403", description = "User does not have permission to use this endpoint"),
            @ApiResponse(responseCode = "404", description = "Pet does not exist in the database")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(name = "id", description = "This is the ID of the pet", example = "42")
            @PathVariable @Valid long id) {
        petService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
