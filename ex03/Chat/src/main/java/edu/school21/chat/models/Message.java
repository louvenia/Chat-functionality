package edu.school21.chat.models;

import java.time.LocalDateTime;
import java.util.Objects;

public class Message {
    private Long id;
    private User author;
    private Chatroom room;
    private String text;
    private LocalDateTime dateTime;

    public Message(Long id, User author, Chatroom room, String text, LocalDateTime dateTime) {
        this.id = id;
        this.author = author;
        this.room = room;
        this.text = text;
        this.dateTime = dateTime;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAuthor() {
        return this.author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Chatroom getRoom() {
        return this.room;
    }

    public void setRoom(Chatroom room) {
        this.room = room;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
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
        return "Message : {\n" +
                "id = " + this.id +
                ",\nauthor = " + this.author +
                ",\nroom = " + this.room +
                ",\ntext = \"" + this.text +
                "\",\ndateTime = " + this.dateTime +
                "\n}";
    }
}
