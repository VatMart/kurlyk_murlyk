package temp;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import entities.User;
import utils.TokenUtil.PairTokens;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@JsonAutoDetect
public class SaverServerData {
	
	private ServerData savingData;
	private List<User> listAllAccounts;
	@JsonDeserialize(keyUsing = UserClassKeyDeserializer.class)
	private ConcurrentHashMap<User, PairTokens> usersTokens;
	
	public SaverServerData(ServerData savingData) {
		this.savingData = savingData;
		initiateFields();
	}
	
	public SaverServerData() {}
	
	private void initiateFields() {
		listAllAccounts = Collections.synchronizedList(new LinkedList<User>());
		usersTokens = new ConcurrentHashMap<User, PairTokens>();
	}

	public void saveData() throws JsonGenerationException, JsonMappingException, IOException {
		File fileSaveData = new File(ServerData.DATA_FILE_NAME);
		//if (fileSaveData.exists()) fileSaveData.delete();
		savingData.getListAllAccounts().forEach(user ->  listAllAccounts.add(user));
		savingData.getUsersTokens().forEach((user, pairToken) -> usersTokens.put(user, pairToken));
		//SimpleModule simpleModule = new SimpleModule();
		//simpleModule.addKeyDeserializer(User.class, new UserClassKeyDeserializer());
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(fileSaveData, SaverServerData.this);
	}

	public List<User> getListAllAccounts() {
		return listAllAccounts;
	}

	public ConcurrentHashMap<User, PairTokens> getUsersTokens() {
		return usersTokens;
	}
}
