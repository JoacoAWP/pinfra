package com.services;

import java.util.Date;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

//Este es un Service
@Stateless
@LocalBean
public class JwtService {
	private static final String jwtKey = "This is an ultra secret key";

	public String createJwt(String username) {
		Long currentTime = System.currentTimeMillis();
		String jwt = Jwts.builder()
				.signWith(SignatureAlgorithm.HS256, jwtKey)
				.setSubject(username)
				.setIssuedAt(new Date(currentTime))
				.setExpiration(new Date(currentTime + 900000))
				.compact();

		return jwt;
	}

	public boolean isValidJwt(String jwt) {
        try {
            Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(jwtKey))
                .parseClaimsJws(jwt);
            return true;
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException e) {
            // the JWT was not correctly constructed and should be rejected
            return false;
        } catch (Exception e) {
            // an unexpected error has occurred
            return false;
        }
    }
}