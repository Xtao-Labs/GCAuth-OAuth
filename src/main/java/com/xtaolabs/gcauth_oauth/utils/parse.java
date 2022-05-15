package com.xtaolabs.gcauth_oauth.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import static me.exzork.gcauth.utils.Authentication.getKey;


public class parse {
    public static DecodedJWT deToken(final String token) {
        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(getKey()).build();
            jwt = verifier.verify(token);
        } catch (JWTVerificationException exception) {
            // Invalid signature/claims
            return null;
        }
        return jwt;
    }
}
