import java.util.Scanner;

public class Sistema {
    public static void mostrarMenu() {
        System.out.println("========= MENU =========");
        System.out.println("1. Cadastrar um novo pet\n" +
                "2. Alterar os dados do pet cadastrado\n" +
                "3. Deletar um pet cadastrado\n" +
                "4. Listar todos os pets cadastrados\n" +
                "5. Listar pets por algum critério (idade, nome, raça)\n" +
                "6. Sair");
        System.out.println("========================");
        System.out.print("Qual sua escolha? ");
    }

    public static void processarEscolha(String escolha) {
        Scanner sc = new Scanner(System.in);

        while (!Validador.isEscolhaValida(escolha)) {
            System.out.println();
            System.out.println("Escolha inválida! Tente novamente.");
            Sistema.mostrarMenu();
            escolha = sc.nextLine();
        }

        System.out.println();
        System.out.println("Escolha processada!");
    }
}
