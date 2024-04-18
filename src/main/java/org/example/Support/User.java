package org.example.Support;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.interactions.modals.ModalInteraction;
import org.example.Main;
import org.example.Message;
import org.example.Object.Bot;
import org.example.Object.Database;
import org.example.Object.Ticket;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

// ユーザーのサポート関連の処理を書くクラス
public class User extends ListenerAdapter {

    private static TextInput createTextInput(String id, String label, String description, boolean isRequire, TextInputStyle style) {

        return TextInput.create(id, label, style)
                .setMinLength(1)
                .setPlaceholder(description)
                .setRequired(isRequire)
                .build();
    }

    @Override
    public void onChannelCreate(ChannelCreateEvent e) {

        if(e.getChannel().getName().contains("ticket-")) {

            Bot bot = Main.bot;
            JDA jda = bot.getJda();

                jda.getGuildById(bot.getGUILD_ID()).getTextChannelById(e.getChannel().getId()).sendMessageEmbeds(Message.getHelpTicketMessage().build())
                    .addActionRow(
                    Button.success("change-password", "Change password"),
                    Button.primary("forgot-login", "Forgot account"),
                    Button.danger("other", "Other")).queue();
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if(e.getChannel().getName().contains("ticket-")) {
            if (!e.getMember().getUser().isBot()) {
                if (e.getMessage().getContentRaw().toLowerCase().contains("hello")) {
                    e.getMessage().reply("Hello, " + e.getMember().getNickname() + "!").addEmbeds(
                            Message.getHelpTicketMessage().build()
                    ).addActionRow(
                            Button.success("change-password", "Change password"),
                            Button.primary("forgot-login", "Forgot account"),
                            Button.danger("other", "Other")
                    ).queue();
                }
            }
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent e) {
        //パスワードの変更
        if(e.getComponentId().equals("change-password")) {
            TextInput name = createTextInput("email", "Your Email", "test@gmail.com", true, TextInputStyle.SHORT);
            Modal modal = Modal.create("email-modal", "Verification of Identity Information")
                    .addActionRows(ActionRow.of(name)).build();

            e.replyModal(modal).queue();
        }
        //ログイン情報
        else if(e.getComponentId().equals("forgot-login")) {

        }
        //認証
        else if(e.getComponentId().equals("verify-button")) {
            Ticket t = null;
            if (!Main.tickets.isEmpty()) {
                for (Ticket ticket : Main.tickets) {
                    if (e.getChannel().getName().contains(String.valueOf(ticket.getId()))) {
                        t = ticket;
                    }
                }
            }

            if (t == null) {
                e.reply("An unexpected error has occurred! Please start over").setEphemeral(true).queue();
                return;
            }

            try {
                Mail.sendVerificationMail(t.getEmail(), t.getName());
            } catch (SQLException | IOException e1) {
                e1.printStackTrace();
                e.reply("An unexpected error has occurred").setEphemeral(true).queue();
            }

            e.reply("ok").queue();
        }
        //その他のサポート (運営に直接連絡)
        else if(e.getComponentId().equals("other")) {

        }
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent e) {

        try {
            Database d = Main.database;
            Bot bot = Main.bot;
            JDA jda = bot.getJda();
            Ticket t = null;

            Connection connection = d.getConnection(d.getDB_HOST(), d.getDB_NAME(), d.getDB_USER(), d.getDB_PASSWORD());
            PreparedStatement ps;
            ResultSet result;

            if (e.getModalId().equals("email-modal")) {
                ps = connection.prepareStatement("select id, name from users where email = ?");
                ps.setString(1, e.getValue("email").getAsString());
                result = ps.executeQuery();
                if(result.next()) {
                    if (!Main.tickets.isEmpty()) {
                        for (Ticket ticket : Main.tickets) {
                            if (e.getChannel().getName().contains(String.valueOf(ticket.getId()))) {
                                t = ticket;
                            }
                        }
                    } else {
                        t = new Ticket(Integer.parseInt(e.getChannel().getName().replace("ticket-", "")), e.getMember());
                        Main.tickets.add(t);
                    }

                    //もしチケットが存在しない場合は新しくインスタンスを作成する (やむを得ない再起動などでリセットされてしまった用)
                    if(t == null) {
                        t = new Ticket(Integer.parseInt(e.getChannel().getName().replace("ticket-", "")), e.getMember());
                        Main.tickets.add(t);
                    }

                    t.setName(result.getString("name"));
                    t.setEmail(e.getValue("email").getAsString());

                    e.replyEmbeds(Message.getVerifyMessage().build())
                            .addActionRow(Button.success("verify-button", "Verify"))
                            .queue();

                } else {
                    e.reply("Hey! the username does not exist!").setEphemeral(true).queue();
                }
            }

            connection.close();

    } catch (SQLException e1) {
            e1.printStackTrace();
            e.reply("An unexpected error has occurred").setEphemeral(true).queue();
        }
    }
}
