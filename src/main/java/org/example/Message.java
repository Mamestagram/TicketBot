package org.example;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import org.example.Object.Ticket;

import java.awt.*;

public abstract class Message {

    //チケットを開くときのメッセージ
    public static EmbedBuilder getOpenTicketMessage() {
        EmbedBuilder eb = new EmbedBuilder();

        eb.addField("Support", ":lock:  Change password\n:page_with_curl:  Send your name\n:white_check_mark:  Support from admin and developer", true);
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
        eb.setColor(Color.CYAN);

        return eb;
    }

    public static EmbedBuilder getVerifyCodeMessage() {

        EmbedBuilder eb = new EmbedBuilder();

        eb.addField("Verify your request", """
                           Please enter the code that was sent to your email
                            """, false);
        eb.setColor(Color.CYAN);

        return eb;
    }

    public static EmbedBuilder getChangePasswordMessage() {
        EmbedBuilder eb = new EmbedBuilder();

        eb.addField("Change Password", "Please enter your new password!", false);
        eb.setColor(Color.CYAN);

        return eb;
    }

    public static EmbedBuilder getCompleteMessage() {
        EmbedBuilder eb = new EmbedBuilder();

        eb.addField("Complete", "Your request has been completed!", false);
        eb.setColor(Color.GREEN);

        return eb;
    }

    public static EmbedBuilder getCloseCheckMessage() {
        EmbedBuilder eb = new EmbedBuilder();

        eb.addField("Close Ticket", "Are you sure you want to close this ticket?", false);
        eb.setColor(Color.RED);

        return eb;
    }
}
