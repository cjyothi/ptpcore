package com.dms.ptp.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dms.ptp.entity.User;
import com.dms.ptp.controller.AffinityReachCalculator;
import com.dms.ptp.dto.JWTExtract;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JWTTokenUtil {
	@Value("${jwt.expiration-hours}")
	private int expirationHours;

	
	  @Value("${jwt.signature-key}") 
	  private String signatureKey;
	  
	  private byte[] signatureKeyBytes;
	  
	  @PostConstruct public void init() { 
		  this.signatureKeyBytes = signatureKey.getBytes();
	  
	 }
	  
	  static Logger logger = LoggerFactory.getLogger(JWTTokenUtil.class);

	
	public String getToken(User user) {

		return Jwts.builder().setSubject(String.valueOf(user.getId()))
		  .signWith(SignatureAlgorithm.HS512, signatureKeyBytes) 
		  .claim("id", user.getId())
		  .claim("role", user.getJobRole())
		  .setIssuedAt(new Date(System.currentTimeMillis()))
		  .setExpiration(getExpirationTime())
		  .compact();
		 
	}
	
	/*
	 * private String doGenerateToken(Map<String, Object> claims, String subject) {
	 * 
	 * return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new
	 * Date(System.currentTimeMillis())) .setExpiration(new
	 * Date(System.currentTimeMillis() + expirationHours*3600))
	 * .signWith(SignatureAlgorithm.HS512, secret).compact(); }
	 */

	public User getUser(String token) throws SignatureException {
		token = token.replace("Bearer ", "");
		Claims claims = Jwts.parser()
				.setSigningKey(signatureKeyBytes)
				.parseClaimsJws(token)
				.getBody();

		User user = new User();
		user.setId(Integer.valueOf(claims.getSubject()));
	//	user.setEmail(claims.get("email").toString());
		user.setJobRole((String) claims.get("role"));

		return user;
	}

	private Date getExpirationTime() {
		Long expireInMillis = TimeUnit.HOURS.toMillis(expirationHours);
		return new Date(expireInMillis + new Date().getTime());
	}
	
	public JWTExtract getIdRoleFromToken(String token) throws JSONException {
        JWTExtract response = new JWTExtract();
        
        String[] split_string = token.split("\\.");
        String base64EncodedHeader = split_string[0];
        String base64EncodedBody = split_string[1];
        String base64EncodedSignature = split_string[2];

        Base64 base64Url = new Base64(true);
        logger.info("~~~~~~~~~ JWT Body ~~~~~~~");
        String body = new String(base64Url.decode(base64EncodedBody));
        logger.info("JWT Body : "+body);
        
        JSONObject obj = new JSONObject(body);
        int userId = (int) obj.get("id");
        String userRole = (String) obj.get("role");
        logger.info("userId: "+userId + " userRole: "+userRole );
        
        response.setUserId(userId);
        response.setUserRole(userRole);
        return response;
    }
}
