package br.ufscar.dc.dsw.mural.services;

import br.ufscar.dc.dsw.mural.domain.dtos.MessageAddForm;
import br.ufscar.dc.dsw.mural.domain.dtos.MessageListForm;
import br.ufscar.dc.dsw.mural.domain.entities.Message;
import br.ufscar.dc.dsw.mural.repositories.MessageRepository;
import br.ufscar.dc.dsw.mural.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public MessageService(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public List<MessageListForm> listMessages(String username, Collection<? extends GrantedAuthority> authorities) {
        boolean isAdmin = authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        // ADMIN ve todas as mensagens; USER ve apenas as que postou
        List<Message> authorizedMessages = isAdmin
                ? messageRepository.findAllOrderedByIdDesc()
                : messageRepository.findByUserUsername(username);

        return authorizedMessages.stream()
                .map(this::toListForm)
                .toList();
    }

    public MessageListForm save(MessageAddForm messageAddForm, String owner) {
        // validacao via codigo: from e to nao podem ser iguais (erro global do mural)
        if (messageAddForm.getFrom().trim().equalsIgnoreCase(messageAddForm.getTo().trim())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "From and to are the same");
        }

        var user = userRepository.findByUsername(owner)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        var message = new Message();
        message.setFrom(messageAddForm.getFrom());
        message.setTo(messageAddForm.getTo());
        message.setMessage(messageAddForm.getMessage());
        message.setTimestamp((new Date()).toString());
        message.setUser(user);
        message = messageRepository.save(message);

        return toListForm(message);
    }

    @Transactional
    public void deleteMessage(Long id, String currentUsername, Collection<? extends GrantedAuthority> authorities) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Message not found"));

        // O dono sempre pode apagar
        if (message.getUser().getUsername().equals(currentUsername)) {
            messageRepository.delete(message);
            return;
        }

        // ADMIN pode apagar mensagens de usuarios comuns (USER)
        boolean isAdmin = authorities.stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isOwnerUser = message.getUser().getRole().equals("USER");
        if (isAdmin && isOwnerUser) {
            messageRepository.delete(message);
            return;
        }

        throw new AccessDeniedException("You are not allowed to delete this message");
    }

    private MessageListForm toListForm(Message message) {
        var form = new MessageListForm();
        form.setId(message.getId());
        form.setFrom(message.getFrom());
        form.setTo(message.getTo());
        form.setMessage(message.getMessage());
        form.setTimestamp(message.getTimestamp());
        form.setOwner(message.getUser().getUsername());
        return form;
    }
}
