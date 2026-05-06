package util;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validador {

    public static boolean isInputValido(String escolha) {
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
        String regex = "[^a-zA-ZÀ-ÿ\\s\\-]";
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
            peso = Double.parseDouble(pesoString.replace(",", "."));
        } catch (Exception e) {
            throw new IllegalArgumentException("Peso inválido!");
        }
        if (peso > 60) {
            throw new IllegalArgumentException("Peso inválido!");
        }
        return peso;
    }

    public static boolean isAlgumPetCadastrado(File[] arquivos) {
        if (arquivos == null || arquivos.length == 0) {
            return false;
        }
        return true;
    }

    public static boolean isCriterioValido(String criterio) {
        // caso 1: apenas um criterio
        int criterioInt;
        try {
            criterioInt = Integer.parseInt(criterio);
        } catch (Exception e) {
            return false;
        }
        return criterioInt >= 1 && criterioInt <= 7;
        // caso 2: dois criterios
//        if (criterios.length == 2) {
//            int criterio1, criterio2;
//            try {
//                criterio1 = Integer.parseInt(criterios[0]);
//                criterio2 = Integer.parseInt(criterios[1]);
//            } catch (Exception e) {
//                return false;
//            }
//            return (criterio1 >= 1 && criterio1 <= 6)
//                    && (criterio2 >= 1 && criterio2 <= 6);
//        }
    }

    public static boolean isDoisCriterios(String resposta) {
        return resposta.split(" E ").length == 2;
    }

    public static boolean isPetEscolhidoValido(String escolha, List<String> lista) {
        int escolhaInt;
        try {
            escolhaInt = Integer.parseInt(escolha);
        } catch (Exception e) {
            return false;
        }
        if (escolhaInt < 1 || escolhaInt > lista.size()) {
            return false;
        }
        return true;
    }

    public static boolean isDadoValido(String escolha) {
        int escolhaInt;
        try {
            escolhaInt = Integer.parseInt(escolha);
        } catch (Exception e) {
            return false;
        }
        if (escolhaInt < 1 || escolhaInt > 5) {
            return false;
        }
        return true;
    }
}
