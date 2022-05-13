package com.xtaolabs.gcauth_oauth.handler;

import java.io.IOException;

import emu.grasscutter.game.Account;
import express.http.HttpContextHandler;
import express.http.Request;
import express.http.Response;

import me.exzork.gcauth.utils.Authentication;


public final class RequestHandler implements HttpContextHandler {
    @Override
    public void handle(Request req, Response res) throws IOException {
        String username = req.formData("username");
        String password = req.formData("password");

        if (username.isEmpty() || password.isEmpty()) {
            res.send("Error: Username or password is empty");
            return;
        }
        Account account = Authentication.getAccountByUsernameAndPassword(username, password);
        if (account == null) {
            res.send("Error: Username or password is incorrect");
            return;
        }
        if (account.getPassword() != null && !account.getPassword().isEmpty()) {
            res.send(String.format("<meta http-equiv=\"refresh\" content=\"0;url=uniwebview://sdkThirdLogin?accessToken=%s\">",
                    Authentication.generateJwt(account)));
        } else {
            res.send("Error: Account is not registered");
        }
    }
}
