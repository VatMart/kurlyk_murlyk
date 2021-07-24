package temp;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.User;

import java.io.IOException;

public class UserClassKeyDeserializer extends KeyDeserializer {

	@Override
	public User deserializeKey(String key, DeserializationContext ctxt) throws IOException {
		//System.out.println(key);
		ObjectMapper mapper = new ObjectMapper();
		User readedUser = mapper.readValue(key, User.class);
		//User test = new User(0L, "login1", "password1", "nickname1", readedUser.getRegistrationDate(), "regular");
		//System.out.println(test.equals(readedUser));
		return readedUser;
	}

}
