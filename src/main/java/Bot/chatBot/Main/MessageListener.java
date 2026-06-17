package Bot.chatBot.Main;

import Bot.chatBot.Message.Processor.MessageProcessor;
import Bot.data.UserRepository;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class MessageListener extends ListenerAdapter {

    private final UserRepository userRepository;

    public MessageListener(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        log.info("로그 : 메세지 감지!");
        MessageProcessor.processReceivedMessage(userRepository, event);
    }
}
