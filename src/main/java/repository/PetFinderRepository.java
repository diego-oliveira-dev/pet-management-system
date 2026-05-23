package repository;

import connection.ConnectionFactory;
import domain.Pet;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class PetFinderRepository {

    public static List<Pet> findAll() {
        List<Pet> pets = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementToFindAll(connection);
             ResultSet rs = ps.executeQuery()) {
            PetCommandRepository.getPetList(rs, pets);
        } catch (SQLException e) {
            log.error("Error while trying to find all pets", e);
        }
        return pets;
    }

    public static PreparedStatement createPreparedStatementToFindAll(Connection c) throws SQLException {
        String sql = "SELECT * FROM pets;";
        return c.prepareStatement(sql);
    }

    public static List<Pet> findByName(String name) {
        List<Pet> pets = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementToFindByName(connection, name);
             ResultSet rs = ps.executeQuery()) {
            PetCommandRepository.getPetList(rs, pets);
        } catch (SQLException e) {
            log.error("Error while trying to find pets by the provided name", e);
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

    public static List<Pet> findBySex(String sex) {
        List<Pet> pets = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementToFindBySex(connection, sex);
             ResultSet rs = ps.executeQuery()) {
            PetCommandRepository.getPetList(rs, pets);
        } catch (SQLException e) {
            log.error("Error while trying to find pets by the provided sex", e);
        }
        return pets;
    }

    public static PreparedStatement createPreparedStatementToFindBySex(Connection c, String petSex) throws SQLException {
        String sql = "SELECT p.id, p.name, p.type, p.sex, p.address, p.age, p.weight, p.race\n" +
                "FROM pets p\n" +
                "WHERE sex like ?;";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, String.format("%%%s%%", petSex));
        return ps;
    }

    public static List<Pet> findByAge(String age) {
        List<Pet> pets = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementToFindByAge(connection, age);
             ResultSet rs = ps.executeQuery()) {
            PetCommandRepository.getPetList(rs, pets);
        } catch (SQLException e) {
            log.error("Error while trying to find pets by the provided age", e);
        }
        return pets;
    }

    public static PreparedStatement createPreparedStatementToFindByAge(Connection c, String petAge) throws SQLException {
        String sql = "SELECT p.id, p.name, p.type, p.sex, p.address, p.age, p.weight, p.race\n" +
                "FROM pets p\n" +
                "WHERE age = ?;";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, petAge);
        return ps;
    }

    public static List<Pet> findByWeight(String weight) {
        List<Pet> pets = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementToFindByWeight(connection, weight);
             ResultSet rs = ps.executeQuery()) {
            PetCommandRepository.getPetList(rs, pets);
        } catch (SQLException e) {
            log.error("Error while trying to find pets by the provided weight", e);
        }
        return pets;
    }

    public static PreparedStatement createPreparedStatementToFindByWeight(Connection c, String petWeight) throws SQLException {
        String sql = "SELECT p.id, p.name, p.type, p.sex, p.address, p.age, p.weight, p.race\n" +
                "FROM pets p\n" +
                "WHERE weight = ?;";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, petWeight);
        return ps;
    }

    public static List<Pet> findByRace(String race) {
        List<Pet> pets = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementToFindByRace(connection, race);
             ResultSet rs = ps.executeQuery()) {
            PetCommandRepository.getPetList(rs, pets);
        } catch (SQLException e) {
            log.error("Error while trying to find pets by the provided name", e);
        }
        return pets;
    }

    public static PreparedStatement createPreparedStatementToFindByRace(Connection c, String petRace) throws SQLException {
        String sql = "SELECT p.id, p.name, p.type, p.sex, p.address, p.age, p.weight, p.race\n" +
                "FROM pets p\n" +
                "WHERE race like ?;";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, String.format("%%%s%%", petRace));
        return ps;
    }

    public static List<Pet> findByAddress(String address) {
        List<Pet> pets = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementToFindByAddress(connection, address);
             ResultSet rs = ps.executeQuery()) {
            PetCommandRepository.getPetList(rs, pets);
        } catch (SQLException e) {
            log.error("Error while trying to find all pets in the database", e);
        }
        return pets;
    }

    public static PreparedStatement createPreparedStatementToFindByAddress(Connection c, String petAddress) throws SQLException {
        String sql = "SELECT p.id, p.name, p.type, p.sex, p.address, p.age, p.weight, p.race\n" +
                "FROM pets p\n" +
                "WHERE address like ?;";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, String.format("%%%s%%", petAddress));
        return ps;
    }
}
