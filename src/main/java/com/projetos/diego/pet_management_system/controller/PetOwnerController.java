package com.projetos.diego.pet_management_system.controller;

import com.projetos.diego.pet_management_system.domain.PetOwner;
import com.projetos.diego.pet_management_system.dto.PetOwnerResponse;
import com.projetos.diego.pet_management_system.mapper.PetOwnerMapper;
import com.projetos.diego.pet_management_system.service.PetOwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
