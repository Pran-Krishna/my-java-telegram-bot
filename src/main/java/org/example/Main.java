package org.example;

import com.sun.net.httpserver.HttpServer;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

public class Main {
    public static void main(String[] args) {
        
        // 1. Render ko khush rakhne ke liye ek Asli Java Web Server shuru kar rahe hain
        try {
            int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/", exchange -> {
                String response = "Bot is running perfectly, bhai!";
                exchange.sendResponseHeaders(200, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            });
            server.setExecutor(null);
            server.start();
            System.out.println("Web Server started on port " + port);
        } catch (Exception e) {
            System.out.println("Web server shuru nahi ho paya: " + e.getMessage());
        }

        // 2. Ab tumhara Telegram Bot ka logic chalega
        // Token check kar lena bhai (Screenshot ke hisab se sahi hai)
        String botToken = "8807394117:AAEUBcPfHkrT07-Bfjn08j51ZfZX-9WVIOI";

        try {
            TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();
            botsApplication.registerBot(botToken, new MyTelegramBot());
            System.out.println("Java Bot ekdum mast chalu ho gaya hai bhai...");
            
            // Thread ko zinda rakhne ke liye
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
