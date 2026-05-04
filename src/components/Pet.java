package components;

import enums.PetSexo;
import enums.PetTipo;

public class Pet {
    private String nomeCompleto;
    private PetTipo tipo;
    private PetSexo sexo;
    private String endereco;
    private int idade;
    private double peso;
    private String raca;

    public Pet() {

    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public void setTipo(PetTipo tipo) {
        this.tipo = tipo;
    }

    public void setSexo(PetSexo sexo) {
        this.sexo = sexo;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Nome: " + nomeCompleto + "\n");
        sb.append("Tipo: " + tipo + "\n");
        sb.append("Sexo: " + sexo + "\n");
        sb.append("Endereço encontrado: " + endereco + "\n");
        sb.append("Idade (em anos): " + idade + "\n");
        sb.append("Peso (em kg): " + peso + "\n");
        sb.append("Raça: " + raca);

        return sb.toString();
    }
}
