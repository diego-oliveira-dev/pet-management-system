package service;

import io.ManipuladorDeArquivos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Busca {

    public static void listarPetsCadastrados() {
        File[] arquivos = ManipuladorDeArquivos.coletarDadosCadastrados();
        List<String> lista = new ArrayList<>();
        for (int i = 0; i < arquivos.length; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append((i + 1) + ". ");
            String item = ManipuladorDeArquivos.coletarItemDaLista(arquivos[i], sb);
            lista.add(item);
        }
        System.out.println();
        lista.forEach(System.out::println);
    }

    public static List<String> listarPetsPorCriterio(File[] arquivos, String valorDoCriterio, String k) {
        List<String> lista = new ArrayList<>();
        int j = 0;
        for (int i = 0; i < arquivos.length; i++) {
            try (BufferedReader br = new BufferedReader(new FileReader(arquivos[i]))) {
                String line = br.readLine();
                while (line != null) {
                    String[] campos = line.split(" - ");
                    Pattern pattern = Pattern.compile(valorDoCriterio);
                    Matcher matcher = pattern.matcher(campos[1]);
                    boolean encontrou = matcher.find();
                    if (campos[0].equals(k)
                            && encontrou) {
                        StringBuilder sb = new StringBuilder();
                        sb.append((j + 1) + ". ");
                        String item = ManipuladorDeArquivos.coletarItemDaLista(arquivos[i], sb);
                        lista.add(item);
                        j++;
                    }
                    line = br.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
        lista.forEach(System.out::println);
        return lista;
    }
}
