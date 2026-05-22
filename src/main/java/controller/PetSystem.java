package controller;

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
                break;
            case 4:
                PetService.findAll();
                break;
            case 5:
                PetService.findByCriteria();
                break;
            case 6:
                System.out.println("Encerrando o sistema...");
        }
        start();
    }
}
