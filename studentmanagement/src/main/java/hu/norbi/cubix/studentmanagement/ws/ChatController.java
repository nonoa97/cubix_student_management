package hu.norbi.cubix.studentmanagement.ws;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat")
    @PreAuthorize("#message.sender == authentication.principal.username")
    public void send(ChatMessage message) {
        messagingTemplate.convertAndSend(
                "/topic/courseChat/" + message.getCourseId(),
                String.format("%s: %s", message.getSender(), message.getMessage()));
    }

    }


