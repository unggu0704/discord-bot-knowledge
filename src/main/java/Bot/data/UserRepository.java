package Bot.data;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Member;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserRepository {
    private final String csvFilePath = "UserData.csv";
    private final List<UserData> userReository = new ArrayList<>();


    public UserRepository() {
        loadCSVUser();
    }

    public void saveUser(UserData userData) {
        userReository.add(userData);
    }

    public Member getMemberByName(String name) {
        for (UserData userData : userReository) {
            if (userData.getName().equals(name)) {
                return userData.getMember();
            }
        }
        return null;
    }

    public void updateUser() {

    }

    public int getUserCount() {
        return userReository.size();
    }

    public boolean findUser(UserData userdata) {
        return userReository.contains(userdata);
    }

    public void loadCSVUser() {

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String name;
            reader.readLine();
            while ((name = reader.readLine()) != null) {
                UserData userInfo = new UserData(name);
                userReository.add(userInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveCSVUser() {
        try (FileWriter writer = new FileWriter(csvFilePath)) {

            for (UserData user : userReository) {
                log.info("User 저장완료");
                writer.write(user.getName() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printUser() {
        for(UserData user : userReository) {
            System.out.println(user.getName());
        }
    }


}
