package service;

import lombok.extern.log4j.Log4j2;
import domain.Pet;
import repository.PetRepository;
import view.UserInterface;

import java.util.List;

@Log4j2
public class PetService {
    public static void save() {
        Pet pet = Pet.builder()
                .name(UserInterface.collectPetName())
                .type(UserInterface.collectPetType())
                .sex(UserInterface.collectPetSex())
                .address(UserInterface.collectAddress())
                .age(UserInterface.collectPetAge())
                .weight(UserInterface.collectPetWeight())
                .race(UserInterface.collectPetRace())
                .build();
        PetRepository.save(pet);
        UserInterface.confirmSave(pet);
    }

    public static void findByCriteria() {
        int chosenCriteria = UserInterface.askForCriteria();
        switch (chosenCriteria) {
            case 1:
                String name = UserInterface.askForCriteriaValue();
                findByName(name);
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
        }
    }

    public static void findAll() {
        List<Pet> pets = PetRepository.findByName("");
        UserInterface.showSearchResult(pets);
    }

    public static void findByName(String name) {
        List<Pet> pets = PetRepository.findByName(name);
        UserInterface.showSearchResult(pets);
    }
}
