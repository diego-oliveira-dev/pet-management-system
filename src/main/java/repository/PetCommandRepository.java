package repository;

import connection.ConnectionFactory;
import lombok.extern.log4j.Log4j2;
import domain.Pet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public static PreparedStatement createPreparedStatementToSave
            (Connection c, Pet pet) throws SQLException {
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

    public static void updateName(String id, String newName) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementToUpdateName(connection, id, newName)) {
            ps.execute();
        } catch (SQLException e) {
            log.error("Error while trying to update pet name from database", e);
        }
    }

    public static PreparedStatement createPreparedStatementToUpdateName
            (Connection c, String id, String newName) throws SQLException {
        String sql = "UPDATE `estudos_java`.`pets` SET `name` = ? WHERE (`id` = ?);";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, newName);
        ps.setString(2, id);
        return ps;
    }

    public static void updateAge(String id, String newAge) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementToUpdateAge(connection, id, newAge)) {
            ps.execute();
        } catch (SQLException e) {
            log.error("Error while trying to update pet name from database", e);
        }
    }

    public static PreparedStatement createPreparedStatementToUpdateAge
            (Connection c, String id, String newAge) throws SQLException {
        String sql = "UPDATE `estudos_java`.`pets` SET `age` = ? WHERE (`id` = ?);";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, newAge);
        ps.setString(2, id);
        return ps;
    }

    public static void updateWeight(String id, String newWeight) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementToUpdateWeight(connection, id, newWeight)) {
            ps.execute();
        } catch (SQLException e) {
            log.error("Error while trying to update pet name from database", e);
        }
    }

    public static PreparedStatement createPreparedStatementToUpdateWeight
            (Connection c, String id, String newWeight) throws SQLException {
        String sql = "UPDATE `estudos_java`.`pets` SET `weight` = ? WHERE (`id` = ?);";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, newWeight);
        ps.setString(2, id);
        return ps;
    }

    public static void updateRace(String id, String newRace) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementToUpdateRace(connection, id, newRace)) {
            ps.execute();
        } catch (SQLException e) {
            log.error("Error while trying to update pet name from database", e);
        }
    }

    public static PreparedStatement createPreparedStatementToUpdateRace
            (Connection c, String id, String newRace) throws SQLException {
        String sql = "UPDATE `estudos_java`.`pets` SET `race` = ? WHERE (`id` = ?);";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, newRace);
        ps.setString(2, id);
        return ps;
    }

    public static void updateAddress(String id, String newAddress) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementToUpdateAddress(connection, id, newAddress)) {
            ps.execute();
        } catch (SQLException e) {
            log.error("Error while trying to update pet name from database", e);
        }
    }

    public static PreparedStatement createPreparedStatementToUpdateAddress
            (Connection c, String id, String newAddress) throws SQLException {
        String sql = "UPDATE `estudos_java`.`pets` SET `address` = ? WHERE (`id` = ?);";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, newAddress);
        ps.setString(2, id);
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

    public static PreparedStatement createPreparedStatementToDelete
            (Connection c, String petId) throws SQLException {
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

