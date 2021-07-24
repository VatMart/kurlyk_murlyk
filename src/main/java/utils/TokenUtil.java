package utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;

import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public final class TokenUtil {

	private static final File FILE_KEY = new File("JWTKeys");
	private static Key KEY;
	private static final long EXP_TIME_ACCESS_TOKEN = 3600000L; // 3600000 - 1 Hour in millisec
	private static final long EXP_TIME_REFRESH_TOKEN = 3600000L * 24L * 31L; // 3600000 * 24 * 31 - 1 Month
	private static final long EXTRA_TIME = 120000; // 2 minutes

	private TokenUtil() {

	}

	public synchronized static String tokenPairToJsonString(PairTokens pairTokens) {
		if (pairTokens != null)
			return "{\"accToken\":\"" + pairTokens.getAcToken().getToken() + "\",\r\n" + " \"accExpiration\":"
					+ pairTokens.getAcToken().getExpiration() + ",\r\n" + " \"refToken\":\""
					+ pairTokens.getRefToken().getToken() + "\",\r\n" + " \"refExpiration\":"
					+ pairTokens.getRefToken().getExpiration() + "}\r\n" + "";
		return "";
	}

	public synchronized static PairTokens generateTokenPair(User user)
			throws InvalidKeyException, IOException {
		AccessToken accessToken = generateAccessToken(user);
		RefreshToken refreshToken = generateRefreshToken(user);
		return new PairTokens(accessToken, refreshToken);
	}

	private synchronized static AccessToken generateAccessToken(User user)
			throws InvalidKeyException, IOException {
		Map<String, Object> claims = new LinkedHashMap<>();
		claims.put("id_user", user.getUser_ID());
		claims.put("user_role", user.getUser_role());
		Date dateExp = new Date(System.currentTimeMillis() + EXP_TIME_ACCESS_TOKEN);
		String jws = Jwts.builder().setHeaderParam("role_token", "access").setClaims(claims).setExpiration(dateExp)
				.signWith(getSecretKey()).compact();
		AccessToken accessToken = new AccessToken(jws, dateExp.getTime() / 1000L, user.getUser_ID());
		accessToken.setUser_role(user.getUser_role());
		return accessToken;
	}

	private synchronized static RefreshToken generateRefreshToken(User user)
			throws InvalidKeyException, IOException {
		Map<String, Object> claims = new LinkedHashMap<>();
		claims.put("id_user", user.getUser_ID());
		Date dateExp = new Date(System.currentTimeMillis() + EXP_TIME_REFRESH_TOKEN);
		String jws = Jwts.builder().setHeaderParam("role_token", "refresh").setClaims(claims).setExpiration(dateExp)
				.signWith(getSecretKey()).compact();
		return new RefreshToken(jws, dateExp.getTime() / 1000L, user.getUser_ID());
	}

	public synchronized static AccessToken decodeAccessToken(String token) {
		if (token.contains("Bearer")) {
			token = token.replace("Bearer", "");
		}
		token = token.replace(" ", "");
		Key key;
		try {
			key = getSecretKey();
		} catch (IOException e) {
			System.out.println("�� ���������� �������� ���� �������");
			return null;
		}
		boolean isSigned = Jwts.parserBuilder().setSigningKey(key).build().isSigned(token);
		if (!isSigned)
			return null;
		String role_token;
		long id;
		long exp;
		String role_user;
		try {
			role_token = (String) Jwts.parserBuilder().setSigningKey(key).build().parse(token).getHeader()
					.get("role_token");
		
		if (!role_token.equals("access"))
			return null;
		id = Long.valueOf((Integer) Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody()
				.get("id_user"));
		exp = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration()
				.getTime() / 1000;
		role_user = (String) Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody()
				.get("user_role");
		} catch (Exception e) {
			System.out.println("������ � acces ������");
			return null;
		}
		AccessToken accToken = new AccessToken(token, exp, id);
		accToken.setUser_role(role_user);
		return accToken;
	}

	public synchronized static RefreshToken decodeRefreshToken(String token) {
		if (token.contains("Bearer")) {
			token = token.replace("Bearer", "");
		}
		token = token.replace(" ", "");
		Key key;
		try {
			key = getSecretKey();
		} catch (IOException e) {
			System.out.println("�� ���������� �������� ���� �������");
			return null;
		}
		boolean isSigned = Jwts.parserBuilder().setSigningKey(key).build().isSigned(token);
		if (!isSigned)
			return null;
		String role_token;
		long id;
		long exp;
		try {
			role_token = (String) Jwts.parserBuilder().setSigningKey(key).build().parse(token).getHeader()
					.get("role_token");
		
		if (!role_token.equals("refresh"))
			return null;
		id = Long.valueOf((Integer) Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody()
				.get("id_user"));
		exp = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration()
				.getTime() / 1000;
		} catch (Exception e) {
			System.out.println("������ � refresh ������");
			return null;
		}
		return new RefreshToken(token, exp, id);
	}

	/**
	 * return key from file
	 * 
	 * @return key
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	private synchronized static Key getSecretKey() throws JsonParseException, JsonMappingException, IOException {
		if (KEY != null)
			return KEY;
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.readValue(FILE_KEY, ObjectNode.class);
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(node.get("key").textValue()));
	}

	@JsonAutoDetect
	public static class PairTokens {
		private AccessToken acToken;
		private RefreshToken refToken;

		private PairTokens(AccessToken acToken, RefreshToken refToken) {
			this.acToken = acToken;
			this.refToken = refToken;
		}

		public PairTokens() {
		}

		public AccessToken getAcToken() {
			return acToken;
		}

		public RefreshToken getRefToken() {
			return refToken;
		}

	}

	@JsonAutoDetect
	public static class AccessToken {
		private long user_ID;
		private String token;
		private long expiration;
		private String user_role;

		private AccessToken(String token, long expiration, long user_id) {
			this.token = token;
			this.expiration = expiration;
			this.user_ID = user_id;
		}

		public AccessToken() {
		}

		public String getToken() {
			return token;
		}

		/**
		 * 
		 * @return long expiration UNIX TIME in seconds
		 */
		public long getExpiration() {
			return expiration;
		}

		public long getUser_ID() {
			return user_ID;
		}

		public String getUser_role() {
			return user_role;
		}

		private void setUser_role(String user_role) {
			this.user_role = user_role;
		}
	}

	@JsonAutoDetect
	public static class RefreshToken {
		private long user_ID;
		private String token;
		private long expiration;

		private RefreshToken(String token, long expiration, long user_id) {
			this.token = token;
			this.expiration = expiration;
			this.user_ID = user_id;
		}

		public RefreshToken() {
		}

		public String getToken() {
			return token;
		}

		/**
		 * 
		 * @return long expiration UNIX TIME in seconds
		 */
		public long getExpiration() {
			return expiration;
		}

		public long getUser_ID() {
			return user_ID;
		}
	}

}
