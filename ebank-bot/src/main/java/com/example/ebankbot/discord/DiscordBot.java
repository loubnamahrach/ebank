
package com.example.ebankbot.discord;

import com.example.ebankbot.agents.EbankAIAgent;
import com.zgamelogic.discord.annotations.DiscordController;
import com.zgamelogic.discord.annotations.DiscordMapping;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

@DiscordController
public class DiscordBot {

    private final EbankAIAgent ebankAIAgent;

    public DiscordBot(EbankAIAgent ebankAIAgent) {
        this.ebankAIAgent = ebankAIAgent;
    }

    @DiscordMapping
    public void perform(MessageReceivedEvent event) {

        // Ignorer les messages envoyés par les bots
        if (event.getAuthor().isBot()) {
            return;
        }

        String query = event.getMessage().getContentRaw();

        // Utiliser un identifiant stable par utilisateur Discord
        String conversationId =
                event.getAuthor().getId();

        // Appeler l'agent IA
        String response =
                ebankAIAgent.chat(query, conversationId);

        // Envoyer la réponse sur Discord
        event.getChannel()
                .sendMessage(response)
                .queue();
    }
}
