package edu.school21.chat.app;

import com.zaxxer.hikari.HikariDataSource;
import edu.school21.chat.models.Chatroom;
import edu.school21.chat.models.Message;
import edu.school21.chat.models.User;
import edu.school21.chat.repositories.MessagesRepository;
import edu.school21.chat.repositories.MessagesRepositoryJdbcImpl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

        User creator = new User(1L, "user", "user", new ArrayList<>(), new ArrayList<>());
        User author = creator;
        Chatroom room = new Chatroom(1L, "room", creator, new ArrayList<>());
        Message message = new Message(null, author, room, "Hello!", LocalDateTime.now());

        MessagesRepository repository = new MessagesRepositoryJdbcImpl(ds);
        repository.save(message);

        System.out.println(message.getId());

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