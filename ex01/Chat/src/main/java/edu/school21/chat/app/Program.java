package edu.school21.chat.app;

import com.zaxxer.hikari.HikariDataSource;
import edu.school21.chat.models.Message;
import edu.school21.chat.repositories.MessagesRepository;
import edu.school21.chat.repositories.MessagesRepositoryJdbcImpl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.Scanner;

public class Program {
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "060601Yumzhana!";

    public static void main(String[] args) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(JDBC_URL);
        ds.setUsername(USER);
        ds.setPassword(PASSWORD);

        executeData(ds, "schema.sql");
        executeData(ds, "data.sql");
        MessagesRepository repository = new MessagesRepositoryJdbcImpl(ds);
        System.out.print("Enter a message ID\n-> ");
        Scanner in = new Scanner(System.in);

        try {
            long id = in.nextLong();
            Optional<Message> message = repository.findById(id);
            if (message.isPresent()) {
                System.out.println(message.get());
            } else {
                throw new RuntimeException("Message with this ID not found");
            }
        } catch (Exception error) {
            error.printStackTrace();
        }

        in.close();
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