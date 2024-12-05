package Service;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

import java.util.List;
import java.util.Optional;



public class MessageService {
    
    private final MessageDAO messageDAO = new MessageDAO();
    private final AccountDAO accountDAO = new AccountDAO();



    // Method to retrieve all message. (Method that calls the messageDAO.getAllMessages function)

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }



    // Method that creates a new message. (Method that calls the messageDAO.createMessage function)

    public Optional<Message> createMessage(Message message) {
        if (message.getMessage_text() == null || message.getMessage_text().isEmpty()
                || message.getMessage_text().length() > 255
                || !accountDAO.isValidUser(message.getPosted_by())) {
            return Optional.empty();
        }

        Message createdMessage = messageDAO.createMessage(message);
        return Optional.ofNullable(createdMessage);
    }



    // Method to retrieve a specific message by its Id. (Method that calls the messageDAO.getMessageById function)

    public Optional<Message> getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }



    // Method to delete a message by its Id. (Method that calls the messageDAO.deleteMessageById function)

    public Optional<Message> deleteMessage(int messageId) {
        Optional<Message> message = messageDAO.getMessageById(messageId);
        if (message.isPresent()) {
            messageDAO.deleteMessageById(messageId);
        }
        return message;
    }



    // Method that updates a message by its Id. (Method that calls the messageDAO.updateMessageText function)

    public Optional<Message> updateMessageText(int messageId, String newText) {
        if (newText == null || newText.isEmpty() || newText.length() > 255) {
            return Optional.empty();
        }
        boolean updated = messageDAO.updateMessageText(messageId, newText);
        if (updated) {
            return messageDAO.getMessageById(messageId);
        }
        return Optional.empty();
    }



    // Method that retrieves all messages by the account Id. (Method that calls the messageDAO.getMessagesByAccountId function)

    public List<Message> getMessagesByAccountId(int accountId) {
        return messageDAO.getMessagesByAccountId(accountId);
    }
}
