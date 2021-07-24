package server;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import entities.*;
import entities.received_messages.*;
import entities.response_messages.UserResponse;
import io.jsonwebtoken.security.InvalidKeyException;
import utils.TokenUtil;
import utils.TokenUtil.AccessToken;
import utils.TokenUtil.PairTokens;
import utils.TokenUtil.RefreshToken;
import services.*;

import java.io.IOException;

public class DataController {

	public DataController() {

	}

	public String registerUser(RegistrationData data) {
		// TODO check input data
		// TODO check serverData
		StringBuilder sb = new StringBuilder();

		UserService userService = new UserService();
		boolean isLoginExists = (userService.findByLogin(data.getLogin()) != null);
		boolean isNicknameExists = (userService.findByNickname(data.getNickname()) != null);
		if (isLoginExists)
			sb.append("This login is already taken. ");
		if (isNicknameExists)
			sb.append("This nickname is already taken. ");
		if (isLoginExists || isNicknameExists)
			return sb.toString();
		// TODO adding User
		User newUser = new User(data.getLogin(), data.getPassword(), data.getNickname());
		userService.save(newUser);
		return sb.toString();
	}

	public User authentication(AuthData data) {
		UserService userService = new UserService();
		User user = userService.findByLogin(data.getLogin());
		boolean isLoginExists = (user != null);
		if (!isLoginExists)
			return null;
		boolean checkPassword = user.getPassword().equals(data.getPassword());
		if (!checkPassword)
			return null;
		return user;
	}

	public PairTokens refreshTokens(RefreshToken refreshToken) {
		User user = getUser(refreshToken.getUser_ID());
		if (user == null) return null;
		PairTokens pair;
		try {
			pair = TokenUtil.generateTokenPair(user);
		} catch (InvalidKeyException | IOException e) {
			e.printStackTrace();
			return null;
		}
		return pair;
	}

	/**
	 * ���������� ������ � ���������� �� � ��. ������ ���� ������ �����
	 * �������������� � ������� ������ authentication ������ DataController
	 * 
	 * @param data
	 * @return Pair of ref and acc tokens
	 * @throws InvalidKeyException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public PairTokens authorization(User data)
			throws InvalidKeyException, JsonParseException, JsonMappingException, IOException {
		PairTokens pairToken = TokenUtil.generateTokenPair(data);
		return pairToken;
	}

	public String createPost(PostMsg msg, long user_id) {
		StringBuilder sb = new StringBuilder();
		PostService postService = new PostService();
		UserService userService = new UserService();
		boolean isTitleExist = (postService.findByTitle(msg.getTitle()) != null);
		User user = userService.findById(user_id);
		boolean isUserExist = (user != null);
		if (!isUserExist) {
			sb.append("User is not exist in database. ");
		}
		if (isTitleExist) {
			sb.append("This title is already taken. ");
		}
		if (!isUserExist || isTitleExist) return sb.toString();
		Post post = new Post(msg.getTitle(), user, msg.getText_content(), msg.getTags());
		post.setDate_editing(-1L);
		//msg.getTags().stream().forEach(str -> System.out.println(str));
		postService.save(post);
		return sb.toString();
	}

	public String createComment(CommentReceived message, long user_id, long post_id) {
		StringBuilder sb = new StringBuilder();
		CommentService commentService = new CommentService();
		PostService postService = new PostService();
		UserService userService = new UserService();
		Post post = postService.findById(post_id);
		boolean isPostExist = (post != null);
		User user = userService.findById(user_id);
		boolean isUserExist = (user != null);
		if (!isUserExist) {
			sb.append("User is not exist in database. ");
		}
		if (!isPostExist) {
			sb.append("Post is not exist in database. ");
		}
		//System.out.println(message.getParent_comment_id()+"; "+ message.getText_content());
		if (!isUserExist || !isPostExist) return sb.toString();
		Comment comment = new Comment(post, user, message.getParent_comment_id(), message.getText_content());
		commentService.save(comment);
		return sb.toString();
	}

	public String addLike(long user_id, long post_id) {
		StringBuilder sb = new StringBuilder();
		LikeService likeService = new LikeService();
		DislikeService dislikeService = new DislikeService();
		PostService postService = new PostService();
		UserService userService = new UserService();
		Post post = postService.findById(post_id);
		User user = userService.findById(user_id);
		boolean isPostExist = (post != null);
		boolean isUserExist = (user != null);
		if (!isUserExist) {
			sb.append("User is not exist in database. ");
		}
		if (!isPostExist) {
			sb.append("Post is not exist in database. ");
		}
		if (!isUserExist || !isPostExist) return sb.toString();
		if (likeService.findByUserAndPost(user_id, post_id) != null) {
			sb.append("User already put like on this post. ");
			return sb.toString();
		}
		Dislike dislike = dislikeService.findByUserAndPost(user_id, post_id);
		if (dislike != null) {
			dislikeService.delete(dislike);
		}
		Like like = new Like(user, post);
		likeService.save(like);
		return sb.toString();
	}

	public String addDislike(long user_id, long post_id) {
		StringBuilder sb = new StringBuilder();
		DislikeService dislikeService = new DislikeService();
		LikeService likeService = new LikeService();
		PostService postService = new PostService();
		UserService userService = new UserService();
		Post post = postService.findById(post_id);
		User user = userService.findById(user_id);
		boolean isPostExist = (post != null);
		boolean isUserExist = (user != null);
		if (!isUserExist) {
			sb.append("User is not exist in database. ");
		}
		if (!isPostExist) {
			sb.append("Post is not exist in database. ");
		}
		if (!isUserExist || !isPostExist) return sb.toString();
		if (dislikeService.findByUserAndPost(user_id, post_id) != null) {
			sb.append("User already put dislike on this post. ");
			return sb.toString();
		}
		Like like = likeService.findByUserAndPost(user_id, post_id);
		if (like != null) {
			likeService.delete(like);
		}
		Dislike dislike = new Dislike(user, post);
		dislikeService.save(dislike);
		return sb.toString();
	}

	public String addUserIcon(long user_id, ImageIconReceived message) {
		StringBuilder sb = new StringBuilder();
		UserService userService = new UserService();
		ImageService imageService = new ImageService();
		User user = userService.findById(user_id);
		boolean isUserExist = (user != null);
		if (!isUserExist) {
			sb.append("User is not exist in database. ");
		}
		userService.addIconToUser(user_id, message.getIcon_image());
		return sb.toString();
	}

	public String deleteLike(long user_id, long post_id) {
		StringBuilder sb = new StringBuilder();
		LikeService likeService = new LikeService();
		PostService postService = new PostService();
		UserService userService = new UserService();
		Post post = postService.findById(post_id);
		User user = userService.findById(user_id);
		boolean isPostExist = (post != null);
		boolean isUserExist = (user != null);
		if (!isUserExist) {
			sb.append("User is not exist in database. ");
		}
		if (!isPostExist) {
			sb.append("Post is not exist in database. ");
		}
		if (!isUserExist || !isPostExist) return sb.toString();
		if (likeService.findByUserAndPost(user_id, post_id) == null) {
			sb.append("User didn't put like on this post. ");
			return sb.toString();
		}
		Like like = likeService.findByUserAndPost(user_id, post_id);
		likeService.delete(like);
		return sb.toString();
	}

	public String deleteDislike(long user_id, long post_id) {
		StringBuilder sb = new StringBuilder();
		DislikeService dislikeService = new DislikeService();
		PostService postService = new PostService();
		UserService userService = new UserService();
		Post post = postService.findById(post_id);
		User user = userService.findById(user_id);
		boolean isPostExist = (post != null);
		boolean isUserExist = (user != null);
		if (!isUserExist) {
			sb.append("User is not exist in database. ");
		}
		if (!isPostExist) {
			sb.append("Post is not exist in database. ");
		}
		if (!isUserExist || !isPostExist) return sb.toString();
		if (dislikeService.findByUserAndPost(user_id, post_id) == null) {
			sb.append("User didn't put dislike on this post. ");
			return sb.toString();
		}
		Dislike dislike = dislikeService.findByUserAndPost(user_id, post_id);
		dislikeService.delete(dislike);
		return sb.toString();
	}

	public boolean checkExpAccessToken(AccessToken decodedToken) {
		if (decodedToken == null)
			return false;
		return decodedToken.getExpiration() >= System.currentTimeMillis() / 1000;
	}

	public boolean checkExpRefreshToken(RefreshToken decodedToken) {
		if (decodedToken == null)
			return false;
		return decodedToken.getExpiration() >= System.currentTimeMillis() / 1000;
	}

	public User getUser(long id) {
		UserService userService = new UserService();
		return userService.findById(id);
	}

	public UserResponse getUserResponse(long id) {
		UserService userService = new UserService();
		return userService.findByIdForResponse(id);
	}
	public UserResponse getUserResponse(String nickname) {
		UserService userService = new UserService();
		return userService.findByNicknameForResponse(nickname);
	}
}
