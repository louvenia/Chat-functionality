package edu.school21.chat.models;

import java.util.List;
import java.util.Objects;

public class User {
    private final long id;
    private final String login;
    private final String password;
    private final List<Chatroom> rooms;
    private final List<Chatroom> userRooms;

    public User(long id, String login, String password, List<Chatroom> rooms, List<Chatroom> userRooms) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.rooms = rooms;
        this.userRooms = userRooms;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || this.getClass() != o.getClass()) return false;
        User u = (User) o;
        return this.id == u.id && Objects.equals(this.login, u.login) && Objects.equals(this.password, u.password) &&
                Objects.equals(this.rooms, u.rooms) && Objects.equals(this.userRooms, u.userRooms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, rooms, userRooms);
    }

    @Override
    public String toString() {
        return "User =\n" +
                "id = " + this.id +
                ", login = " + this.login +
                ", password = " + this.password +
                ", rooms = " + this.rooms +
                ", userRooms = " + this.userRooms;
    }
}
