package org.example.Event;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import org.example.Main;
import org.example.Message;
import org.example.Object.Bot;
import org.example.Object.Ticket;

import java.util.EnumSet;

public class CreateChannel extends ListenerAdapter {

    //チケットの作成メッセージを送信する
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        Bot bot = Main.bot;

        if(bot.getCHANNEL_ID() == e.getChannel().getIdLong()) {
            if(e.getMessage().getContentRaw().equals("create-support-channel")) {
                JDA jda = bot.getJda();
                jda.getGuildById(bot.getGUILD_ID()).getTextChannelById(bot.getCHANNEL_ID()).sendMessageEmbeds(Message.getOpenTicketMessage().build())
                        .addActionRow(
                                Button.success("btn_create", "Create ticket")
                        ).queue();
            }
        }
    }

    //ボタンに反応したイベントを確認する
    @Override
    public void onButtonInteraction(ButtonInteractionEvent e) {

        if(e.getComponentId().equals("btn_create")) {
            Guild g = e.getMember().getGuild();
            Bot bot = Main.bot;

            //初期値: 1
            int ticketNo = 1;

            //アクティブなチケットの最後の数字を取得する
            for(GuildChannel ch : g.getChannels()) {
                if(ch.getName().contains("ticket-")) {
                    if (ticketNo <= Integer.parseInt(ch.getName().replace("ticket-", ""))) {
                        ticketNo = Integer.parseInt(ch.getName().replace("ticket-", "")) + 1;
                    }
                }
            }

            //チケットを作成する
            g.createTextChannel("ticket-" + ticketNo, g.getCategoryById(bot.getCATEGORY_ID()))
                    .addPermissionOverride(e.getMember(), EnumSet.of(Permission.VIEW_CHANNEL), null)
                    //Nominator
                    .addRolePermissionOverride(1093865053448589342L, EnumSet.of(Permission.VIEW_CHANNEL), null)
                    //Server Moderator
                    .addRolePermissionOverride(1194245046321545327L, EnumSet.of(Permission.VIEW_CHANNEL), null)
                    .addPermissionOverride(g.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                    .queue();

            //チケットのデータの追加
            Main.tickets.add(new Ticket(ticketNo, e.getMember()));

            e.reply("Created your ticket!").setEphemeral(true).queue();
        }
    }
}
