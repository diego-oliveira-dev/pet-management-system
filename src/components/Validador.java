package components;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validador {

    public static boolean isEscolhaValida(String escolha) {
        int escolhaInt;
        try {
            escolhaInt = Integer.parseInt(escolha);
        } catch (Exception e) {
            return false;
        }
        if (escolhaInt < 1 || escolhaInt > 6) {
            return false;
        }
        return true;
    }

    public static String validarNome(String nome) {
        String[] campos = nome.trim().split("\\s+");
        String regex = "[^a-zA-ZÀ-ÿ\\s]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(nome.trim());

        if (matcher.find()) {
            throw new IllegalArgumentException("Nome inválido!");
        }
        if (nome.isBlank()) {
            return "NÃO INFORMADO";
        }
        return nome;
    }

    public static String validarNomeCompleto(String nome) {
        String nomeValido = Validador.validarNome(nome);
        String[] campos = nomeValido.split(" ");

        if (campos.length <= 1) {
            throw new IllegalArgumentException("Nome inválido!");
        }

        return nomeValido;
    }

    public static int validarIdade(String idadeString) {
        int idade;
        try {
            idade = Integer.parseInt(idadeString);
        } catch (Exception e) {
            throw new IllegalArgumentException("Idade inválida!");
        }
        if (idade <= 0 || idade > 20) {
            throw new IllegalArgumentException("Idade inválida!");
        }
        return idade;
    }

    public static double validarPeso(String pesoString) {
        double peso;
        try {
            peso = Double.parseDouble(pesoString);
        } catch (Exception e) {
            throw new IllegalArgumentException("Peso inválida!");
        }

        if (peso > 60) {
            throw new IllegalArgumentException("Peso inválida!");
        }

        return peso;
    }

}
