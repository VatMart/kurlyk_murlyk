package dao;

import entities.User;
import entities.response_messages.UserResponse;

import java.util.List;

public interface UserDAO {
	public User findById(long id);

	public UserResponse findByIdForResponse(long id);

	public User findByNickname(String nickname);

	public UserResponse findByNicknameForResponse(String nickname);

	public User findByLogin(String login);

	public void addIconToUser(long id, String base64Icon);

	public void save(User user);

	public void update(User user);

	public void delete(User user);

	public List<User> findAll();

}
