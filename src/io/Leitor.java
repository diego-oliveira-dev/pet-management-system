package io;

import util.Validador;

import java.util.List;
import java.util.Scanner;

public class Leitor {
    public final Scanner SC = new Scanner(System.in);

    public String lerResposta() {
        return SC.nextLine();
    }

    public int lerInputInicial() {
        String resposta = SC.nextLine();
        while (!Validador.isInputValido(resposta)) {
            System.out.println();
            System.out.print("Escolha inválida! Tente novamente: ");
            resposta = SC.nextLine();
        }
        return Integer.parseInt(resposta);
    }

    public int lerCriterio() {
        String resposta = SC.nextLine();
        while (!Validador.isCriterioValido(resposta)) {
            System.out.println();
            System.out.print("Critério inválido! Tente novamente: ");
            resposta = SC.nextLine();
        }
        return Integer.parseInt(resposta);
    }

    public int lerPetEscolhido(List<String> lista) {
        String resposta = SC.nextLine();
        while(!Validador.isPetEscolhidoValido(resposta, lista)) {
            System.out.println();
            System.out.print("Input inválido! Tente novamente: ");
            resposta = SC.nextLine();
        }
        return Integer.parseInt(resposta);
    }

    public int lerDadoASerAlterado() {
        String resposta = SC.nextLine();
        while(!Validador.isDadoValido(resposta)) {
            System.out.println();
            System.out.print("Dado inválido! Tente novamente: ");
            resposta = SC.nextLine();
        }
        return Integer.parseInt(resposta);
    }
}
