package com.xtaolabs.gcauth_oauth.handler;

import emu.grasscutter.Grasscutter;
import emu.grasscutter.auth.AuthenticationSystem;
import emu.grasscutter.auth.Authenticator;
import emu.grasscutter.auth.OAuthAuthenticator;
import emu.grasscutter.database.DatabaseHelper;
import emu.grasscutter.game.Account;
import emu.grasscutter.server.http.objects.LoginResultJson;
import static emu.grasscutter.utils.Language.translate;

import me.exzork.gcauth.utils.Authentication;

public class GCAuthenticators {

    public static class GCAuthAuthenticator implements Authenticator<LoginResultJson> {
        @Override
        public LoginResultJson authenticate(AuthenticationSystem.AuthenticationRequest authenticationRequest) {
            var response = new LoginResultJson();

            var requestData = authenticationRequest.getPasswordRequest();
            assert requestData != null; // This should never be null.

            Account account = Authentication.getAccountByOneTimeToken(requestData.account);
            if(account == null) {
                Grasscutter.getLogger().info("[GCAuth] Client " + requestData.account + " tried to login with invalid one time token.");
                response.retcode = -201;
                response.message = "Token is invalid";
                return response;
            }

            // Account was found, log the player in
            response.message = "OK";
            response.data.account.uid = account.getId();
            response.data.account.token = account.generateSessionKey();
            response.data.account.email = account.getEmail();
            response.data.account.twitter_name = account.getUsername();

            Grasscutter.getLogger().info("[GCAuth] Client " + requestData.account + " logged in");
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

            // Log the attempt.
            Grasscutter.getLogger().info(translate("messages.dispatch.account.login_token_attempt", address));

            // Get account from database.
            Account account = DatabaseHelper.getAccountById(requestData.uid);

            // Check if account exists/token is valid.
            successfulLogin = account != null && account.getSessionKey().equals(requestData.token);

            // Set response data.
            if(successfulLogin) {
                response.message = "OK";
                response.data.account.uid = account.getId();
                response.data.account.token = account.getSessionKey();
                response.data.account.email = account.getEmail();
                response.data.account.twitter_name = account.getUsername();

                // Log the login.
                Grasscutter.getLogger().info(translate("messages.dispatch.account.login_token_success", address, requestData.uid));
            } else {
                response.retcode = -201;
                response.message = translate("messages.dispatch.account.account_cache_error");

                // Log the failure.
                Grasscutter.getLogger().info(translate("messages.dispatch.account.login_token_error", address));
            }

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

        @Override public void handleDesktopRedirection(AuthenticationSystem.AuthenticationRequest request) {
            assert request.getResponse() != null;
            JsonHandler.handle(request.getRequest(), request.getResponse());
        }

        @Override public void handleMobileRedirection(AuthenticationSystem.AuthenticationRequest request) {
            assert request.getResponse() != null;
            sdkHandler.handle(request.getRequest(), request.getResponse());
        }

        @Override public void handleTokenProcess(AuthenticationSystem.AuthenticationRequest request) {
            assert request.getResponse() != null;
            request.getResponse().send("Authentication is not available with the default authentication method.");
        }
    }
}
