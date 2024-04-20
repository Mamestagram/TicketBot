package org.example.Support;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
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
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
                    Button.primary("forgot-login", "Forgot my name")).queue();
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if(e.getChannel().getName().contains("ticket-")) {
            if (!e.getMember().getUser().isBot()) {
                if (e.getMessage().getContentRaw().toLowerCase().equals("hello")) {
                    e.getMessage().reply("Hello, " + e.getMember().getNickname() + "!").addEmbeds(
                            Message.getHelpTicketMessage().build()
                    ).addActionRow(
                            Button.success("change-password", "Change password"),
                            Button.primary("forgot-login", "Forgot my name")
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
            TextInput name = createTextInput("email", "Your Email", "test@gmail.com", true, TextInputStyle.SHORT);
            Modal modal = Modal.create("forgot-name-modal", "Verification of Identity Information")
                    .addActionRows(ActionRow.of(name)).build();

            e.replyModal(modal).queue();
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
                e.getMessage().delete().queue();
                return;
            }

            try {
                //メール送信とメッセージを送信

                if (Mail.sendVerificationMail(t.getEmail(), t.getName())) {
                    e.reply("Verification code sent to your email!").setEphemeral(true).queue();
                    e.getMessage().editMessageEmbeds(Message.getVerifyCodeMessage().build()).setComponents(ActionRow.of(
                            Button.success("verify-code-button", "Enter Verification Code")
                    )).queue();
                } else {
                    e.reply("An unexpected error has occurred").setEphemeral(true).queue();
                    e.getMessage().delete().queue();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
                e.reply("An unexpected error has occurred").setEphemeral(true).queue();
                e.getMessage().delete().queue();
            }
        }
        // 認証コード
        else if(e.getComponentId().equals("verify-code-button")) {
            TextInput code = createTextInput("code", "Verification Code", "$2a$12$mDJmDSZZi/7jlMycrsVvqumaZFacck1tl2pz/YuYkIGmm87G6X4LC", true, TextInputStyle.SHORT);
            Modal modal = Modal.create("code-modal", "Verification of Identity Information")
                    .addActionRows(ActionRow.of(code)).build();
            e.replyModal(modal).queue();
        }
        // パスワードの変更
        else if(e.getComponentId().equals("change-password-button")) {
            TextInput password = createTextInput("password", "New Password", "password", true, TextInputStyle.SHORT);
            Modal modal = Modal.create("password-modal", "Change Password")
                    .addActionRows(ActionRow.of(password)).build();
            e.replyModal(modal).queue();
        }
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent e) {

        try {
            Database d = Main.database;
            Ticket t = null;

            Connection connection = d.getConnection(d.getDB_HOST(), d.getDB_NAME(), d.getDB_USER(), d.getDB_PASSWORD());
            PreparedStatement ps;
            ResultSet result;

            //メールアドレスの確認
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
                    e.reply("Hey! the email does not exist!").setEphemeral(true).queue();
                }
            } else if(e.getModalId().equals("code-modal")) {
                //チケットインスタンスの作成
                if (!Main.tickets.isEmpty()) {
                    for (Ticket ticket : Main.tickets) {
                        if (e.getChannel().getName().contains(String.valueOf(ticket.getId()))) {
                            t = ticket;
                        }
                    }
                }

                if (t == null) {
                    e.reply("An unexpected error has occurred! Please start over").setEphemeral(true).queue();
                    e.getMessage().delete().queue();
                    return;
                }

                //認証コードの確認
                ps = connection.prepareStatement("select verification_code from users where name = ?");
                ps.setString(1, t.getName());
                result = ps.executeQuery();

                if(result.next()) {
                    if(Objects.equals(e.getValue("code").getAsString(), result.getString("verification_code"))) {
                        e.reply("Verification successful!").setEphemeral(true).queue();
                        e.getMessage().editMessageEmbeds(Message.getChangePasswordMessage().build()).setComponents(ActionRow.of(
                                Button.success("change-password-button", "Change Password")
                        )).queue();
                    } else {
                        e.reply("Verification failed!\nYour verification code may wrong!").setEphemeral(true).queue();
                    }
                } else {
                    e.reply("An unexpected error has occurred! Please start over").setEphemeral(true).queue();
                    e.getMessage().delete().queue();
                }
            }
            //パスワードの変更
            else if (e.getModalId().equals("password-modal")) {
                //チケットインスタンスの作成
                if (!Main.tickets.isEmpty()) {
                    for (Ticket ticket : Main.tickets) {
                        if (e.getChannel().getName().contains(String.valueOf(ticket.getId()))) {
                            t = ticket;
                        }
                    }
                }

                if (t == null) {
                    e.reply("An unexpected error has occurred! Please start over").setEphemeral(true).queue();
                    e.getMessage().delete().queue();
                    return;
                }

                try {
                    Password.changeUserPassword(t.getName(), e.getValue("password").getAsString());
                } catch (NoSuchAlgorithmException e1) {
                    e1.printStackTrace();
                }
                e.reply("Password changed successfully!").setEphemeral(true).queue();
                e.getMessage().editMessageEmbeds(Message.getCompleteMessage().build()).setComponents().queue();
            }
            //名前を忘れてしまった
            else if (e.getModalId().equals("forgot-name-modal")) {
                ps = connection.prepareStatement("select name from users where email = ?");
                ps.setString(1, e.getValue("email").getAsString());
                result = ps.executeQuery();
                if(result.next()) {
                    try {
                        e.reply("Your name sent to your email").setEphemeral(true).queue();
                        e.getMessage().replyEmbeds(Message.getCompleteMessage().build()).queue();
                        Mail.sendNameNotificationMail(e.getValue("email").getAsString(), result.getString("name"));
                    } catch(IOException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    e.reply("Hey! the email does not exist!").setEphemeral(true).queue();
                }
            }

            connection.close();

    } catch (SQLException e1) {
            e1.printStackTrace();
            e.reply("An unexpected error has occurred").setEphemeral(true).queue();
            e.getMessage().delete().queue();
        }
    }
}
