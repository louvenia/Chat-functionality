package edu.school21.chat.models;

import java.util.List;
import java.util.Objects;

public class Chatroom {
    private final long id;
    private final String name;
    private final User owner;
    private final List<Message> messages;

    public Chatroom(long id, String name, User owner, List<Message> messages) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.messages = messages;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || this.getClass() != o.getClass()) return false;
        Chatroom ch = (Chatroom) o;
        return this.id == ch.id && Objects.equals(this.name, ch.name) &&
                Objects.equals(this.owner, ch.owner) && Objects.equals(this.messages, ch.messages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, owner, messages);
    }

    @Override
    public String toString() {
        return "{id = " + this.id + ", name = \"" + this.name +
                "\", creator = " + this.owner + ", messages = " + this.messages + "}";
    }
}
