package io;

import entities.Pet;
import entities.PetSexo;
import entities.PetTipo;
import util.Validador;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ManipuladorDeArquivos {
    public static List<String> lerFormularioDeCadastro() {
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
        return perguntas;
    }

    public static File criarArquivo(Pet pet) {
        File directory = new File("dados/petsCadastrados");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'-HHmm");
        LocalDateTime now = LocalDateTime.now();
        String formattedTime = now.format(formatter);
        String formattedName = pet.getNomeCompleto().replaceAll("(\\s)", "").toUpperCase();
        String fileName = formattedTime + formattedName + ".TXT";
        return new File(directory, fileName);
    }

    public static boolean salvarDadosNoArquivo(File file, List<String> dados) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
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

    public static File[] coletarDadosCadastrados() {
        File diretorio = new File("dados/petsCadastrados");
        File[] arquivos = diretorio.listFiles();
        if (!Validador.isAlgumPetCadastrado(arquivos)) {
            throw new IllegalStateException("Nenhum pet cadastrado.");
        }
        return arquivos;
    }

    public static String coletarItemDaLista(File arquivo, StringBuilder sb) {
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String line = br.readLine();
            int j = 0;
            while (line != null) {
                String data = line.split(" - ")[1];
                if (j == 0) {
                    sb.append(data);
                } else {
                    sb.append(" - " + data);
                }
                line = br.readLine();
                j++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static Pet lerPetDoArquivo(File file) {
        Pet pet = new Pet();
        List<Consumer<String>> acoes = List.of(
                r -> pet.setNomeCompleto(Validador.validarNomeCompleto(r)),
                r -> pet.setTipo(PetTipo.valueOf(r.toUpperCase())),
                r -> pet.setSexo(PetSexo.valueOf(r.toUpperCase())),
                pet::setEndereco,
                r -> pet.setIdade(Validador.validarIdade(r.split(" ")[0])),
                r -> pet.setPeso(Validador.validarPeso(r.split(" ")[0])),
                r -> pet.setRaca(Validador.validarNome(r))
        );
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            int i = 0;
            while (line != null) {
                String valor = line.split(" - ")[1];
                acoes.get(i).accept(valor);
                i++;
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pet;
    }
}
