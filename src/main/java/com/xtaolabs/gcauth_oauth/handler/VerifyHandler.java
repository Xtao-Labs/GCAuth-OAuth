package com.xtaolabs.gcauth_oauth.handler;

import java.io.IOException;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.xtaolabs.gcauth_oauth.json.VerifyJson;
import com.xtaolabs.gcauth_oauth.utils.parse;
import emu.grasscutter.Grasscutter;
import emu.grasscutter.game.Account;
import emu.grasscutter.server.dispatch.json.LoginResultJson;
import express.http.HttpContextHandler;
import express.http.Request;
import express.http.Response;
import me.exzork.gcauth.utils.Authentication;


public final class VerifyHandler implements HttpContextHandler {
    @Override
    public void handle(Request req, Response res) throws IOException {
        VerifyJson request = req.body(VerifyJson.class);
        LoginResultJson responseData = new LoginResultJson();
        DecodedJWT jwt = parse.deToken(request.access_token);
        Account account = null;
        if (jwt != null) {
            account = Authentication.getAccountByOneTimeToken(jwt.getClaim("token").asString());
        }
        // Login
        if(account == null) {
            Grasscutter.getLogger().info("[GCAuth] Client " + req.ip() + " failed to log in");
            responseData.retcode = -201;
            responseData.message = "Token is invalid";
            res.send(responseData);
            return;
        }

        // Account was found, log the player in
        responseData.message = "OK";
        responseData.data.account.uid = account.getId();
        responseData.data.account.token = account.generateSessionKey();
        responseData.data.account.email = account.getEmail();
        responseData.data.account.twitter_name = account.getUsername();

        Grasscutter.getLogger().info(String.format("[GCAuth] Client %s logged in as %s", req.ip(), responseData.data.account.uid));

        res.send(responseData);
    }
}
