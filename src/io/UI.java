package io;

import java.sql.SQLOutput;

public class UI {
    public static void mostrarMenuInicial() {
        System.out.println();
        System.out.println("========= MENU =========");
        System.out.println("""
                1. Cadastrar um novo pet
                2. Alterar os dados do pet cadastrado
                3. Deletar um pet cadastrado
                4. Listar todos os pets cadastrados
                5. Listar pets por algum critério (idade, nome, raça)
                6. Sair""");
        System.out.println("========================");
        System.out.print("Qual sua escolha? ");
    }

    public static void mostrarFormularioDeCadastro() {
        System.out.println();
        System.out.println("FORMULÁRIO DE CADASTRO");
        System.out.println("Responda às perguntas abaixo:");
    }

    public static void mostrarPergunta(String pergunta) {
        System.out.print(pergunta + " ");
    }

    public static void mostrarCriteriosDeBusca() {
        System.out.println();
        System.out.println("=== CRITÉRIOS DE BUSCA ===");
        System.out.println("""
                1. Nome
                2. Sexo
                3. Idade
                4. Peso
                5. Raça
                6. Endereço""");
        System.out.println("==========================");
        System.out.print("Selecione o critério: ");
    }

    public static void perguntarValorDoCriterio(int criterio) {
        String valorDoCriterio = null;
        switch (criterio) {
            case 1:
                valorDoCriterio = "nome";
                break;
            case 2:
                valorDoCriterio = "tipo";
                break;
            case 3:
                valorDoCriterio = "sexo";
                break;
            case 4:
                valorDoCriterio = "endereço";
                break;
            case 5:
                valorDoCriterio = "idade";
                break;
            case 6:
                valorDoCriterio = "peso";
                break;
            case 7:
                valorDoCriterio = "raça";
                break;
        }
        System.out.print("Qual " + valorDoCriterio + "? ");
    }
}
