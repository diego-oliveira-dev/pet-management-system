package domain;

import lombok.Builder;
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

    public enum PetType {
        CACHORRO("CACHORRO"),
        GATO("GATO"),
        NULL("NAO INFORMADO");

        public final String type;
        PetType(String type) {
            this.type = type;
        }
        public String getType() {
            return type;
        }
    }

    public enum PetSex {
        MACHO("MACHO"),
        FEMEA("FEMEA"),
        NULL("NAO INFORMADO");

        public final String sex;
        PetSex(String sex) {
            this.sex = sex;
        }
        public String getSex() {
            return sex;
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
