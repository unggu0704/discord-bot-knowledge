package Bot.chatBot.Message.Filter;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

@Slf4j
public class MessageValidator {

    /**
     * 봇인지 또는 이미지 인지 검사
     * @param user
     * @param message
     * @return
     */
    public static boolean validateMessage(User user, Message message) {
        for (Message.Attachment attachment : message.getAttachments()) {
            String fileName = attachment.getFileName();
            if (user.isBot() || fileName != null && (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg"))) {
               log.info("Bot이 말하는 것은 측정하지 않음");

                return true;
            }
        }
        return false;
    }
}
