package carsharing.service.telegram;

import carsharing.exception.TelegramException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@RequiredArgsConstructor
@Service
public class TelegramNotificationService extends TelegramLongPollingBot {
    @Value("${telegram.botToken}")
    private String botToken;

    @Value("${telegram.chatId}")
    private Long chatId;

    public void sendNotification(String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(message);

        try {
            execute(sendMessage);
        } catch (TelegramApiException ex) {
            throw new TelegramException("Telegram notification failed: " + ex.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return "YourBotName";
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String userMessage = update.getMessage().getText();
            Long userChatId = update.getMessage().getChatId();
            String responseMessage = "You said: " + userMessage;
            sendResponse(userChatId, responseMessage);
        }
    }

    private void sendResponse(Long chatId, String message) {
        SendMessage response = new SendMessage();
        response.setChatId(chatId.toString());
        response.setText(message);

        try {
            execute(response);
        } catch (TelegramApiException ex) {
            throw new TelegramException("Telegram notification failed: " + ex.getMessage());
        }
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }
}
