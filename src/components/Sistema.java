package components;

import java.util.Scanner;

public class Sistema {
    public static void mostrarMenu() {
        System.out.println("========= MENU =========");
        System.out.println("""
                1. Cadastrar um novo pet
                2. Alterar os dados do pet cadastrado
                3. Deletar um pet cadastrado
                4. Listar todos os pets cadastrados
                5. Listar pets por algum critério (idade, nome, raça)
                6. Sair""");
        System.out.println("========================");
        System.out.print("Qual sua escolha? ");
    }

    public static void processarEscolha(String escolha, Scanner sc) {
        while (!Validador.isEscolhaValida(escolha)) {
            System.out.println();
            System.out.println("Escolha inválida! Tente novamente.");
            Sistema.mostrarMenu();
            escolha = sc.nextLine();
        }
        int escolhaInt = Integer.parseInt(escolha);
        switch (escolhaInt) {
            case 1:
                Cadastro.coletarInfo(sc);
                break;
            case 2:
                System.out.println("Alterando dados de pet cadastrado...");
                break;
            case 3:
                System.out.println("Deletando pet cadastrado...");
                break;
            case 4:
                System.out.println("Listando pets cadastrados...");
                break;
            case 5:
                System.out.println("Listando pets cadastrados por critério..");
                break;
            case 6:
                System.out.println("Encerrando o sistema...");
                break;
        }
    }
}
