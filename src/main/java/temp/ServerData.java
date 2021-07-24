package temp;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.User;
import utils.TokenUtil.PairTokens;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ServerData {
	public static final String DATA_FILE_NAME = "dataServerFile";
	private static volatile ServerData instance;

	private List<User> listAllAccounts;
	private ConcurrentHashMap<User, PairTokens> usersTokens;

	public static synchronized ServerData getInstance() {
		if (instance == null) {
			instance = new ServerData();
		}
		return instance;
	}

	private ServerData() {
		// TODO (?)
		initiate();
		System.out.println("Loading server data...");
		try {
			System.out.println(tryLoadData());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String tryLoadData() throws JsonParseException, JsonMappingException, IOException {
		File dataServerFile = new File(DATA_FILE_NAME);
		if (dataServerFile.exists()) {
			ObjectMapper mapper = new ObjectMapper();
			SaverServerData savedData = mapper.readValue(dataServerFile, SaverServerData.class);
			ServerDataLoader loader = new ServerDataLoader(savedData, this);
			loader.loadData();
			return "Server data was loaded";
		} else
			return "Server data was not found";
	}

	private void initiate() {
		listAllAccounts = Collections.synchronizedList(new LinkedList<User>());
		usersTokens = new ConcurrentHashMap<User, PairTokens>();
	}

	public List<User> getListAllAccounts() {
		return listAllAccounts;
	}

	public ConcurrentHashMap<User, PairTokens> getUsersTokens() {
		return usersTokens;
	}

}
