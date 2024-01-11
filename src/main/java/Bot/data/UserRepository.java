package Bot.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Member;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserRepository {
    private final String jsonFilePath = "UserData.json";
    private final List<UserData> userRepository  = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();


    public UserRepository() {
        loadJsonUser();
    }

    public void saveUser(UserData userData) {
        userRepository.add(userData);
        saveJsonUser();
    }

    public void saveJsonUser() {
        synchronized (userRepository) {
            try {
                objectMapper.writeValue(new File(jsonFilePath), userRepository);
                log.info("User 저장완료");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Member getMemberByName(String name) {
        for (UserData userData : userRepository) {
            if (userData.getName().equals(name)) {
                return userData.getMember();
            }
        }
        return null;
    }

    public void loadJsonUser() {
        File jsonFile = new File(jsonFilePath);

        // 파일이 존재하지 않으면 빈 userRepository를 생성
        if (!jsonFile.exists()) {
            log.info("Json 데이터가 없음.");
            return;
        }

        try {
            // JSON 파일을 읽어 List<UserData>로 변환
            List<UserData> loadedUsers = objectMapper.readValue(jsonFile,
                    new TypeReference<>() {
                    });

            // 읽어온 데이터를 userRepository에 추가
            System.out.println("도달");

            for (UserData userData : loadedUsers) {
                log.info(userData.getName());
            }
            userRepository.addAll(loadedUsers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getUserCount() {
        return userRepository.size();
    }

    public boolean findUser(UserData userdata) {
        return userRepository.contains(userdata);
    }

    public void printUser() {
        for(UserData user : userRepository) {
            System.out.println(user.getName());
        }
    }


}
