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
public class PetCommandRepository {
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

    public static void delete(String id) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementToDelete(connection, id)) {
            ps.execute();
        } catch (SQLException e) {
            log.error("Error while trying to delete pet from database", e);
        }
    }

    public static PreparedStatement createPreparedStatementToDelete(Connection c, String petId) throws SQLException {
        String sql = "DELETE FROM `estudos_java`.`pets` WHERE (`id` = ?);";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, petId);
        return ps;
    }

    public static void getPetList(ResultSet rs, List<Pet> pets) throws SQLException {
        while (rs.next()) {
            Pet pet = Pet.builder()
                    .id(rs.getInt("id"))
                    .name(rs.getString("name"))
                    .type(Pet.PetType.fromType(rs.getString("type")))
                    .sex(Pet.PetSex.fromSex(rs.getString("sex")))
                    .address(rs.getString("address"))
                    .age(rs.getInt("age"))
                    .weight(rs.getDouble("weight"))
                    .race(rs.getString("race"))
                    .build();
            pets.add(pet);
        }
    }
}

