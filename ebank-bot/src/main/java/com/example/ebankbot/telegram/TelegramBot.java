package com.example.ebankbot.telegram;

import com.example.ebankbot.agents.EbankAIAgent;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private static final Logger log = LoggerFactory.getLogger(TelegramBot.class);

    @Value("${telegram.token}")
    private String telegramBotToken;

    @Value("${telegram.username:Ebank01Bot}")
    private String telegramBotUsername;

    private final EbankAIAgent aiAgent;

    public TelegramBot(EbankAIAgent aiAgent) {
        this.aiAgent = aiAgent;
    }

    @PostConstruct
    public void registerTelegramBot() {
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(this);
            log.info("Telegram Bot registered successfully: {}", telegramBotUsername);
        } catch (TelegramApiException e) {
            log.error("Failed to register Telegram bot", e);
        }
    }

    @Override
    public String getBotUsername() {
        return telegramBotUsername;
    }

    @Override
    public String getBotToken() {
        return telegramBotToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            log.info("Received message from Telegram chat {}: {}", chatId, messageText);

            try {
                String response = aiAgent.chat(messageText, String.valueOf(chatId));
                if (response == null || response.isBlank()) {
                    response = "Désolé, aucune réponse n'a été générée.";
                }
                SendMessage sendMessage = new SendMessage(String.valueOf(chatId), response);
                execute(sendMessage);
            } catch (Exception e) {
                log.error("Error handling message from Telegram chat {}", chatId, e);
                try {
                    SendMessage errorMessage = new SendMessage(String.valueOf(chatId),
                            "Désolé, une erreur est survenue lors du traitement : " + e.getMessage());
                    execute(errorMessage);
                } catch (TelegramApiException te) {
                    log.error("Error sending error message to Telegram", te);
                }
            }
        }
    }
}
