package entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import javax.transaction.Transactional;


@JsonAutoDetect
@Entity
@Table (name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private long user_ID;
	@Column(name = "login", unique = true)
	private String login;
	@Column(name = "password")
	private String password;
	@Column(name = "nickname", unique = true)
	private String nickname;
	@Column(name = "registrationdate")
	@ColumnDefault(value="CURRENT_TIMESTAMP")
	@Generated(GenerationTime.INSERT)
	@Convert(converter = utils.TimestampToLongConverter.class)
	private long registrationDate; // in millisec
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "img_id")
	private Image image;
	@Column(name = "user_role")
	private String user_role = "regular";
	
	public User(String login, String password, String nickname) {
		//this.user_ID = user_ID;
		this.login = login;
		this.password = password;
		this.nickname = nickname;
		//this.registrationDate = registrationDate;
		//this.user_role = user_role;
	}

	public User() {
	}

	public long getUser_ID() {
		return user_ID;
	}

	public void setUser_ID(long user_ID) {
		this.user_ID = user_ID;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
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
		// return "{\"user_ID\":\"" + user_ID + "\", \"login\":\""+login+"\",
		// \"password\":\""+password+"\", \"nickname\":\"" + nickname + "\",
		// \"registrationDate\":\"" + registrationDate + "\",
		// \"user_role\":\""+user_role+"\"}";
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		result = (int) (prime * result + user_ID);
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		User objUser = (User) obj;
		if (objUser.getLogin().equals(this.login) && objUser.getPassword().equals(this.password)
				&& Long.valueOf(objUser.getUser_ID()).equals(Long.valueOf(this.user_ID))
				&& objUser.getNickname().equals(this.nickname)
				&& Long.valueOf(objUser.getRegistrationDate()).equals(Long.valueOf(this.registrationDate))
				&& objUser.getUser_role().equals(this.user_role))
			return true;
		else
			return false;
	}
}
