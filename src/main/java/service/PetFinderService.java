package service;

import domain.Pet;
import repository.PetFinderRepository;
import view.UserInterface;

import java.util.List;

public class PetFinderService {

    public static void findAll() {
        List<Pet> pets = PetFinderRepository.findAll();
        UserInterface.showSearchResult(pets);
    }

    public static void findByCriteria() {
        int chosenCriteria = UserInterface.askForCriteria();
        switch (chosenCriteria) {
            case 1:
                String name = UserInterface.askNameToSearchFor();
                findByName(name);
                break;
            case 2:
                String sex = UserInterface.askSexToSearchFor();
                findBySex(sex);
                break;
            case 3:
                String age = UserInterface.askAgeToSearchFor();
                findByAge(age);
                break;
            case 4:
                String weight = UserInterface.askWeightToSearchFor();
                findByWeight(weight);
                break;
            case 5:
                String race = UserInterface.askRaceToSearchFor();
                findByRace(race);
                break;
            case 6:
                String address = UserInterface.askAddressToSearchFor();
                findByAddress(address);
                break;
        }
    }

    public static void findByName(String name) {
        List<Pet> pets = PetFinderRepository.findByName(name);
        UserInterface.showSearchResult(pets);
    }

    public static void findBySex(String sex) {
        List<Pet> pets = PetFinderRepository.findBySex(sex);
        UserInterface.showSearchResult(pets);
    }

    public static void findByAge(String age) {
        List<Pet> pets = PetFinderRepository.findByAge(age);
        UserInterface.showSearchResult(pets);
    }

    public static void findByWeight(String weight) {
        List<Pet> pets = PetFinderRepository.findByWeight(weight);
        UserInterface.showSearchResult(pets);
    }

    public static void findByRace(String race) {
        List<Pet> pets = PetFinderRepository.findByRace(race);
        UserInterface.showSearchResult(pets);
    }

    public static void findByAddress(String address) {
        List<Pet> pets = PetFinderRepository.findByAddress(address);
        UserInterface.showSearchResult(pets);
    }
}
