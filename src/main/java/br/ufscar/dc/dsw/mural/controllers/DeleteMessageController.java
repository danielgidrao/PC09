package br.ufscar.dc.dsw.mural.controllers;

import br.ufscar.dc.dsw.mural.services.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class DeleteMessageController {

    private final static Logger log = LoggerFactory.getLogger(DeleteMessageController.class);

    private final MessageService messageService;

    public DeleteMessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        log.info("DELETE /api/messages with id {}", id);

        messageService.deleteMessage(id, authentication.getName(), authentication.getAuthorities());

        return ResponseEntity.noContent().build();
    }
}
