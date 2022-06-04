package com.xtaolabs.gcauth_oauth.handler;

import com.xtaolabs.gcauth_oauth.GCAuth_OAuth;
import emu.grasscutter.Grasscutter;
import emu.grasscutter.auth.AuthenticationSystem;
import emu.grasscutter.auth.Authenticator;
import emu.grasscutter.auth.OAuthAuthenticator;
import emu.grasscutter.database.DatabaseHelper;
import emu.grasscutter.game.Account;
import emu.grasscutter.server.http.objects.LoginResultJson;

import me.exzork.gcauth.GCAuth;
import me.exzork.gcauth.utils.Authentication;

import static emu.grasscutter.Configuration.ACCOUNT;
import static emu.grasscutter.utils.Language.translate;

public final class GCAuthenticators {

    public static class GCAuthAuthenticator implements Authenticator<LoginResultJson> {
        @Override
        public LoginResultJson authenticate(AuthenticationSystem.AuthenticationRequest authenticationRequest) {
            var response = new LoginResultJson();

            var requestData = authenticationRequest.getPasswordRequest();
            assert requestData != null; // This should never be null.

            Account account = Authentication.getAccountByOTP(requestData.account);
            if(account == null) {
                response.retcode = -201;
                response.message = "OTP invalid";
                return response;
            }

            // Account was found, log the player in
            response.message = "OK";
            response.data.account.uid = account.getId();
            response.data.account.token = account.generateSessionKey();
            response.data.account.email = account.getEmail();
            response.data.account.twitter_name = account.getUsername();

            GCAuth.getInstance().getLogger().info("[GCAuth] Client " + requestData.account + " logged in");
            return response;
        }
    }

    /**
     * Handles the authentication request from the game when using a registry token.
     */
    public static class TokenAuthenticator implements Authenticator<LoginResultJson> {
        @Override
        public LoginResultJson authenticate(AuthenticationSystem.AuthenticationRequest request) {
            var response = new LoginResultJson();

            var requestData = request.getTokenRequest();
            assert requestData != null;

            boolean successfulLogin;
            String address = request.getRequest().ip();
            String loggerMessage;
            int playerCount = Grasscutter.getGameServer().getPlayers().size();

            // Log the attempt.
            GCAuth_OAuth.getInstance().getLogger().info(translate("messages.dispatch.account.login_token_attempt", address));

            if (ACCOUNT.maxPlayer <= -1 || playerCount < ACCOUNT.maxPlayer) {

                // Get account from database.
                Account account = DatabaseHelper.getAccountById(requestData.uid);

                // Check if account exists/token is valid.
                successfulLogin = account != null && account.getSessionKey().equals(requestData.token);

                // Set response data.
                if (successfulLogin) {
                    response.message = "OK";
                    response.data.account.uid = account.getId();
                    response.data.account.token = account.getSessionKey();
                    response.data.account.email = account.getEmail();
                    response.data.account.twitter_name = account.getUsername();

                    // Log the login.
                    loggerMessage = translate("messages.dispatch.account.login_token_success", address, requestData.uid);
                } else {
                    response.retcode = -201;
                    response.message = translate("messages.dispatch.account.account_cache_error");

                    // Log the failure.
                    loggerMessage = translate("messages.dispatch.account.login_token_error", address);
                }

            } else {
                response.retcode = -201;
                response.message = translate("messages.dispatch.account.server_max_player_limit");

                loggerMessage = translate("messages.dispatch.account.login_max_player_limit", address);
            }

            GCAuth_OAuth.getInstance().getLogger().info(loggerMessage);
            return response;
        }
    }

    /**
     * Handles authentication requests from OAuth sources.
     */
    public static class OAuthAuthentication implements OAuthAuthenticator {
        @Override
        public void handleLogin(AuthenticationSystem.AuthenticationRequest request) {
            assert request.getResponse() != null;
            VerifyHandler.handle(request.getRequest(), request.getResponse());
        }
        /**
         * The type of the client.
         * Used for handling redirection.
         */
        @Override
        public void handleRedirection(AuthenticationSystem.AuthenticationRequest request, ClientType type) {
            assert request.getResponse() != null;
            switch (type) {
                case DESKTOP -> DesktopRedirectHandler.handle(request.getRequest(), request.getResponse());
                case MOBILE -> MobileRedirectHandler.handle(request.getRequest(), request.getResponse());
            }
        }
        @Override
        public void handleTokenProcess(AuthenticationSystem.AuthenticationRequest request) {
            assert request.getResponse() != null;
            request.getResponse().send("Authentication is not available with the default authentication method.");
        }
    }
}
