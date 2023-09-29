package edu.school21.chat.models;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Message {
    private final long id;
    private final User author;
    private final Chatroom room;
    private final String text;
    private final DateTimeFormatter dateTime;

    public Message(long id, User author, Chatroom room, String text, DateTimeFormatter dateTime) {
        this.id = id;
        this.author = author;
        this.room = room;
        this.text = text;
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || this.getClass() != o.getClass()) return false;
        Message msg = (Message) o;
        return this.id == msg.id && Objects.equals(this.author, msg.author) && Objects.equals(this.room, msg.room) &&
                Objects.equals(this.text, msg.text) && Objects.equals(this.dateTime, msg.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author, room, text, dateTime);
    }

    @Override
    public String toString() {
        return "Message =\n" +
                "id = " + this.id +
                ", author = " + this.author +
                ", room = " + this.room +
                ", text = " + this.text +
                ", dateTime = " + this.dateTime;
    }
}
