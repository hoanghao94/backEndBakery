package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Message;
import com.mycompany.myapp.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class MessageController {
    private final MessageRepository messageRepository;

    @Autowired
    public MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @PostMapping("/messages")
    @ResponseBody
    public Message createMessage(@RequestBody Message message) {
        message.setSentAt(LocalDateTime.now());
        return messageRepository.save(message);
    }

    @GetMapping("/messages")
    @ResponseBody
    public List<Message> getMessages() {
        return messageRepository.findAll();
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public Message handleChatMessage(Message message) {
        message.setSentAt(LocalDateTime.now());
        return messageRepository.save(message);
    }
}
