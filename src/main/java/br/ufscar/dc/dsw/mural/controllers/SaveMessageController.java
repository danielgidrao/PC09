package br.ufscar.dc.dsw.mural.controllers;

import br.ufscar.dc.dsw.mural.domain.dtos.MessageAddForm;
import br.ufscar.dc.dsw.mural.domain.dtos.MessageListForm;
import br.ufscar.dc.dsw.mural.services.MessageService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class SaveMessageController {

    private final static Logger log = LoggerFactory.getLogger(SaveMessageController.class);

    private final MessageService messageService;

    public SaveMessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<MessageListForm> save(@RequestBody @Valid MessageAddForm messageAddForm,
                                                Authentication authentication) {
        log.info("POST /api/messages - {}", messageAddForm);
        return ResponseEntity.ok(
                messageService.save(messageAddForm, authentication.getName())
        );
    }
}
