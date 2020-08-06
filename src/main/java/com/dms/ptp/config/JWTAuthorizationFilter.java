package com.dms.ptp.config;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.dms.ptp.entity.User;
import com.dms.ptp.util.JWTTokenUtil;

import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	public static final String TOKEN_NOT_FOUND = "JWT Token not found";
	
	@Autowired
    private final JWTTokenUtil jwtUtil;
	
	
    private static final String JWT_HEADER_NAME = "Authorization";
    
    static Logger logger = LoggerFactory.getLogger(JWTAuthorizationFilter.class);

    public JWTAuthorizationFilter(AuthenticationManager authManager, JWTTokenUtil jwtUtil) {
        super(authManager);
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        String token = req.getHeader(JWT_HEADER_NAME);

        /*if (token == null || !token.startsWith("Bearer ")) {
        	log.error(Constants.JWT_EXCEPTION);
            throw new JwtTokenException(Constants.JWT_EXCEPTION);
        } else {*/

        if (token != null) {
            try {
                User user = jwtUtil.getUser(token);

                GrantedAuthority roleAuthority = new SimpleGrantedAuthority(String.valueOf(user.getRole()));
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        user.getId(), null, Arrays.asList(roleAuthority));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (SignatureException ex) {
                logger.error(ex.getMessage());
            }
        }
        //}
        chain.doFilter(req, res);
    }
}