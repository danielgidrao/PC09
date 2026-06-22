package br.ufscar.dc.dsw.mural;

import br.ufscar.dc.dsw.mural.domain.entities.Message;
import br.ufscar.dc.dsw.mural.domain.entities.User;
import br.ufscar.dc.dsw.mural.repositories.MessageRepository;
import br.ufscar.dc.dsw.mural.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@SpringBootApplication
public class MuralApplication {

    private final static Logger logger = LoggerFactory.getLogger(MuralApplication.class);

    public static void main(String[] args) {
        var context = SpringApplication.run(MuralApplication.class, args);

        var userService = context.getBean(UserService.class);
        var messageRepository = context.getBean(MessageRepository.class);
        if (userService.count() == 0) {
            logger.info("No users found, adding some");

            var admin = userService.registerUser(new User("admin", "admin", "ADMIN"));
            messageRepository.save(seed("admin", "user", "Bem-vindos ao mural!", admin));

            var user1 = userService.registerUser(new User("user1", "user1", "USER"));
            messageRepository.save(seed("user1", "admin", "Ola, primeira mensagem.", user1));
            messageRepository.save(seed("user1", "user2", "Mensagem para o user2.", user1));

            var user2 = userService.registerUser(new User("user2", "user2", "USER"));
            messageRepository.save(seed("user2", "user1", "Respondendo ao user1.", user2));
        }
    }

    private static Message seed(String from, String to, String text, User owner) {
        var message = new Message(from, to, text, owner);
        message.setTimestamp((new Date()).toString());
        return message;
    }
}
