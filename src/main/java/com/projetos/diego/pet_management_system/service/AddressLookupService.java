package com.projetos.diego.pet_management_system.service;

import com.projetos.diego.pet_management_system.domain.pet.Address;

public interface AddressLookupService {
    Address findByPostalCode(String postalCode);
}
