package view;

import domain.Pet;
import service.InputHandler;

import java.util.List;
import java.util.Scanner;

public class UserInterface {
    private static final Scanner SC = new Scanner(System.in);

    public static void showMainMenu() {
        System.out.println();
        System.out.println("============ MENU ============");
        System.out.println("""
                1. Cadastrar um novo pet
                2. Alterar os dados do pet cadastrado
                3. Deletar um pet cadastrado
                4. Listar todos os pets cadastrados
                5. Listar pets por algum critério (nome, sexo, idade, peso, raça ou endereço)
                6. Sair""");
        System.out.println("========================");
        System.out.print("Qual sua escolha? ");
    }

    public static int readChoice() {
        String choice = SC.nextLine();
        while (!InputHandler.isChoiceValid(choice)) {
            System.out.print("  Input inválido! Tente novamente: ");
            choice = SC.nextLine();
        }
        return Integer.parseInt(choice);
    }

    public static String collectPetName() {
        System.out.println();
        System.out.println("=== FORMULÁRIO DE CADASTRO ===");
        System.out.println("Responda às perguntas abaixo:");
        System.out.print("1 - Qual o nome do pet? ");
        String providedName = SC.nextLine();
        while (!InputHandler.isNameValid(providedName)) {
            System.out.print("    Nome inválido! Tente novamente: ");
            providedName = SC.nextLine();
        }
        if (providedName.isBlank()) {
            System.out.println("    O nome do pet será salvo como 'NAO INFORMADO'");
            return "NÃO INFORMADO";
        }
        return providedName;
    }

    public static Pet.PetType collectPetType() {
        System.out.print("2 - Qual o tipo do pet (Cachorro/Gato)? ");
        String providedType = SC.nextLine();
        while (!InputHandler.isTypeValid(providedType)) {
            System.out.print("    Tipo inválido! Tente novamente: ");
            providedType = SC.nextLine();
        }
        if (providedType.trim().equalsIgnoreCase(Pet.PetType.CACHORRO.type))
            return Pet.PetType.CACHORRO;
        if (providedType.trim().equalsIgnoreCase(Pet.PetType.GATO.type))
            return Pet.PetType.GATO;
        System.out.println("    O tipo do pet será salvo como 'NAO INFORMADO'");
        return Pet.PetType.NULL;
    }

    public static Pet.PetSex collectPetSex() {
        System.out.print("3 - Qual o sexo do pet (Macho/Fêmea)? ");
        String providedSex = SC.nextLine();
        while (!InputHandler.isSexValid(providedSex)) {
            System.out.print("    Sexo inválido! Tente novamente: ");
            providedSex = SC.nextLine();
        }
        String sex = InputHandler.normalizeText(providedSex);
        if (sex.trim().equalsIgnoreCase(Pet.PetSex.MACHO.sex))
            return Pet.PetSex.MACHO;
        if (sex.trim().equalsIgnoreCase(Pet.PetSex.FEMEA.sex))
            return Pet.PetSex.FEMEA;
        System.out.println("    O sexo do pet será salvo como 'NAO INFORMADO'");
        return Pet.PetSex.NULL;
    }

    public static String collectAddress() {
        System.out.print("4 - Qual endereço e bairro que ele foi encontrado? ");
        String providedAddress = SC.nextLine();
        if (providedAddress.isBlank()) {
            System.out.println("    O endereço onde o pet foi encontrado será salvo como 'NAO INFORMADO'");
            return "NÃO INFORMADO";
        }
        return providedAddress;
    }

    public static int collectPetAge() {
        System.out.print("5 - Qual a idade aproximada do pet (anos)? ");
        String providedAge = SC.nextLine();
        while (!InputHandler.isAgeValid(providedAge)) {
            System.out.print("    Idade inválida! Tente novamente: ");
            providedAge = SC.nextLine();
        }
        return Integer.parseInt(providedAge);
    }

    public static double collectPetWeight() {
        System.out.print("6 - Qual o peso aproximado do pet (kg)? ");
        String providedWeight = SC.nextLine();
        while (!InputHandler.isWeightValid(providedWeight)) {
            System.out.print("    Peso inválido! Tente novamente: ");
            providedWeight = SC.nextLine();
        }
        return Double.parseDouble(providedWeight);
    }

    public static String collectPetRace() {
        System.out.print("7 - Qual a raça do pet? ");
        String providedRace = SC.nextLine();
        while (!InputHandler.isNameValid(providedRace)) {
            System.out.print("    Nome de raça inválido! Tente novamente: ");
            providedRace = SC.nextLine();
        }
        if (providedRace.isBlank()) {
            System.out.println("    O nome de raça do pet será salvo como 'NAO INFORMADO'");
            return "NÃO INFORMADO";
        }
        return providedRace;
    }

    public static void confirmSave(Pet pet) {
        System.out.println();
        if (pet.getSex().equals(Pet.PetSex.FEMEA)) {
            System.out.printf("A %s foi cadastrada com sucesso!\n", pet.getName());
        }
        if (pet.getSex().equals(Pet.PetSex.MACHO)) {
            System.out.printf("O %s foi cadastrado com sucesso!\n", pet.getName());
        }
        if (pet.getSex().equals(Pet.PetSex.NULL)) {
            System.out.printf("O pet %s foi cadastrado com sucesso!\n", pet.getName());
        }
    }

    public static boolean isUserSearchingByCriteria() {
        System.out.print("Deseja procurar por algum critério em específico (Sim/Não)? ");
        String answer = SC.nextLine();
        while (!InputHandler.isAnswerValid(answer)) {
            System.out.print("Resposta inválida! Tente novamente: ");
            answer = SC.nextLine();
        }
        return answer.equalsIgnoreCase("sim");
    }

    public static String askForSelectedPet() {
        System.out.println();
        System.out.print("Digite o ID do pet escolhido: ");
        String petId = SC.nextLine();
        while (!InputHandler.isInteger(petId)) {
            System.out.print("ID inválido! Tente novamente: ");
            petId = SC.nextLine();
        }
        return petId;
    }

    public static boolean doubleCheck() {
        System.out.println("====== AVISO ======");
        System.out.println("Essa operação não poderá ser desfeita.");
        System.out.print("Tem certeza que deseja prosseguir (Sim/Não)? ");
        String answer = SC.nextLine();
        while (!InputHandler.isAnswerValid(answer)) {
            System.out.print("Resposta inválida! Tente novamente: ");
            answer = SC.nextLine();
        }
        return answer.equalsIgnoreCase("sim");
    }

    public static void confirmDelete() {
        System.out.println();
        System.out.println("Pet apagado com sucesso.");
    }

    public static void confirmUpdate() {
        System.out.println();
        System.out.println("Atualização feita com sucesso.");
    }

    public static int askForCriteria() {
        System.out.println();
        System.out.println("===== CRITÉRIOS DE BUSCA =====");
        System.out.println("""
                1. Nome
                2. Sexo
                3. Idade
                4. Peso
                5. Raça
                6. Endereço
                """);
        System.out.print("Digite o critério desejado: ");
        return readCriteria();
    }

    public static int readCriteria() {
        String criteria = SC.nextLine();
        while (!InputHandler.isChoiceValid(criteria)) {
            System.out.print("    Critério inválido! Tente novamente: ");
            criteria = SC.nextLine();
        }
        return Integer.parseInt(criteria);
    }

    public static int askForInfoToUpdate() {
        System.out.println();
        System.out.println("=== INFORMAÇÕES ATUALIZÁVEIS ===");
        System.out.println("""
                1. Nome
                2. Idade
                3. Peso
                4. Raça
                5. Endereço
                """);
        System.out.print("Qual sua escolha? ");
        return readInfoToBeUpdated();
    }

    public static int readInfoToBeUpdated() {
        String info = SC.nextLine();
        while (!InputHandler.isInfoChoiceValid(info)) {
            System.out.print("Escolha inválida! Tente novamente: ");
            info = SC.nextLine();
        }
        return Integer.parseInt(info);
    }

    public static String askForNewName() {
        System.out.print("Novo nome: ");
        return SC.nextLine();
    }

    public static String askForNewAge() {
        System.out.print("Nova idade: ");
        String providedAge = SC.nextLine();
        while (!InputHandler.isAgeValid(providedAge)) {
            System.out.print("    Idade inválida! Tente novamente: ");
            providedAge = SC.nextLine();
        }
        return providedAge;
    }

    public static String askForNewWeight() {
        System.out.print("Novo peso: ");
        String providedWeight = SC.nextLine();
        while (!InputHandler.isWeightValid(providedWeight)) {
            System.out.print("    Peso inválido! Tente novamente: ");
            providedWeight = SC.nextLine();
        }
        return providedWeight;
    }

    public static String askForNewRace() {
        System.out.print("Novo nome de raça: ");
        return SC.nextLine();
    }

    public static String askForNewAddress() {
        System.out.print("Novo endereço: ");
        return SC.nextLine();
    }

    public static String askNameToSearchFor() {
        System.out.print("Nome a ser procurado: ");
        return SC.nextLine();
    }

    public static String askSexToSearchFor() {
        System.out.print("Sexo a ser procurado (MACHO/FEMEA): ");
        String providedSex = SC.nextLine();
        while (!InputHandler.isSexValid(providedSex)) {
            System.out.print("    Sexo inválido! Tente novamente: ");
            providedSex = SC.nextLine();
        }
        return providedSex;
    }

    public static String askAgeToSearchFor() {
        System.out.print("Idade a ser procurada: ");
        String providedAge = SC.nextLine();
        while (!InputHandler.isAgeValid(providedAge)) {
            System.out.print("    Idade inválida! Tente novamente: ");
            providedAge = SC.nextLine();
        }
        return providedAge;
    }

    public static String askWeightToSearchFor() {
        System.out.print("Peso a ser procurada: ");
        String providedWeight = SC.nextLine();
        while (!InputHandler.isWeightValid(providedWeight)) {
            System.out.print("    Peso inválido! Tente novamente: ");
            providedWeight = SC.nextLine();
        }
        return providedWeight;
    }

    public static String askRaceToSearchFor() {
        System.out.print("Nome de raça a ser procurado: ");
        String providedRace = SC.nextLine();
        while (!InputHandler.isNameValid(providedRace)) {
            System.out.print("    Nome de raça inválido! Tente novamente: ");
            providedRace = SC.nextLine();
        }
        return providedRace;
    }

    public static String askAddressToSearchFor() {
        System.out.print("Dados do endereço a ser procurado: ");
        return SC.nextLine();
    }

    public static void showSearchResult(List<Pet> pets) {
        System.out.println();
        if (pets.isEmpty()) {
            System.out.println("Nenhum pet foi encontrado.");
        } else {
            System.out.println("==== RESULTADOS DE BUSCA ====");
            pets.forEach(
                    p -> System.out.println(
                            "ID " + p.getId() + " - "
                                    + p.getName() + " - "
                                    + p.getType() + " - "
                                    + p.getSex() + " - "
                                    + p.getAddress() + " - "
                                    + p.getAge() + " anos - "
                                    + p.getWeight() + " kg - "
                                    + p.getRace()
                    )
            );
        }
    }

    public static boolean checkSystemUsage() {
        System.out.println();
        System.out.print("Deseja continuar usando o sistema (Sim/Não)? ");
        String answer = SC.nextLine();
        while (!InputHandler.isAnswerValid(answer)) {
            System.out.print("Resposta inválida! Tente novamente: ");
            answer = SC.nextLine();
        }
        return answer.equalsIgnoreCase("sim");
    }

    public static void shuttingDown() {
        System.out.println();
        System.out.println("Encerrando o sistema...");
    }
}