package edu.school21.chat.repositories;

import edu.school21.chat.models.Chatroom;
import edu.school21.chat.models.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsersRepositoryJdbcImpl implements UsersRepository {
    private final DataSource ds;

    public UsersRepositoryJdbcImpl(DataSource ds) {
        this.ds = ds;
    }

    public List<User> findAll(int page, int size) {
        List<User> listUser = new ArrayList<>();
        String query = "with room as (\n" +
                            "select u.id,\n" +
                                    "string_agg(cast(ch.id as varchar), ' ') as r_id,\n" +
                                    "string_agg(ch.name, ' ') as r_name\n" +
                            "from chat.user as u\n" +
                                "left join chat.chatroom as ch on ch.owner = u.id\n" +
                            "group by 1\n" +
                            "limit ? offset ?\n" +
                        "),user_room as (\n" +
                            "select u.id,\n" +
                                "string_agg(cast(m.room as varchar), ' ') as ur_id,\n" +
                                "string_agg(r.name, ' ') as ur_name\n" +
                            "from chat.user as u\n" +
                                "left join chat.message as m on m.author = u.id\n" +
                                "left join chat.chatroom as r on r.id = m.room\n" +
                            "group by 1\n" +
                        ")\n" +
                        "select u.id,\n" +
                                "u.login,\n" +
                                "u.password,\n" +
                                "r_id,\n" +
                                "r_name,\n" +
                                "ur_id,\n" +
                                "ur_name\n" +
                        "from room as r\n" +
                            "join user_room as ur on ur.id = r.id\n" +
                            "join chat.user as u on u.id = r.id";
        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, size);
            stmt.setInt(2, page * size);

            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                List<Chatroom> rooms = new ArrayList<>();
                List<Chatroom> userRooms = new ArrayList<>();

                getDataRooms(rs, rooms, "r_id", "r_name");
                getDataRooms(rs, userRooms, "ur_id", "ur_name");

                User user = new User(rs.getLong("id"), rs.getString("login"), rs.getString("password"), rooms, userRooms);
                listUser.add(user);
            }
        } catch(SQLException error) {
            error.printStackTrace();
        }

        return listUser;
    }

    private void getDataRooms(ResultSet rs, List<Chatroom> rooms, String id, String name) throws SQLException {
        String[] rId = rs.getString(id).split("\\s");
        String[] rName = rs.getString(name).split("\\s");

        for(int i = 0; i < rId.length; i++) {
            Chatroom r = new Chatroom(Long.parseLong(rId[i]), rName[i], null, null);
            rooms.add(r);
        }
    }
}
