package components.menu;

import components.Pet;
import components.Validador;
import enums.PetSexo;
import enums.PetTipo;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;

public class Cadastro {

    public static void cadastrarPet (Scanner sc) {
        System.out.println();
        System.out.println("FORMULÁRIO DE CADASTRO");

        Pet pet = new Pet();
        List<String> perguntas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("formulario.txt"))) {
            String line = br.readLine();
            while (line != null) {
                perguntas.add(line);
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Consumer<String>> acoes = List.of(
                r -> pet.setNomeCompleto(Validador.validarNomeCompleto(r)),
                r -> pet.setTipo(PetTipo.valueOf(r.toUpperCase())),
                r -> pet.setSexo(PetSexo.valueOf(r.toUpperCase())),
                pet::setEndereco,
                r -> pet.setIdade(Validador.validarIdade(r)),
                r -> pet.setPeso(Validador.validarPeso(r)),
                r -> pet.setRaca(Validador.validarNome(r))
        );

        System.out.println("Responda às perguntas abaixo:");
        System.out.println();

        for (int i = 0; i < perguntas.size(); i++) {
            System.out.print(perguntas.get(i) + " ");
            String resposta = sc.nextLine();
            acoes.get(i).accept(resposta);
        }

        if (Cadastro.criarArquivo(pet)) {
            System.out.println();
            System.out.println("O pet foi cadastrado com sucesso! Verifique as informações abaixo.");
            System.out.println();
            System.out.println(pet);
        } else {
            System.out.println("Ocorreu um erro ao cadastrar seu pet.");
        }
    }

    public static boolean criarArquivo(Pet pet) {
        Number peso;
        if (pet.getPeso() % 1 == 0) {
            peso = (int) pet.getPeso();
        } else {
            peso = pet.getPeso();
        }

        List<String> dados = List.of(
                pet.getNomeCompleto(),
                pet.getTipo().toString(),
                pet.getSexo().toString(),
                pet.getEndereco(),
                pet.getIdade() + " anos",
                peso + "kg",
                pet.getRaca()
        );

        File directory = new File("src/petsCadastrados");
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
            for (int i = 0; i < dados.size(); i++) {
                bw.write((i + 1) + " - " + dados.get(i));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
