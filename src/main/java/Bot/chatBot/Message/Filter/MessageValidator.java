package Bot.chatBot.Message.Filter;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;


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
                return true;
            }
        }
        return false;
    }
}
