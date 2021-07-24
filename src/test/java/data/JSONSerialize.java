package data;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JSONSerialize {

	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
		// TODO Auto-generated method stub
		//UUID id_user = UUID.randomUUID();
		//System.out.println(id_user.toString());
		//User testUser1 = new User(1L, "login1", "password", "nickname", new Date().getTime(), "regular");
		//User testUser2 = new User(1L, "login1", "password", "nickname", new Date().getTime(), "regular");
		//System.out.println(testUser1.equals(testUser2));
		ObjectMapper mapper = new ObjectMapper();
		//User readedUser = mapper.readValue(testUser1.toString(), User.class);
		//System.out.println(readedUser.equals(testUser1));
		
		
		//mapper.getFactory().setRootValueSeparator("\n");
		//JsonFactory jfactory = new JsonFactory();
		//JsonGenerator jGenerator = jfactory.createGenerator(new File("TestUserFile.json"), JsonEncoding.UTF8);
		//jGenerator.setPrettyPrinter(new MinimalPrettyPrinter("\n"));
		//mapper.setDefaultMergeable(true);
		//;
		//mapper.writeValue(jGenerator, testUser);
		
		//User desUser = mapper.readValue(new File("TestUserFile"), User.class);
		//System.out.println(desUser.toString());
	}

}
