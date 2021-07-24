package server;

import com.github.vanbv.num.annotation.*;
import com.github.vanbv.num.json.JsonParser;
import entities.User;
import entities.response_messages.*;
import entities.received_messages.*;
import io.jsonwebtoken.security.InvalidKeyException;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import utils.TokenUtil;
import utils.TokenUtil.AccessToken;
import utils.TokenUtil.PairTokens;
import utils.TokenUtil.RefreshToken;
import services.CommentService;
import services.PostService;
import temp.SaverServerData;
import temp.ServerData;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpMappedHandler extends CustomHttpHandler {

	public HttpMappedHandler(JsonParser parser) {
		super(parser);
	}

	@Post("/test/{id}/test2")
	public DefaultFullHttpResponse save(@PathParam(value = "id") long id,
										@QueryParam(value = "q") int q,
										@RequestBody RegistrationData message,
										HttpRequest request) {
		System.out.println("message="+message+"; id=" +id + "; " +"q=" + q);
		return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
				Unpooled.copiedBuffer("", StandardCharsets.UTF_8));
	}

	@Post("/registration")
	public DefaultFullHttpResponse registration(@RequestBody RegistrationData message, HttpRequest request) {
		// TODO
		DataController controller = new DataController();
		String responseMsg = controller.registerUser(message);
		if (!responseMsg.isEmpty()) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNPROCESSABLE_ENTITY,
					Unpooled.copiedBuffer(responseMsg, StandardCharsets.UTF_8));
		}
		return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
	}

	@Post("/auth")
	public DefaultFullHttpResponse auth(@RequestBody AuthData message, HttpRequest request) {
		// TODO
		DataController controller = new DataController();
		// 1. ��������� ������ ������������ � �� (controller)
		User authentUser = controller.authentication(message);
		if (authentUser == null) {
			// TODO
			System.out.println("������������ ��� � ����");
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND, Unpooled
					.copiedBuffer("User not found in database or error in login or password", StandardCharsets.UTF_8));
		}
		// 2. ������������� ������ � �������� �� � ��
		PairTokens pair = null;
		try {
			pair = controller.authorization(authentUser);
		} catch (InvalidKeyException | IOException e1) {
			e1.printStackTrace();
			// TODO
		}
		if (pair == null)
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR,
					Unpooled.copiedBuffer("Server error when generating a pair of tokens", StandardCharsets.UTF_8));

		// DELETE THIS LATER
		SaverServerData saver = new SaverServerData(ServerData.getInstance());
		try {
			saver.saveData();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 3. �������� �� � ���� json
		String responseMsg = TokenUtil.tokenPairToJsonString(pair);
		return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
				Unpooled.copiedBuffer(responseMsg, StandardCharsets.UTF_8));
	}

	@Post("/auth/refreshToken")
	public DefaultFullHttpResponse refreshToken(@RequestBody MessageRefreshToken message, HttpRequest request) {
		// System.out.println("TEST");
		DataController controller = new DataController();
		RefreshToken refToken = TokenUtil.decodeRefreshToken(message.getRefreshToken());
		if (refToken == null) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST,
					Unpooled.copiedBuffer("Token format error or token is not valid", StandardCharsets.UTF_8));
		}

		// System.out.println(refToken.getToken());
		if (!controller.checkExpRefreshToken(refToken)) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST,
					Unpooled.copiedBuffer("The token has expired", StandardCharsets.UTF_8));
		}
		PairTokens pair = controller.refreshTokens(refToken);
		if (pair == null) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR,
					Unpooled.copiedBuffer("Server error when generating a pair of tokens", StandardCharsets.UTF_8));
		}
		String jsonResponse = TokenUtil.tokenPairToJsonString(pair);
		return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
				Unpooled.copiedBuffer(jsonResponse, StandardCharsets.UTF_8));
	}

	@Post("/post")
	public DefaultFullHttpResponse posting(@RequestBody PostMsg message, HttpRequest request) {
		DataController controller = new DataController();
		// 1. CHECKING TOKEN
		if (!request.headers().contains("Authorization")) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED,
					Unpooled.copiedBuffer("Authorization required. There is no Authorization field in the header",
							StandardCharsets.UTF_8));
		}
		String headerAuthorization = request.headers().get("Authorization");
		AccessToken accToken = TokenUtil.decodeAccessToken(headerAuthorization);
		if (accToken == null) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST,
					Unpooled.copiedBuffer("Token format error or token is not valid", StandardCharsets.UTF_8));
		}
		if (!controller.checkExpAccessToken(accToken)) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED,
					Unpooled.copiedBuffer("The token has expired", StandardCharsets.UTF_8));
		}
		// 2. CHECKING INPUT DATA
		// TODO

		// 3. PUT POST IN DATABASE
		String errorStroke = controller.createPost(message, accToken.getUser_ID());
		if (!errorStroke.isEmpty()) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST,
					Unpooled.copiedBuffer(errorStroke, StandardCharsets.UTF_8));
		}

		return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
	}

	@Post("/comment/{post_id}")
	public DefaultFullHttpResponse commenting(@PathParam(value = "post_id") long post_id, @RequestBody CommentReceived message, HttpRequest request) {
		DataController controller = new DataController();
		// 1. CHECKING TOKEN
		if (!request.headers().contains("Authorization")) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED,
					Unpooled.copiedBuffer("Authorization required. There is no Authorization field in the header",
							StandardCharsets.UTF_8));
		}
		String headerAuthorization = request.headers().get("Authorization");
		AccessToken accToken = TokenUtil.decodeAccessToken(headerAuthorization);
		if (accToken == null) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST,
					Unpooled.copiedBuffer("Token format error or token is not valid", StandardCharsets.UTF_8));
		}
		if (!controller.checkExpAccessToken(accToken)) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED,
					Unpooled.copiedBuffer("The token has expired", StandardCharsets.UTF_8));
		}
		// 2. CHECKING INPUT DATA
		// TODO

		// 3. PUT COMMENT IN DATABASE
		String errorStroke = controller.createComment(message, accToken.getUser_ID(), post_id);
		if (!errorStroke.isEmpty()) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST,
					Unpooled.copiedBuffer(errorStroke, StandardCharsets.UTF_8));
		}
		return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
	}

	@Post("/like/post/{post_id}")
	public DefaultFullHttpResponse likePost(@PathParam(value = "post_id") long post_id, HttpRequest request) {
		DataController controller = new DataController();
		// 1. CHECKING TOKEN
		if (!request.headers().contains("Authorization")) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED,
					Unpooled.copiedBuffer("Authorization required. There is no Authorization field in the header",
							StandardCharsets.UTF_8));
		}
		String headerAuthorization = request.headers().get("Authorization");
		AccessToken accToken = TokenUtil.decodeAccessToken(headerAuthorization);
		if (accToken == null) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST,
					Unpooled.copiedBuffer("Token format error or token is not valid", StandardCharsets.UTF_8));
		}
		if (!controller.checkExpAccessToken(accToken)) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED,
					Unpooled.copiedBuffer("The token has expired", StandardCharsets.UTF_8));
		}
		// 3. PUT COMMENT IN DATABASE
		String errorStroke = controller.addLike(accToken.getUser_ID(), post_id);
		if (!errorStroke.isEmpty()) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST,
					Unpooled.copiedBuffer(errorStroke, StandardCharsets.UTF_8));
		}
		return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
	}

	@Post("/dislike/post/{post_id}")
	public DefaultFullHttpResponse dislikePost(@PathParam(value = "post_id") long post_id, HttpRequest request) {
		DataController controller = new DataController();
		// 1. CHECKING TOKEN
		if (!request.headers().contains("Authorization")) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED,
					Unpooled.copiedBuffer("Authorization required. There is no Authorization field in the header",
							StandardCharsets.UTF_8));
		}
		String headerAuthorization = request.headers().get("Authorization");
		AccessToken accToken = TokenUtil.decodeAccessToken(headerAuthorization);
		if (accToken == null) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST,
					Unpooled.copiedBuffer("Token format error or token is not valid", StandardCharsets.UTF_8));
		}
		if (!controller.checkExpAccessToken(accToken)) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED,
					Unpooled.copiedBuffer("The token has expired", StandardCharsets.UTF_8));
		}
		// 3. PUT COMMENT IN DATABASE
		String errorStroke = controller.addDislike(accToken.getUser_ID(), post_id);
		if (!errorStroke.isEmpty()) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST,
					Unpooled.copiedBuffer(errorStroke, StandardCharsets.UTF_8));
		}
		return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
	}

	@Delete("/like/post/{post_id}")
	public DefaultFullHttpResponse deleteLikePost(@PathParam(value = "post_id") long post_id, HttpRequest request) {
		DataController controller = new DataController();
		// 1. CHECKING TOKEN
		if (!request.headers().contains("Authorization")) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED,
					Unpooled.copiedBuffer("Authorization required. There is no Authorization field in the header",
							StandardCharsets.UTF_8));
		}
		String headerAuthorization = request.headers().get("Authorization");
		AccessToken accToken = TokenUtil.decodeAccessToken(headerAuthorization);
		if (accToken == null) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST,
					Unpooled.copiedBuffer("Token format error or token is not valid", StandardCharsets.UTF_8));
		}
		if (!controller.checkExpAccessToken(accToken)) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED,
					Unpooled.copiedBuffer("The token has expired", StandardCharsets.UTF_8));
		}
		// 3. PUT COMMENT IN DATABASE
		String errorStroke = controller.deleteLike(accToken.getUser_ID(), post_id);
		if (!errorStroke.isEmpty()) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST,
					Unpooled.copiedBuffer(errorStroke, StandardCharsets.UTF_8));
		}
		return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
	}

	@Delete("/dislike/post/{post_id}")
	public DefaultFullHttpResponse deleteDislikePost(@PathParam(value = "post_id") long post_id, HttpRequest request) {
		DataController controller = new DataController();
		// 1. CHECKING TOKEN
		if (!request.headers().contains("Authorization")) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED,
					Unpooled.copiedBuffer("Authorization required. There is no Authorization field in the header",
							StandardCharsets.UTF_8));
		}
		String headerAuthorization = request.headers().get("Authorization");
		AccessToken accToken = TokenUtil.decodeAccessToken(headerAuthorization);
		if (accToken == null) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST,
					Unpooled.copiedBuffer("Token format error or token is not valid", StandardCharsets.UTF_8));
		}
		if (!controller.checkExpAccessToken(accToken)) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED,
					Unpooled.copiedBuffer("The token has expired", StandardCharsets.UTF_8));
		}
		// 3. PUT COMMENT IN DATABASE
		String errorStroke = controller.deleteDislike(accToken.getUser_ID(), post_id);
		if (!errorStroke.isEmpty()) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST,
					Unpooled.copiedBuffer(errorStroke, StandardCharsets.UTF_8));
		}
		return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
	}

	@Get("/user/{id}")
	public DefaultFullHttpResponse getUser(@PathParam(value = "id") long id, HttpRequest request) {
		DataController controller = new DataController();
		//System.out.println(id);
		UserResponse userResponse = controller.getUserResponse(id);
		if (userResponse == null) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND,
					Unpooled.copiedBuffer("User with this id not found", StandardCharsets.UTF_8));
		}
		DefaultFullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
				Unpooled.copiedBuffer(userResponse.toString(), StandardCharsets.UTF_8));
		fullHttpResponse.headers().set("Content-Type", "application/json");
		return fullHttpResponse;
	}

	@Get("/post/{id}")
	public DefaultFullHttpResponse getPost(@PathParam(value = "id") long id, HttpRequest request) {
		PostService postService = new PostService();
		//System.out.println(id);
		PostResponse postResponse = postService.findByIdForResponse(id);
		if (postResponse == null) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND,
					Unpooled.copiedBuffer("Post with this id not found", StandardCharsets.UTF_8));
		}
		DefaultFullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
				Unpooled.copiedBuffer(postResponse.toString(), StandardCharsets.UTF_8));
		fullHttpResponse.headers().set("Content-Type", "application/json");
		return fullHttpResponse;
	}

	@Get("/comment/{comment_id}")
	public DefaultFullHttpResponse getComment(@PathParam(value = "comment_id") long id, HttpRequest request) {
		CommentService commentService = new CommentService();
		//System.out.println(id);
		CommentResponse commentResponse = commentService.findByIdForResponse(id);
		if (commentResponse == null) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND,
					Unpooled.copiedBuffer("Comment with this id not found", StandardCharsets.UTF_8));
		}
		DefaultFullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
				Unpooled.copiedBuffer(commentResponse.toString(), StandardCharsets.UTF_8));
		fullHttpResponse.headers().set("Content-Type", "application/json");
		return fullHttpResponse;
	}

	@Get("/posts")
	public DefaultFullHttpResponse posts(@QueryParam(value = "from") int from,
										   @QueryParam(value = "to") int to,
										   @QueryParam(value = "order") String orderStroke,
										   HttpRequest request) {
		if (from < 0 || to < 0) return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST,
				Unpooled.copiedBuffer("Mistake in query parameters", StandardCharsets.UTF_8));
		PostsResponse.SortingType order;
		try {
			order = PostsResponse.SortingType.valueOf(orderStroke);
		} catch(IllegalArgumentException e) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST,
					Unpooled.copiedBuffer("Not supported query parameter 'order' type", StandardCharsets.UTF_8));
		}
		PostService postService = new PostService();
		List<PostResponse> posts = postService.getByOrder(from, to, order);
		PostsResponse postsResponse = new PostsResponse(posts, order, from, to);
		DefaultFullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
				Unpooled.copiedBuffer(postsResponse.toString(), StandardCharsets.UTF_8));
		fullHttpResponse.headers().set("Content-Type", "application/json");
		return fullHttpResponse;
	}

	@Get("/comments/{post_id}")
	public DefaultFullHttpResponse comments(@PathParam(value = "post_id") long id,
										 @QueryParam(value = "from") int from,
										 @QueryParam(value = "to") int to,
										 @QueryParam(value = "order") String orderStroke,
										 HttpRequest request) {
		if (from < 0 || to < 0) return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST,
				Unpooled.copiedBuffer("Mistake in query parameters", StandardCharsets.UTF_8));
		CommentsResponse.SortingType order;
		try {
			order = CommentsResponse.SortingType.valueOf(orderStroke);
		} catch(IllegalArgumentException e) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST,
					Unpooled.copiedBuffer("Not supported query parameter 'order' type", StandardCharsets.UTF_8));
		}
		CommentService commentService = new CommentService();
		List<CommentResponse> commentResponses = commentService.getByOrder(id, from, to, order);
		CommentsResponse commentsResponse = new CommentsResponse(commentResponses, order, from, to);
		DefaultFullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
				Unpooled.copiedBuffer(commentsResponse.toString(), StandardCharsets.UTF_8));
		fullHttpResponse.headers().set("Content-Type", "application/json");
		return fullHttpResponse;
	}

	@Put("/user/image")
	public DefaultFullHttpResponse putUserIcon(@RequestBody ImageIconReceived message, HttpRequest request) {
		DataController controller = new DataController();
		// 1. CHECKING TOKEN
		if (!request.headers().contains("Authorization")) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED,
					Unpooled.copiedBuffer("Authorization required. There is no Authorization field in the header",
							StandardCharsets.UTF_8));
		}
		String headerAuthorization = request.headers().get("Authorization");
		AccessToken accToken = TokenUtil.decodeAccessToken(headerAuthorization);
		if (accToken == null) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST,
					Unpooled.copiedBuffer("Token format error or token is not valid", StandardCharsets.UTF_8));
		}
		if (!controller.checkExpAccessToken(accToken)) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED,
					Unpooled.copiedBuffer("The token has expired", StandardCharsets.UTF_8));
		}
		// 3. PUT COMMENT IN DATABASE
		String errorStroke = controller.addUserIcon(accToken.getUser_ID(), message);
		if (!errorStroke.isEmpty()) {
			return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST,
					Unpooled.copiedBuffer(errorStroke, StandardCharsets.UTF_8));
		}
		return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
	}



}
//{"accToken":"eyJyb2xlX3Rva2VuIjoiYWNjZXNzIiwiYWxnIjoiSFMyNTYifQ.eyJpZF91c2VyIjoxOCwidXNlcl9yb2xlIjoicmVndWxhciIsImV4cCI6MTYxODM2NjM1M30.M598dC-6Q4ptR6rYZNy0oZkM7lja98nQiXnSSpNPJGk",
//	 "accExpiration":1618366353,
//	 "refToken":"eyJyb2xlX3Rva2VuIjoicmVmcmVzaCIsImFsZyI6IkhTMjU2In0.eyJpZF91c2VyIjoxOCwiZXhwIjoxNjIxMDQxMTUzfQ.H2GQJbh0j_tygoRevH0LNFiD4-M_f00I6a3H-nANims",
//	 "refExpiration":1621041153}

//{"title":"MarkTitle2","text_content":"Mark Article2","tags":["tag1","tag2","tag3"]}
