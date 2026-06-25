package org.example;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class MyTelegramBot implements LongPollingSingleThreadUpdateConsumer {

    // IMPORTANT: @BotFather se mila hua token yahan paste kar bhai
    private final String botToken = "8807394117:AAEUBcPfHkrT07-BFjn08j51ZfZX-9WVI0I";
    private final TelegramClient telegramClient = new OkHttpTelegramClient(botToken);

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            System.out.println("Bhai, user ne likha: " + messageText);

            String responseText;
            if (messageText.equals("/start")) {
                responseText = "Ram Ram Bhai! Main Java se bana tera pehla bot hoon. Kuch bhi likh, tera bhai reply dega!";
            } else {
                responseText = "Bhai, tu bol raha hai: " + messageText;
            }

            SendMessage message = SendMessage.builder()
                    .chatId(chatId)
                    .text(responseText)
                    .build();

            try {
                telegramClient.execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}