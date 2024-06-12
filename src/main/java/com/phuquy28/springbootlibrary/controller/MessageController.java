package com.phuquy28.springbootlibrary.controller;

import com.phuquy28.springbootlibrary.entity.Message;
import com.phuquy28.springbootlibrary.requestmodels.AdminQuestionRequest;
import com.phuquy28.springbootlibrary.service.MessagesService;
import com.phuquy28.springbootlibrary.utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private MessagesService messagesService;

    @Autowired
    public MessageController(MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @PostMapping("/secure/add/message")
    public void postMessage(@RequestHeader(value = "Authorization") String token,
                            @RequestBody Message message) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        if (userEmail == null) {
            throw new Exception("User email is missing");
        }
        messagesService.postMessage(message, userEmail);
    }

    @PutMapping("/secure/admin/message")
    public void putMessage(@RequestHeader(value = "Authorization") String token,
                           @RequestBody AdminQuestionRequest adminQuestionRequest) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        String admin = ExtractJWT.payloadJWTExtraction(token, "\"userType\"");
        if (userEmail == null) {
            throw new Exception("User email is missing");
        }
        if (admin == null || !admin.equals("admin")) {
            throw new Exception("Administration page only");
        }
        messagesService.putMessage(adminQuestionRequest, userEmail);
    }
}
