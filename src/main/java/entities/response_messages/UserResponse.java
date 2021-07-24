package entities.response_messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.User;

public class UserResponse {
    public UserResponse() {
    }

    public UserResponse(long user_ID, String nickname, long registrationDate, String user_role) {
        this.user_ID = user_ID;
        this.nickname = nickname;
        this.registrationDate = registrationDate;
        this.user_role = user_role;
    }

    public UserResponse(User user) {
        this.user_ID = user.getUser_ID();
        this.nickname = user.getNickname();
        this.registrationDate = user.getRegistrationDate();
        this.user_role = user.getUser_role();
    }

    private long user_ID;
    private String nickname;
    private long registrationDate;
    private String user_role;

    public long getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(long user_ID) {
        this.user_ID = user_ID;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public long getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(long registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getUser_role() {
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
