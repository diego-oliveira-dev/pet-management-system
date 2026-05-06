package service;

import io.Leitor;
import io.ManipuladorDeArquivos;
import io.UI;

import java.io.File;

public class Sistema {
    public Leitor leitor = new Leitor();

    public void iniciar() {
        UI.mostrarMenuInicial();
        int escolha = leitor.lerInputInicial();
        processarEscolha(escolha);
    }

    public void processarEscolha(int escolha) {
        switch (escolha) {
            case 1:
                Cadastro.cadastrarPet(leitor);
                break;
            case 2:
                processarBuscaPorCriterio(leitor);
                // Cadastro.alterarDados(leitor);
                System.out.println("Alterando dados de pet cadastrado...");
                break;
            case 3:
                System.out.println("Deletando pet cadastrado...");
                break;
            case 4:
                Busca.listarPetsCadastrados();
                break;
            case 5:
                processarBuscaPorCriterio(leitor);
                break;
            case 6:
                System.out.println("Encerrando o sistema...");
                break;
        }
    }

    public static void processarBuscaPorCriterio(Leitor leitor) {
        UI.mostrarCriteriosDeBusca();
        int criterioEscolhido = leitor.lerCriterio();
        UI.perguntarValorDoCriterio(criterioEscolhido);
        String valorDoCriterio = leitor.lerResposta();
        File[] arquivos = ManipuladorDeArquivos.coletarDadosCadastrados();
        Busca.listarPetsPorCriterio(
                arquivos,
                valorDoCriterio,
                String.valueOf(criterioEscolhido)
        );
    }
}
