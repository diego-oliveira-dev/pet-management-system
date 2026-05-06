package io;

import util.Validador;

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
}
