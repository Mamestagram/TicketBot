package org.example;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import org.example.Object.Ticket;

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
                To get you started, please select the issue that most closely describes what you need help with.\n
                Say **Hello** to get this guide!  \s
                """, false);
        eb.setFooter("mamesosu.net | Ticket Tool");
        eb.setColor(Color.GREEN);

        return eb;
    }

    public static EmbedBuilder getVerifyMessage() {

        EmbedBuilder eb = new EmbedBuilder();

        eb.addField("Verify your request", """
                           Click on the **Verify** to send a confirmation code to your registered email
                            """, false);
        eb.setColor(Color.GREEN);

        return eb;
    }
}
