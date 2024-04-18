package org.example.Event;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.example.Message;

public class DeleteChannel extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent e) {
        if(e.getComponentId().equals("close")) {
            e.replyEmbeds(Message.getCloseCheckMessage().build()
                    ).addActionRow(
                            Button.danger("btn_close", "Close"),
                            Button.primary("btn_cancel", "Cancel")
                    )
                    .queue();
        }
        else if(e.getComponentId().equals("btn_close")) {
            e.getChannel().delete().queue();
        }
        else if(e.getComponentId().equals("btn_cancel")) {
            e.getMessage().delete().queue();
        }
    }
}
