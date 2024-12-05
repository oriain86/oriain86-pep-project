package Controller;

import Service.AccountService;
import Service.MessageService;
import Model.Account;
import Model.Message;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;

import java.util.Optional;



public class SocialMediaController {

    // Dependencies
    
    private final AccountService accountService = new AccountService();
    private final MessageService messageService = new MessageService();



    // Endpoints

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerAccount);
        app.post("/login", this::loginAccount);
        app.get("/messages", this::getAllMessages);
        app.post("/messages", this::createMessage);
        app.get("/messages/{messageId}", this::getMessageById);
        app.delete("/messages/{messageId}", this::deleteMessageById);
        app.patch("/messages/{messageId}", this::updateMessageText);
        app.get("/accounts/{accountId}/messages", this::getMessagesByAccountId);
        return app;
    }



    // HTTP Handlers

    // Delegates requests to the accountService.registerAccount function

    private void registerAccount(Context context) {
        Account account = context.bodyAsClass(Account.class);
        Optional<Account> registeredAccount = accountService.registerAccount(account);
        if (registeredAccount.isPresent()) {
            context.status(200).json(registeredAccount.get());
        } else {
            context.status(400).result();
        }
    }



    // Delegates requests to the accountService.loginAccount function

    private void loginAccount(Context context) {
        Account account = context.bodyAsClass(Account.class);
        Optional<Account> loggedInAccount = accountService.loginAccount(account.getUsername(), account.getPassword());
        if (loggedInAccount.isPresent()) {
            context.status(200).json(loggedInAccount.get());
        } else {
            context.status(401).result();
        }
    }



    // Delegates requests to the messageService.getAllMessages function

    private void getAllMessages(Context context) {
        List<Message> messages = messageService.getAllMessages();
        context.status(200).json(messages);
    }



    // Delegates requests to the messageService.createMessage function

    private void createMessage(Context context) {
        Message message = context.bodyAsClass(Message.class);
        Optional<Message> createdMessage = messageService.createMessage(message);
        if (createdMessage.isPresent()) {
            context.status(200).json(createdMessage.get());
        } else {
            context.status(400).result();
        }
    }



    // Delegates requests to the messageService.getMessageById function 

    private void getMessageById(Context context) {
        int messageId = Integer.parseInt(context.pathParam("messageId"));
        Optional<Message> message = messageService.getMessageById(messageId);
        if (message.isPresent()) {
            context.status(200).json(message.get());
        } else {
            context.status(200).result(""); // Return an empty result for non-existent messages
        }
    }
    



    // Delegates requests to the messageService.deleteMessage function

    private void deleteMessageById(Context context) {
        int messageId = Integer.parseInt(context.pathParam("messageId"));
        Optional<Message> deletedMessage = messageService.deleteMessage(messageId);
        if (deletedMessage.isPresent()) {
            context.status(200).json(deletedMessage.get());
        } else {
            context.status(200).result(""); // Idempotent response for non-existent messages
        }
    }



    // Delegates requests to the messageService.updateMessageText function

    private void updateMessageText(Context context) {
        int messageId = Integer.parseInt(context.pathParam("messageId"));
        String newText = context.bodyAsClass(Message.class).getMessage_text();
    
        Optional<Message> updatedMessage = messageService.updateMessageText(messageId, newText);
        if (updatedMessage.isPresent()) {
            context.status(200).json(updatedMessage.get());
        } else {
            context.status(400).result();
        }
    }



    // Delegates requests to the messageService.updateMessageText function

    private void getMessagesByAccountId(Context context) {
        int accountId = Integer.parseInt(context.pathParam("accountId"));
        List<Message> messages = messageService.getMessagesByAccountId(accountId);
        context.status(200).json(messages);
    }
}
