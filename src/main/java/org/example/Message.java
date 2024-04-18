package org.example;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public abstract class Message {

    //チケットを開くときのメッセージ
    public static EmbedBuilder getOpenTicketMessage() {
        EmbedBuilder eb = new EmbedBuilder();

        eb.addField("Support", "To receive assistance from developer or admin, please create a ticket. No matter how trivial it may seem!", true);
        eb.setFooter("mamesosu.net | Ticket Tool");
        eb.setColor(Color.CYAN);

        return eb;
    }

    public static EmbedBuilder getHelpTicketMessage() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.addField("Support", """
                Welcome to Mamestagram Support!
                We help you with your problems.
                To get you started, please select the issue that most closely describes what you need help with.""", false);
        eb.setFooter("mamesosu.net | Ticket Tool");
        eb.setColor(Color.GREEN);

        return eb;
    }

    public static EmbedBuilder getChangePasswordMessage(int guidance) {

        EmbedBuilder eb = new EmbedBuilder();

        switch (guidance) {
            case 0 -> {

            }
        }

        return eb;
    }
}
