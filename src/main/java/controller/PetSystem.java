package controller;

import service.PetFinderService;
import service.PetService;
import view.UserInterface;

public class PetSystem {

    public void start() {
        UserInterface.showMainMenu();
        int choice = UserInterface.readChoice();
        handleRequest(choice);
    }

    public void handleRequest(int choice) {
        switch (choice) {
            case 1:
                PetService.save();
                checkSystemUsage();
                break;
            case 3:
                PetService.delete();
                checkSystemUsage();
                break;
            case 4:
                PetFinderService.findAll();
                checkSystemUsage();
                break;
            case 5:
                PetFinderService.findByCriteria();
                checkSystemUsage();
                break;
            case 6:
                shutDown();
                break;
        }
    }

    public void checkSystemUsage() {
        if (UserInterface.checkSystemUsage()) start();
        else shutDown();
    }

    public void shutDown() {
        UserInterface.shuttingDown();
    }
}
