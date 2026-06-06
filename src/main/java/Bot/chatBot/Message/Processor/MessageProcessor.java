package Bot.chatBot.Message.Processor;

import Bot.chatBot.Message.Filter.MessageValidator;
import Bot.chatBot.Message.Filter.MessageFilter;
import Bot.data.UserData;
import Bot.data.UserRepository;
import Bot.features.Command.FrontCommand;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MessageProcessor {

    public static void processReceivedMessage(UserRepository userRepository, MessageReceivedEvent event) {
        if (MessageValidator.validateMessage(event.getAuthor(), event.getMessage()))
            return;

        User user = event.getAuthor();
        UserData userData = new UserData(user.getName(), event.getMember(), user.getId(), user.getDiscriminator(), user.getAsMention());

        if (!userRepository.findUser(userData))
            userRepository.saveUser(userData);

        String messageContent = event.getMessage().getContentRaw();
        if (MessageFilter.containsNegativeSpeech(messageContent)) {
            MessageFilter.profanityFilter(event, userRepository, userData);
            return;
        }

        if (messageContent.isEmpty() || messageContent.charAt(0) != '!')
            return;

        FrontCommand.handleCommand(userRepository, messageContent.substring(1), event);
    }
}
