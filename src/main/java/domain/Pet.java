package domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Value
@Builder
public class Pet {
    private int id;
    private String name;
    private PetType type;
    private PetSex sex;
    private String address;
    private int age;
    private double weight;
    private String race;

    @Getter
    public enum PetType {
        CACHORRO("CACHORRO"),
        GATO("GATO"),
        NULL("NAO INFORMADO");

        public final String type;

        PetType(String type) {
            this.type = type;
        }

        public static PetType fromType(String value) {
            for (Pet.PetType type : Pet.PetType.values()) {
                if (type.getType().equalsIgnoreCase(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Tipo inválido!");
        }
    }

    @Getter
    public enum PetSex {
        MACHO("MACHO"),
        FEMEA("FEMEA"),
        NULL("NAO INFORMADO");

        public final String sex;

        PetSex(String sex) {
            this.sex = sex;
        }

        public static PetSex fromSex(String value) {
            for (Pet.PetSex sex : Pet.PetSex.values()) {
                if (sex.getSex().equalsIgnoreCase(value)) {
                    return sex;
                }
            }
            throw new IllegalArgumentException("Sexo inválido!");
        }
    }

    @Override
    public String toString() {
        return getId() + " - "
                + getName() + " - "
                + getType() + " - "
                + getSex() + " - "
                + getAddress() + " - "
                + getAge() + " - "
                + getWeight() + " - "
                + getRace() + " - ";
    }
}
