package edu.school21.chat.repositories;

import edu.school21.chat.models.Chatroom;
import edu.school21.chat.models.Message;
import edu.school21.chat.models.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class MessagesRepositoryJdbcImpl implements MessagesRepository {
    private final DataSource ds;

    public MessagesRepositoryJdbcImpl(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public Optional<Message> findById(Long id) {
        String query = "select * from chat.message where id = " + id;

        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                User user = getResultUser("select * from chat.user where id = " + rs.getLong(2));
                Chatroom room = getResultChatroom("select * from chat.chatroom where id = " + rs.getLong(3));
                return Optional.of(new Message(rs.getLong(1), user, room,
                        rs.getString(4), rs.getTimestamp(5).toLocalDateTime()));
            }
        } catch (SQLException error) {
            error.printStackTrace();
        }

        return Optional.empty();
    }

    private User getResultUser(String query) {
        try(Connection conn = ds.getConnection();
            Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return new User(rs.getLong(1), rs.getString(2), rs.getString(3), null, null);
            }
        } catch (SQLException error) {
            error.printStackTrace();
        }

        return null;
    }

    private Chatroom getResultChatroom(String query) {
        try(Connection conn = ds.getConnection();
            Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return new Chatroom(rs.getLong(1), rs.getString(2), null, null);
            }
        } catch(SQLException error) {
            error.printStackTrace();
        }

        return null;
    }
}
