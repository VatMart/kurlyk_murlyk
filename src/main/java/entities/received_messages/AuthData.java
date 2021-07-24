package entities.received_messages;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class AuthData {
	private String login;
	private String password;

	public AuthData() {
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

}
