package com.xtaolabs.gcauth_oauth.handler;

import emu.grasscutter.auth.*;
import emu.grasscutter.server.http.objects.ComboTokenResJson;
import emu.grasscutter.server.http.objects.LoginResultJson;

import me.exzork.gcauth.handler.GCAuthExternalAuthenticator;

public class GCAuthAuthenticationHandler implements AuthenticationSystem {
    private final Authenticator<LoginResultJson> gcAuthAuthenticator = new GCAuthenticators.GCAuthAuthenticator();
    private final Authenticator<LoginResultJson> tokenAuthenticator = new DefaultAuthenticators.TokenAuthenticator();
    private final Authenticator<ComboTokenResJson> sessionKeyAuthenticator = new DefaultAuthenticators.SessionKeyAuthenticator();
    private final GCAuthExternalAuthenticator externalAuthenticator = new GCAuthExternalAuthenticator();

    @Override
    public void createAccount(String username, String password) {
        // Unhandled.
    }

    @Override
    public void resetPassword(String username) {
        // Unhandled.
    }

    @Override
    public boolean verifyUser(String s) {
        return false;
    }

    @Override
    public Authenticator<LoginResultJson> getPasswordAuthenticator() {
        return gcAuthAuthenticator;
    }

    @Override
    public Authenticator<LoginResultJson> getTokenAuthenticator() {
        return tokenAuthenticator;
    }

    @Override
    public Authenticator<ComboTokenResJson> getSessionKeyAuthenticator() {
        return sessionKeyAuthenticator;
    }

    @Override
    public ExternalAuthenticator getExternalAuthenticator() {
        return externalAuthenticator;
    }
}
