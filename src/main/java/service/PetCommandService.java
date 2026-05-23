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

    public static void update() {
        String id = getChosenPetId();
        int infoChoice = UserInterface.askForInfoToUpdate();
        switch (infoChoice) {
            case 1: {
                String newName = UserInterface.askForNewName();
                boolean isUserSure = UserInterface.doubleCheck();
                if (isUserSure) {
                    PetCommandRepository.updateName(id, newName);
                    UserInterface.confirmUpdate();
                }
                break;
            }
            case 2: {
                String newAge = UserInterface.askForNewAge();
                boolean isUserSure = UserInterface.doubleCheck();
                if (isUserSure) {
                    PetCommandRepository.updateAge(id, newAge);
                    UserInterface.confirmUpdate();
                }
                break;
            }
            case 3: {
                String newWeight = UserInterface.askForNewWeight();
                boolean isUserSure = UserInterface.doubleCheck();
                if (isUserSure) {
                    PetCommandRepository.updateWeight(id, newWeight);
                    UserInterface.confirmUpdate();
                }
                break;
                }
            case 4: {
                String newRace = UserInterface.askForNewRace();
                boolean isUserSure = UserInterface.doubleCheck();
                if (isUserSure) {
                    PetCommandRepository.updateRace(id, newRace);
                    UserInterface.confirmUpdate();
                }
                break;
            }
            case 5: {
                String newAddress = UserInterface.askForNewAddress();
                boolean isUserSure = UserInterface.doubleCheck();
                if (isUserSure) {
                    PetCommandRepository.updateAddress(id, newAddress);
                    UserInterface.confirmUpdate();
                }
                break;
            }
        }
    }

    public static void delete() {
        String id = getChosenPetId();
        boolean isUserSure = UserInterface.doubleCheck();
        if (isUserSure) {
            PetCommandRepository.delete(id);
            UserInterface.confirmDelete();
        }
    }

    public static String getChosenPetId() {
        boolean searchingByCriteria = UserInterface.isUserSearchingByCriteria();
        if (searchingByCriteria) PetFinderService.findByCriteria();
        else PetFinderService.findAll();
        return UserInterface.askForSelectedPet();
    }
}
