package repository;

import connection.ConnectionFactory;
import lombok.extern.log4j.Log4j2;
import domain.Pet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class PetRepository {
    public static void save(Pet pet) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementToSave(connection, pet)) {
            ps.execute();
        } catch (SQLException e) {
            log.error("Error while trying to save the pet '{}' into the database", pet.getName(), e);
        }
    }

    public static PreparedStatement createPreparedStatementToSave(Connection c, Pet pet) throws SQLException {
        String sql = "INSERT INTO `estudos_java`.`pets` " +
                "(`name`, `type`, `sex`, `address`, `age`, `weight`, `race`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, pet.getName());
        ps.setString(2, pet.getType().type);
        ps.setString(3, pet.getSex().sex);
        ps.setString(4, pet.getAddress());
        ps.setInt(5, pet.getAge());
        ps.setDouble(6, pet.getWeight());
        ps.setString(7, pet.getRace());
        return ps;
    }

    public static List<Pet> findByName(String name) {
        List<Pet> pets = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementToFindByName(connection, name);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Pet pet = Pet.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .type(fromType(rs.getString("type")))
                        .sex(fromSex(rs.getString("sex")))
                        .address(rs.getString("address"))
                        .age(rs.getInt("age"))
                        .weight(rs.getDouble("weight"))
                        .race(rs.getString("race"))
                        .build();
                pets.add(pet);
            }
        } catch (SQLException e) {
            log.error("Error while trying to find all pets in the database", e);
        }
        return pets;
    }

    public static PreparedStatement createPreparedStatementToFindByName(Connection c, String petName) throws SQLException {
        String sql = "SELECT p.id, p.name, p.type, p.sex, p.address, p.age, p.weight, p.race\n" +
                "FROM pets p\n" +
                "WHERE name like ?;";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, String.format("%%%s%%", petName));
        return ps;
    }

    public static Pet.PetType fromType(String value) {
        for (Pet.PetType type : Pet.PetType.values()) {
            if (type.getType().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Tipo inválido!");
    }

    public static Pet.PetSex fromSex(String value) {
        for (Pet.PetSex sex : Pet.PetSex.values()) {
            if (sex.getSex().equalsIgnoreCase(value)) {
                return sex;
            }
        }
        throw new IllegalArgumentException("Sexo inválido!");
    }
}
