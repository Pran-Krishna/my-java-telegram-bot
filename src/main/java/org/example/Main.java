package org.example;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        try {
            // 🔥 SAFETY CHECK: Pehle check karo ki laptop ya cloud par keys mili ya nahi
            String botToken = System.getenv("BOT_TOKEN");
            String geminiApiKey = System.getenv("GEMINI_API_KEY");

            if (botToken == null || botToken.isEmpty()) {
                System.out.println("❌ ERROR: 'BOT_TOKEN' is missing in Environment Variables!");
                System.out.println("💡 Please add it to IntelliJ Run Configurations or Render Dashboard.");
                return;
            }

            if (geminiApiKey == null || geminiApiKey.isEmpty()) {
                System.out.println("❌ ERROR: 'GEMINI_API_KEY' is missing in Environment Variables!");
                return;
            }

            // 1. Built-in Java HttpServer Start Karo
            int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

            server.createContext("/", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    String response = "Sneha Bot is up and running! ❤️";
                    exchange.sendResponseHeaders(200, response.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            });

            server.setExecutor(null);
            server.start();
            System.out.println("Web Server started on port " + port);

            // 2. Telegram Bot Register Karo (Ab token kabhi null nahi jayega)
            TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();
            botsApplication.registerBot(botToken, new MyTelegramBot());
            System.out.println("Java Bot ekdum mast chalu ho gaya hai bhai...");

            // 3. ANTI-SLEEP HACK
            startAntiSleepTimer();

            Thread.currentThread().join();

        } catch (Exception e) {
            System.out.println("❌ Main Execution Error: " + e.getMessage());
        }
    }

    private static void startAntiSleepTimer() {
        String renderUrl = System.getenv("RENDER_EXTERNAL_URL");

        if (renderUrl != null && !renderUrl.isEmpty()) {
            System.out.println("🚀 Anti-Sleep Timer Active! Target URL: " + renderUrl);
            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

            scheduler.scheduleAtFixedRate(() -> {
                try {
                    HttpClient client = HttpClient.newHttpClient();
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(renderUrl))
                            .GET()
                            .build();

                    client.send(request, HttpResponse.BodyHandlers.ofString());
                    System.out.println("🔄 [Anti-Sleep] Self-ping sent! Render is awake. 😎");
                } catch (Exception e) {
                    System.out.println("⚠️ [Anti-Sleep] Ping failed: " + e.getMessage());
                }
            }, 5, 10, TimeUnit.MINUTES);
        } else {
            System.out.println("ℹ️ Local testing chal rahi hai, Anti-Sleep Timer start nahi kiya.");
        }
    }
}
