package com.chat.app.controller;

import com.chat.app.model.ChatMessage;
import com.chat.app.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatController {

    @Autowired
    private ChatMessageRepository repository;

    // ✅ Save + broadcast
    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        System.out.println("Saved: " + message.getContent());
        return repository.save(message); // ✅ DB save
    }

    // ✅ Load chat page + history
    @GetMapping("/chat")
    public String chat(Model model, @AuthenticationPrincipal OAuth2User principal) {

        if (principal != null) {
            model.addAttribute("username", principal.getAttribute("name"));
        }

        model.addAttribute("messages", repository.findAll()); // ✅ history
        return "chat";
    }
}