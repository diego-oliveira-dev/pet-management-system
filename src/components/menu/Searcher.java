package components.menu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Searcher {
    public static void searchPets(Scanner sc) {
//        System.out.println("Critérios de busca:");
//        System.out.println("1 - Nome ou sobrenome");
//        System.out.println("2 - Sexo");
//        System.out.println("3 - Idade");
//        System.out.println("4 - Peso");
//        System.out.println("5 - Raça");
//        System.out.println("6 - Endereço");
//        System.out.print("Escolha o critério desejado: ");
//
//        int escolha = sc.nextInt();
//
//        while (!Validador.isEscolhaValida(String.valueOf(escolha))) {
//            System.out.println();
//            System.out.println("Escolha inválida! Tente novamente.");
//            Sistema.mostrarMenu();
//            escolha = sc.nextInt();
//        }

        File directory = new File("src/petsCadastrados");
        File[] files = directory.listFiles();

        if (files == null || files.length == 0) {
            System.out.println("Nenhum pet cadastrado");
        }

        // em cada arquivo
        for (int i = 0; i < files.length; i++) {

            StringBuilder sb = new StringBuilder();
            sb.append((i + 1) + ". ");

            try (BufferedReader br = new BufferedReader(new FileReader(files[i]))) {
                String line = br.readLine();
                int j = 0;

                while (line != null) {
                    String value = line.split(" - ")[1];

                    if (j == 0) {
                        sb.append(value);
                    } else {
                        sb.append(" - " + value);
                    }

                    line = br.readLine();
                    j++;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(sb);
        }
    }
}
