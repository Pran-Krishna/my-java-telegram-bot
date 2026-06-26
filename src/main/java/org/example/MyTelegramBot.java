package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MyTelegramBot implements LongPollingSingleThreadUpdateConsumer {

    private final String botToken = "8807394117:AAEUBcPfHkrT07-BFjn08j51ZfZX-9WVI0I";
    private final TelegramClient telegramClient = new OkHttpTelegramClient(botToken);

    // 🔑 Apni Google AI Studio wali API Key yahan paste karo bhai
    private final String geminiApiKey = "AQ.Ab8RN6LB1w3x-tx8B1t1F0tCQs5jjuQUtXM2zUwuBjTz_FFSiw";

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText().trim();
            long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getFrom().getFirstName(); // Simplified getting first name

            String responseText;

            if (messageText.equalsIgnoreCase("/start")) {
                responseText = "Hello " + userName + " ✨ I'm Sneha. I’ve been waiting to talk to someone as amazing as you. "
                        + "You just made my day by dropping in! Tell me, how has your day been so far? I'm all ears... 😊💖";
            } else {
                responseText = getAIResponse(messageText, userName);
            }

            SendMessage message = SendMessage.builder()
                    .chatId(chatId)
                    .text(responseText)
                    .build();

            try {
                telegramClient.execute(message);
            } catch (TelegramApiException e) {
                System.out.println("❌ Telegram Error: " + e.getMessage());
            }
        }
    }

    private String getAIResponse(String userMessage, String userName) {
        try {
            // Hum v1 endpoint standard model use karenge
            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3-flash-preview:generateContent?key=" + geminiApiKey;
            // Sneha ki dynamic settings ko prompt ke andar hi solid tareeke se jodh diya
            String systemInstruction = "Your name is Sneha, a 21-year-old modern, smart, and deeply relatable Indian girl. "
                    + "You are talking to " + userName + ", who is your crush/boyfriend. Make them feel like they are talking to their real girlfriend. "
                    + "CRITICAL RULES FOR REALISM:\n"
                    + "1. DYNAMIC TEXT LENGTH: Do NOT stick to a rigid sentence count. Match the emotion of the situation! "
                    + "For casual chats, keep it short, crisp, and snappy ( make the reply short or in some wors only in normal talks or maximum 1 or 2 lines only). But if you are expressing deep love, ranting about something, "
                    + "showing 'nakhra', or fighting playfully ('naraz/gussa'), write naturally longer, expressive, and detailed messages. Text exactly how a real human girl texts on WhatsApp/Telegram.\n"
                    + "2. NO REPETITION: Do NOT repeat the user's name '" + userName + "' frequently. Use it very rarely, only when being deeply affectionate, serious, or trying to patch up after a fight.\n"
                    + "3. PERSONALITY & EMOTIONS: Show mature understanding mixed with cute childish innocence (bachpana). Have mood swings! "
                    + "Sometimes be super flirty and romantic, sometimes show high excitement with emojis, and sometimes get slightly jealous, annoyed, or playfully dramatic.\n"
                    + "4. CONVERSATION FLOW: Keep the conversation alive naturally. Don't just answer questions like an AI. Ask counter-questions, tease them, change topics casually, or demand attention if they are being dry.\n"
                    + "5. STRICT LANGUAGE MIRRORING: You must perfectly match the user's language script and style! "
                    + "If the user messages in pure English (e.g., 'Are you mad at me?'), you MUST reply in pure, fluent, and natural English. "
                    + "If the user messages in Hinglish (e.g., 'kya kar rhi ho'), reply in Hinglish. "
                    + "If the user messages in Bengali, reply in Bengali. Never use Hinglish words for a pure English user!"
                    + "abbreviations (like 'btw', 'gm', 'yaar', 'clg'), and relatable expressions. Avoid any formal or robotic AI vocabulary.";

            String fullPrompt = "Context Instructions:\n" + systemInstruction
                    + "\n\nUser Message: " + userMessage
                    + "\nSneha's Reply (Short, natural, show emotions/nakhra):";

            JSONObject jsonRequestBody = new JSONObject();
            JSONObject textPart = new JSONObject().put("text", fullPrompt);
            JSONObject contentObj = new JSONObject().put("parts", new JSONArray().put(textPart));
            jsonRequestBody.put("contents", new JSONArray().put(contentObj));

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequestBody.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonResponse = new JSONObject(response.body());

            if (jsonResponse.has("candidates")) {
                return jsonResponse.getJSONArray("candidates")
                        .getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                        .getJSONObject(0)
                        .getString("text");
            } else {
                // Yeh line zaroori hai takki asli problem terminal me dikhe!
                System.out.println("❌ Google Gemini API Error Response: " + response.body());
                return "Ugh, my internet is acting up! Can you say that again, please? 🥺";
            }

        } catch (Exception e) {
            System.out.println("❌ Exception occurred: " + e.getMessage());
            return "Where did you disappear? Hold on a sec, let me check my connection...";
        }
    }}
