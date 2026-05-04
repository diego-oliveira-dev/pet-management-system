package components;

import enums.PetSexo;
import enums.PetTipo;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class Cadastro {

    public static void coletarInfo () {
        System.out.println();
        System.out.println("FORMULÁRIO DE CADASTRO");

        Pet pet = new Pet();
        Scanner sc = new Scanner(System.in);

        Map<String, Consumer<String>> map = new HashMap<>();

        map.put("1", r -> pet.setNomeCompleto(Validador.validarNomeCompleto(r)));
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
        if (Cadastro.criarArquivo(pet)) {
            System.out.println("O pet foi cadastrado com sucesso! Verifique as informações abaixo.");
            System.out.println(pet);
        } else {
            System.out.println("Ocorreu um erro ao cadastrar seu pet.");
        }
    }

    public static boolean criarArquivo(Pet pet) {
        File directory = new File("src/petsRegistrados");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'hhmm");
        LocalDateTime now = LocalDateTime.now();
        String formattedTime = now.format(formatter);
        String formattedName = pet.getNomeCompleto().replaceAll("(\\s)", "").toUpperCase();
        String fileName = formattedTime + formattedName + ".TXT";

        File file = new File(directory, fileName);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write("1 - " + pet.getNomeCompleto());
            bw.newLine();
            bw.write("2 - " + pet.getTipo());
            bw.newLine();
            bw.write("3 - " + pet.getSexo());
            bw.newLine();
            bw.write("4 - " + pet.getEndereco());
            bw.newLine();
            bw.write("5 - " + pet.getIdade());
            bw.newLine();
            bw.write("6 - " + pet.getPeso());
            bw.newLine();
            bw.write("7 - " + pet.getRaca());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        System.out.println(directory.exists());
        System.out.println(file.getAbsolutePath());
        return true;
    }
}
