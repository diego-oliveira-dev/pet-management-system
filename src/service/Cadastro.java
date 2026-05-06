package service;

import entities.Pet;
import entities.PetSexo;
import entities.PetTipo;
import io.Leitor;
import io.ManipuladorDeArquivos;
import io.UI;
import util.Validador;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;

public class Cadastro {

    public static void cadastrarPet (Leitor leitor) {
        Pet pet = new Pet();
        UI.mostrarFormularioDeCadastro();
        List<String> perguntas = ManipuladorDeArquivos.lerFormularioDeCadastro();
        List<Consumer<String>> acoes = List.of(
                r -> pet.setNomeCompleto(Validador.validarNomeCompleto(r)),
                r -> pet.setTipo(PetTipo.valueOf(r.toUpperCase())),
                r -> pet.setSexo(PetSexo.valueOf(r.toUpperCase())),
                pet::setEndereco,
                r -> pet.setIdade(Validador.validarIdade(r)),
                r -> pet.setPeso(Validador.validarPeso(r)),
                r -> pet.setRaca(Validador.validarNome(r))
        );
        for (int i = 0; i < perguntas.size(); i++) {
            UI.mostrarPergunta(perguntas.get(i));
            String resposta = leitor.lerResposta();
            acoes.get(i).accept(resposta);
        }
        salvarInfoEmArquivo(pet);
    }

    public static void salvarInfoEmArquivo(Pet pet) {
        List<String> dadosDoPet = List.of(
                pet.getNomeCompleto(),
                pet.getTipo().toString(),
                pet.getSexo().toString(),
                pet.getEndereco(),
                pet.getIdade() + " anos",
                pet.getPeso() + " kg",
                pet.getRaca()
        );
        File file = ManipuladorDeArquivos.criarArquivo(pet);
        boolean arquivoSalvo = ManipuladorDeArquivos.salvarDadosNoArquivo(file, dadosDoPet);
        if (arquivoSalvo) {
            System.out.println("Pet cadastrado com sucesso!");
        } else {
            System.out.println("Ocorreu um erro ao cadastrar.");
        }
    }
}
