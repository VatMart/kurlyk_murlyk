package dao;

import entities.Image;
import entities.User;
import entities.response_messages.UserResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import services.ImageService;
import utils.HibernateSessionFactoryUtil;

import java.util.List;

public class UserDAOImpl implements UserDAO {

	@Override
	public User findById(long id) {
		return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(User.class, id);
	}

	@Override
	public UserResponse findByIdForResponse(long id) {
		Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
		User user = session.get(User.class, id);
		if (user == null) {
			session.close();
			return null;
		}
		UserResponse userResponse = new UserResponse(user);
		session.close();
		return userResponse;
	}

	@Override
	public User findByNickname(String nickname) {
		Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
		Query<User> query = session.createQuery("from User where nickname = :nickname");
		query.setParameter("nickname", nickname);
		User user;
		try {
			user = query.getSingleResult();
		} catch (javax.persistence.NoResultException e) {
			System.out.println(e.getMessage());
			return null;
		} finally {
			session.close();
		}
		return user;
	}

	@Override
	public UserResponse findByNicknameForResponse(String nickname) {
		Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
		Query<User> query = session.createQuery("from User where nickname = :nickname");
		query.setParameter("nickname", nickname);
		User user;
		try {
			user = query.getSingleResult();
		} catch (javax.persistence.NoResultException e) {
			System.out.println(e.getMessage());
			session.close();
			return null;
		}
		UserResponse userResponse = new UserResponse(user);
		session.close();
		return userResponse;
	}

	@Override
	public User findByLogin(String login) {
		Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
		Query<User> query = session.createQuery("from User where login = :login");
		query.setParameter("login", login);
		User user;
		try {
			user = query.getSingleResult();
		} catch (javax.persistence.NoResultException e) {
			System.out.println(e.getMessage());
			return null;
		} finally {
			session.close();
		}
		return user;
	}

	@Override
	public void addIconToUser(long id, String base64Icon) {
		Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
		User user = session.get(User.class, id);
		ImageService imageService = new ImageService();
		Image image = new Image(base64Icon);
		imageService.save(image);
		Transaction tx1 = session.beginTransaction();
		user.setImage(image);
		session.update(user);
		tx1.commit();
		session.close();
	}

	@Override
	public void save(User user) {
		Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
		Transaction tx1 = session.beginTransaction();
		session.save(user);
		tx1.commit();
		session.close();
	}

	@Override
	public void update(User user) {
		Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
		Transaction tx1 = session.beginTransaction();
		session.update(user);
		tx1.commit();
		session.close();
	}

	@Override
	public void delete(User user) {
		Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
		Transaction tx1 = session.beginTransaction();
		session.delete(user);
		tx1.commit();
		session.close();
	}

	@Override
	public List<User> findAll() {
		List<User> users = (List<User>) HibernateSessionFactoryUtil.getSessionFactory().openSession()
				.createQuery("From User").list();
		return users;
	}

}
