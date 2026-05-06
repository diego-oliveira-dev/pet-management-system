package service;

import io.Leitor;
import io.UI;

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
                Cadastro.alterarDados(leitor);
                //Modificador.alterarDadosCadastrais(leitor);
                System.out.println("Alterando dados de pet cadastrado...");
                break;
            case 3:
                System.out.println("Deletando pet cadastrado...");
                break;
            case 4:
                System.out.println("Listando pets cadastrados...");
                Busca.listarPets();
                break;
            case 5:
                System.out.println("Listando pets cadastrados por critério..");
                //Busca.listarPetsPorCriterio();
                break;
            case 6:
                System.out.println("Encerrando o sistema...");
                break;
        }
    }
}
