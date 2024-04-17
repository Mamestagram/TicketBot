package org.example.Object;

import net.dv8tion.jda.api.entities.Member;

public class Ticket {

    //User
    private final Member member;
    //Ticket Data
    private final int id;
    private int verification_count;

    Ticket(int id, Member m) {
        this.id = id;
        this.member = m;
        this.verification_count = 0;
    }

    public int getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public int getVerification_count() {
        return verification_count;
    }

    public void setVerification_count(int verification_count) {
        this.verification_count = verification_count;
    }
}
