package org.example;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public abstract class Message {

    public static EmbedBuilder getOpenTicketMessage() {
        EmbedBuilder eb = new EmbedBuilder();

        eb.addField("Support", "To receive assistance from developer or admin, please create a ticket. No matter how trivial it may seem!", true);
        eb.setDescription("mamesosu.net | Ticket Tool");
        eb.setColor(Color.CYAN);

        return eb;
    }
}
