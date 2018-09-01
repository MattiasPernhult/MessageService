package app.component.message;

import app.component.user.User;
import org.joda.time.DateTime;

import java.util.Comparator;

public class Message {
    private String id;
    private String text;
    private DateTime createdAt;
    private User user;

    public Message() {
    }

    public Message(String id, String text, DateTime createdAt, User user) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public SortByCreatedAt getSorter() {
        return new SortByCreatedAt();
    }

    private class SortByCreatedAt implements Comparator<Message> {

        @Override
        public int compare(Message m1, Message m2) {
            return (int) (m1.createdAt.getMillis() - m2.createdAt.getMillis());
        }
    }
}
