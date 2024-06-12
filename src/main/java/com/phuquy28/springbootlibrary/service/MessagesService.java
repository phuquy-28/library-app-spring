package com.phuquy28.springbootlibrary.service;

import com.phuquy28.springbootlibrary.dao.MessageRepository;
import com.phuquy28.springbootlibrary.entity.Message;
import com.phuquy28.springbootlibrary.requestmodels.AdminQuestionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
public class MessagesService {

    private MessageRepository messageRepository;

    @Autowired
    public MessagesService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void postMessage(Message messageRequest, String userEmail) {
        Message message = new Message(messageRequest.getTitle(), messageRequest.getQuestion());
        message.setUserEmail(userEmail);
        messageRepository.save(message);
    }

    public void putMessage(AdminQuestionRequest adminQuestionRequest, String userEmail) throws Exception {
        Optional<Message> messageOptional = messageRepository.findById(adminQuestionRequest.getId());
        if (!messageOptional.isPresent()) {
            throw new Exception("Message not found");
        }
        Message message = messageOptional.get();
        message.setAdminEmail(userEmail);
        message.setResponse(adminQuestionRequest.getResponse());
        message.setClosed(true);
        messageRepository.save(message);
    }
}
