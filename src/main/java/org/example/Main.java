package org.example;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

public class Main {
    public static void main(String[] args) {
        // IMPORTANT: Apna wahi Token yahan bhi daalna hai
        String botToken = "8807394117:AAEUBcPfHkrT07-BFjn08j51ZfZX-9WVI0I";

        try {
            TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();

            // Bot ko register kar rahe hain
            botsApplication.registerBot(botToken, new MyTelegramBot());

            System.out.println("Java Bot ekdum mast chalu ho gaya hai bhai...");

            // Thread ko zinda rakhne ke liye taaki bot automatic band na ho
            Thread.currentThread().join();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}