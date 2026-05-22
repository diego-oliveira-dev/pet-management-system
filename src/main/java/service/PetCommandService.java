package service;

import lombok.extern.log4j.Log4j2;
import domain.Pet;
import repository.PetCommandRepository;
import view.UserInterface;

@Log4j2
public class PetCommandService {
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
        PetCommandRepository.save(pet);
        UserInterface.confirmSave(pet);
    }

    public static void delete() {
        boolean searchingByCriteria = UserInterface.isUserSearchingByCriteria();
        if (searchingByCriteria) PetFinderService.findByCriteria();
        else PetFinderService.findAll();
        String id = UserInterface.askForPetToDelete();
        boolean isUserSure = UserInterface.doubleCheck();
        if (isUserSure) {
            PetCommandRepository.delete(id);
            UserInterface.confirmDelete();
        }
    }
}
