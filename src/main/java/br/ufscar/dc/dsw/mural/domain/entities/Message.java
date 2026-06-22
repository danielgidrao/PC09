package br.ufscar.dc.dsw.mural.domain.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "table_messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // "from" e "to" sao keywords do SQL, por isso renomeamos as colunas
    @Column(name = "mFrom", nullable = false)
    private String from;

    @Column(name = "mTo", nullable = false)
    private String to;

    @Column(length = 500, nullable = false)
    private String message;

    private String timestamp;

    // Muitas mensagens pertencem a um usuario (quem postou)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Message() {
    }

    public Message(String from, String to, String message, User user) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return String.format("Message[id='%d', from='%s', to='%s', message='%s', timestamp='%s']",
                id, from, to, message, timestamp);
    }
}
