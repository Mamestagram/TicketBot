package org.example.Support;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.example.Main;
import org.example.Message;
import org.example.Object.Bot;

// ユーザーのサポート関連の処理を書くクラス
public class User extends ListenerAdapter {

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

        }
        //ログイン情報
        else if(e.getComponentId().equals("forgot-login")) {

        }
        //その他のサポート (運営に直接連絡)
        else if(e.getComponentId().equals("other")) {

        }
    }
}
