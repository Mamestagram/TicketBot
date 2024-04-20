package org.example.Object;

import net.dv8tion.jda.api.entities.Member;

public class Ticket {

    //User
    private final Member member;
    //Ticket Data
    private final int id;
    private String email;
    private String name;

    public Ticket(int id, Member m) {
        this.id = id;
        this.member = m;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

}
