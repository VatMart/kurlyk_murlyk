package services;

import dao.UserDAO;
import dao.UserDAOImpl;
import entities.Image;
import entities.User;
import entities.response_messages.UserResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateSessionFactoryUtil;

import java.util.List;

//Service � ���� ������ � ����������, ���������� �� ���������� ������-������. 
public class UserService {
	
	private UserDAO usersDAO = new UserDAOImpl();
	
//	public UserService(UserDAO userDAO) {
//		this.usersDAO = userDAO;
//	}

	public User findById(long id) {
		return usersDAO.findById(id);
	}

	public UserResponse findByIdForResponse(long id) {
		return usersDAO.findByIdForResponse(id);
	}

	public User findByNickname(String nickname) {
		return usersDAO.findByNickname(nickname);
	}

	public UserResponse findByNicknameForResponse(String nickname) {
		return usersDAO.findByNicknameForResponse(nickname);
	}

	public User findByLogin(String login) {
		return usersDAO.findByLogin(login);
	}

	public void addIconToUser(long id, String base64Icon) {
		usersDAO.addIconToUser(id, base64Icon);
	}

	public void save(User user) {
		usersDAO.save(user);
	}

	public void update(User user) {
		usersDAO.update(user);
	}

	public void delete(User user) {
		usersDAO.delete(user);
	}

	public List<User> findAll() {
		return usersDAO.findAll();
	}

}
