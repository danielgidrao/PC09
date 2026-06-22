package br.ufscar.dc.dsw.mural.controllers;

import br.ufscar.dc.dsw.mural.domain.dtos.MessageListForm;
import br.ufscar.dc.dsw.mural.services.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class ListMessageController {

    private final static Logger logger = LoggerFactory.getLogger(ListMessageController.class);

    private final MessageService messageService;

    public ListMessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public ResponseEntity<List<MessageListForm>> list(Authentication authentication) {
        logger.info("GET /api/messages User: {}", authentication.getName());
        var messages = messageService.listMessages(authentication.getName(), authentication.getAuthorities());

        return ResponseEntity.ok(messages);
    }
}
