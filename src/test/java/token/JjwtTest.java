package token;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class JjwtTest {

	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
		File keysFile = new File("JWTKeys");
		// GENERATE KEY AND PUT IN FILE JWTKeys
//		JsonFactory jfactory = new JsonFactory();
//		JsonGenerator jGenerator = jfactory.createGenerator(keysFile, JsonEncoding.UTF8);
//		
		Key key2 = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//		String encodedKey = Encoders.BASE64.encode(key.getEncoded());
//		jGenerator.writeStartObject();
//		jGenerator.writeStringField("key", encodedKey);
//		jGenerator.writeEndObject();
//		jGenerator.close();  	
//Key decodedKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(encodedKey));

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.readValue(keysFile, ObjectNode.class);
		Key decodedKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(node.get("key").textValue()));
		System.out.println(decodedKey.equals(key2));
		//test
		Map<String, Object> claims = new LinkedHashMap<>();
		claims.put("id_user", Long.valueOf(1234));
		claims.put("user_role", "regular");
		Date time = new Date(System.currentTimeMillis() + 3600000);
		String jws = Jwts.builder().
				setHeaderParam("role_token", "access").
				setClaims(claims).
				setExpiration(time).
				signWith(decodedKey).compact();
		System.out.println("Time: "+ time.getTime()/1000);
		System.out.println(jws);
		
		System.out.println("--decode--");
		//Jwts.parserBuilder().;
		try {
		Jwts.parserBuilder().setSigningKey(key2).build().parseClaimsJws(jws).getBody().get("id_user", Long.class);
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("CATCH!");
		} 
		//System.out.println(Jwts.parserBuilder().setSigningKey(decodedKey).build().parse(jws).toString());
//		System.out.println("isSigning: "+(boolean) Jwts.parserBuilder().setSigningKey(key2).build().isSigned(jws));
//		String jws2 = Jwts.builder().
//				setHeaderParam("role_token", (String) Jwts.parserBuilder().setSigningKey(decodedKey).build().parse(jws).getHeader().get("role_token")).
//				claim("id_user", Long.valueOf((Integer)Jwts.parserBuilder().setSigningKey(decodedKey).build().parseClaimsJws(jws).getBody().get("id_user"))).
//				claim("user_role", (String) Jwts.parserBuilder().setSigningKey(decodedKey).build().parseClaimsJws(jws).getBody().get("user_role")).
//				setExpiration(Jwts.parserBuilder().setSigningKey(decodedKey).build().parseClaimsJws(jws).getBody().getExpiration()).
//				signWith(decodedKey).compact();
//		System.out.println(jws2.equals(jws));
		
		
		//System.out.println("id_user: "+Long.valueOf((Integer) Jwts.parserBuilder().setSigningKey(decodedKey).build().parseClaimsJws(jws).getBody().get("id_user")));
		//System.out.println("role_token: "+(String) Jwts.parserBuilder().setSigningKey(decodedKey).build().parse(jws).getHeader().get("role_token"));
		//System.out.println("exp_token: "+Jwts.parserBuilder().setSigningKey(decodedKey).build().parseClaimsJws(jws).getBody().getExpiration().getTime()/1000);
	}
}
