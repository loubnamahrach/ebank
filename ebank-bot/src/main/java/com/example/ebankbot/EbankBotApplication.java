package com.example.ebankbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(excludeName = "com.zgamelogic.discord.components.DiscordBot")
public class EbankBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankBotApplication.class, args);
    }

}
