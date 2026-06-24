package com.projetos.diego.pet_management_system.controller;

import com.projetos.diego.pet_management_system.domain.PetOwner;
import com.projetos.diego.pet_management_system.dto.PetOwnerRequest;
import com.projetos.diego.pet_management_system.dto.PetOwnerResponse;
import com.projetos.diego.pet_management_system.mapper.PetOwnerMapper;
import com.projetos.diego.pet_management_system.service.PetOwnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("owners")
@RequiredArgsConstructor
public class PetOwnerController {
    private final PetOwnerService petOwnerService;
    private final PetOwnerMapper petOwnerMapper;

    @GetMapping("/all")
    public ResponseEntity<List<PetOwnerResponse>> listAll() {
        List<PetOwner> owners = petOwnerService.listAll();
        List<PetOwnerResponse> responseList = owners.stream().map(petOwnerMapper::toResponse).toList();
        return ResponseEntity.ok(responseList);
    }

    @PostMapping
    public ResponseEntity<PetOwnerResponse> save(@RequestBody @Valid PetOwnerRequest request) {
        PetOwner savedOwner = petOwnerService.save(request);
        PetOwnerResponse response = petOwnerMapper.toResponse(savedOwner);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
