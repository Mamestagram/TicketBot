package org.example.Object;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.example.Event.CreateChannel;
import org.example.Event.DeleteChannel;

public class Bot {

    private long GUILD_ID;
    private long CHANNEL_ID;
    private String TOKEN;
    private long CATEGORY_ID;

    private JDA jda;

    public Bot() {
        Dotenv dotenv = Dotenv.configure()
                .load();

        GUILD_ID = Long.parseLong(dotenv.get("GUILD_ID"));
        CHANNEL_ID = Long.parseLong(dotenv.get("CHANNEL_ID"));
        TOKEN = dotenv.get("TOKEN");
        CATEGORY_ID = Long.parseLong(dotenv.get("CATEGORY_ID"));
    }

    public long getCHANNEL_ID() {
        return CHANNEL_ID;
    }

    public long getGUILD_ID() {
        return GUILD_ID;
    }

    public String getTOKEN() {
        return TOKEN;
    }

    public JDA getJda() {
        return jda;
    }

    public long getCATEGORY_ID() {
        return CATEGORY_ID;
    }

    public void loadJDA() {
        jda = JDABuilder.createDefault(this.TOKEN)
                .setRawEventsEnabled(true)
                .enableIntents(
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_MESSAGE_REACTIONS,
                        GatewayIntent.GUILD_EMOJIS_AND_STICKERS
                ).enableCache(
                        CacheFlag.MEMBER_OVERRIDES,
                        CacheFlag.ROLE_TAGS,
                        CacheFlag.EMOJI
                )
                .disableCache(
                        CacheFlag.VOICE_STATE,
                        CacheFlag.STICKER,
                        CacheFlag.SCHEDULED_EVENTS
                ).setActivity(
                        Activity.playing("Loading all settings.."))
                .addEventListeners(new CreateChannel())
                .addEventListeners(new DeleteChannel())
                .build();
    }


}
