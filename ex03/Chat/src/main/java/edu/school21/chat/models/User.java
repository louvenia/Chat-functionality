package edu.school21.chat.models;

import java.util.List;
import java.util.Objects;

public class User {
    private Long id;
    private String login;
    private String password;
    private List<Chatroom> rooms;
    private List<Chatroom> userRooms;

    public User(Long id, String login, String password, List<Chatroom> rooms, List<Chatroom> userRooms) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.rooms = rooms;
        this.userRooms = userRooms;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Chatroom> getRooms() {
        return this.rooms;
    }

    public void setRooms(List<Chatroom> rooms) {
        this.rooms = rooms;
    }

    public List<Chatroom> getUserRooms() {
        return this.userRooms;
    }

    public void setUserRooms(List<Chatroom> userRooms) {
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
        return "{id = " + this.id + ", login = \"" + this.login + "\", password = \"" + this.password
                + "\", createdRooms = " + this.rooms + ", rooms = " + this.userRooms + "}";
    }
}
