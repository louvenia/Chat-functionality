package edu.school21.chat.app;

import com.zaxxer.hikari.HikariDataSource;
import edu.school21.chat.models.User;
import edu.school21.chat.repositories.UsersRepository;
import edu.school21.chat.repositories.UsersRepositoryJdbcImpl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Program {
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "password";

    public static void main(String[] args) throws SQLException {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(JDBC_URL);
        ds.setUsername(USER);
        ds.setPassword(PASSWORD);

        executeData(ds, "schema.sql");
        executeData(ds, "data.sql");

        UsersRepository usersRepository = new UsersRepositoryJdbcImpl(ds);
        List<User> users = usersRepository.findAll(0, 2);
        for(User user : users) {
            System.out.println(user);
        }
        ds.close();
    }

    private static void executeData(HikariDataSource dataSource, String file) {
        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {
            InputStream input = Program.class.getClassLoader().getResourceAsStream(file);
            Optional<InputStream> in = Optional.ofNullable(input);
            if(in.isPresent()) {
                Scanner scanner = new Scanner(input).useDelimiter(";");
                while (scanner.hasNext()) {
                    stmt.executeUpdate(scanner.next());
                }
                scanner.close();
                input.close();
            }
        } catch (SQLException | IOException error) {
            error.printStackTrace();
        }
    }
}