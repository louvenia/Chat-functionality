package edu.school21.chat.repositories;

import edu.school21.chat.exceptions.NotSavedSubEntityException;
import edu.school21.chat.models.Chatroom;
import edu.school21.chat.models.Message;
import edu.school21.chat.models.User;

import javax.sql.DataSource;
import java.sql.*;
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

    @Override
    public void save(Message message) throws NotSavedSubEntityException {
        validate(message);
        String query = "insert into chat.message(author, room, text, dateTime) values(?, ?, ?, ?)";
        try(Connection conn = ds.getConnection();
            PreparedStatement prst = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            prst.setLong(1, message.getAuthor().getId());
            prst.setLong(2, message.getRoom().getId());
            prst.setString(3, message.getText());
            prst.setTimestamp(4, Timestamp.valueOf(message.getDateTime()));
            prst.execute();

            ResultSet rs = prst.getGeneratedKeys();
            if (rs.next()) {
                message.setId(rs.getLong(1));
            }
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    @Override
    public void update(Message message) throws NotSavedSubEntityException {
        validate(message);
        String query = "update chat.message set author = ?, room = ?, text = ?, dateTime = ? where id = " + message.getId();
        try(Connection conn = ds.getConnection();
            PreparedStatement prst = conn.prepareStatement(query, Statement.NO_GENERATED_KEYS)) {
            if(message.getAuthor() == null) {
                prst.setNull(1, Types.INTEGER);
            } else {
                prst.setLong(1, message.getAuthor().getId());
            }
            if(message.getRoom() == null) {
                prst.setNull(2, Types.INTEGER);
            } else {
                prst.setLong(2, message.getRoom().getId());
            }
            if(message.getText() == null) {
                prst.setNull(3, Types.VARCHAR);
            } else {
                prst.setString(3, message.getText());
            }
            if(message.getDateTime() == null) {
                prst.setNull(4, Types.TIMESTAMP);
            } else {
                prst.setTimestamp(4, Timestamp.valueOf(message.getDateTime()));
            }
            prst.execute();
        } catch (SQLException error) {
            error.printStackTrace();
        }
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

    private boolean validateParametersSave(Long id, String query) {
        try(Connection conn = ds.getConnection();
            Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            boolean lastRow = true;
            while(lastRow) {
                rs.next();
                if(rs.getLong(1) == id) {
                    return true;
                }
                if(rs.isLast()) {
                    lastRow = false;
                }
            }
        } catch(SQLException error) {
            error.printStackTrace();
        }
        return false;
    }

    private void validate(Message message) {
        if(message.getAuthor().getId() == null || message.getRoom().getId() == null) {
            throw new NotSavedSubEntityException("The author and room have IDs with value null");
        }
        boolean flagAuthor = validateParametersSave(message.getAuthor().getId(), "select * from chat.user");
        boolean flagRoom = validateParametersSave(message.getRoom().getId(), "select * from chat.chatroom");
        if(!flagAuthor || !flagRoom) {
            throw new NotSavedSubEntityException("The author and room do not have an ID in the assigned database");
        }
    }
}
