package components;

import enums.PetSexo;
import enums.PetTipo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class Processador {

    public static void cadastrarPet() {
        System.out.println();
        System.out.println("FORMULÁRIO DE CADASTRO");

        Pet pet = new Pet();
        Scanner sc = new Scanner(System.in);

        Map<String, Consumer<String>> map = new HashMap<>();

        map.put("1", r -> pet.setNomeCompleto(Validador.validarNome(r)));
        map.put("2", r -> pet.setTipo(PetTipo.valueOf(r.toUpperCase())));
        map.put("3", r -> pet.setSexo(PetSexo.valueOf(r.toUpperCase())));
        map.put("4", pet::setEndereco);
        map.put("5", r -> pet.setIdade(Validador.validarIdade(r.replace(",", "."))));
        map.put("6", r -> pet.setPeso(Validador.validarPeso(r.replace(",", "."))));
        map.put("7", r -> pet.setRaca(Validador.validarNome(r)));

        System.out.println("Responda às perguntas abaixo:");

        try (BufferedReader br = new BufferedReader(new FileReader("formulario.txt"))) {
            String line = br.readLine();

            while (line != null) {
                System.out.print(line + " ");
                String resposta = sc.nextLine();

                // vai receber o numero da pergunta
                String chave = line.split(" - ")[0];

                Consumer<String> acao = map.get(chave);

                if (acao != null) {
                    acao.accept(resposta);
                }

                line = br.readLine();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        System.out.println();
        System.out.println("O pet foi cadastrado com sucesso! Verifique as informações abaixo.");
        System.out.println(pet);
    }
}
