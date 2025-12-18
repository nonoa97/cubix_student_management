package hu.norbi.cubix.studentmanagement.ws;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatMessage {
    private long courseId;
    private String sender;
    private String message;
}
