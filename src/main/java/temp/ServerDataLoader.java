package temp;

public class ServerDataLoader {
	private SaverServerData savedServerData;
	private ServerData serverData;

	public ServerDataLoader(SaverServerData savedServerData, ServerData serverData) {
		this.savedServerData = savedServerData;
		this.serverData = serverData;
	}

	public void loadData() {
		savedServerData.getListAllAccounts().forEach(user -> serverData.getListAllAccounts().add(user));
		savedServerData.getUsersTokens().forEach((user, pairToken) -> serverData.getUsersTokens().put(user, pairToken));
	}
}
