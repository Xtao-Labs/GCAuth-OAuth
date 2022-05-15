package com.xtaolabs.gcauth_oauth;

import com.xtaolabs.gcauth_oauth.handler.*;

import emu.grasscutter.Grasscutter;
import emu.grasscutter.auth.DefaultAuthentication;
import emu.grasscutter.plugin.Plugin;
import emu.grasscutter.server.http.HttpServer;
import static emu.grasscutter.Configuration.*;

import io.javalin.http.staticfiles.Location;

import java.io.File;


public class GCAuth_OAuth extends Plugin {
    @Override
    public void onEnable() {
        String Login_Html_Path = PLUGINS_FOLDER + "/GCAuth/OAuth/login.html";
        File Login_Html = new File(Login_Html_Path);
        if(!Login_Html.exists()) {
            Grasscutter.getLogger().warn(String.format("[GCAuth_OAuth] %s not found", Login_Html_Path));
        } else {
            loadTwitterLogin();
            Grasscutter.getLogger().info("[GCAuth_OAuth] Enabled");
        }
    }

    @Override
    public void onDisable() {
        Grasscutter.setAuthenticationSystem(new DefaultAuthentication());
        Grasscutter.getLogger().info("[GCAuth_OAuth] Disabled");
    }

    public void loadTwitterLogin() {
        String folder_name = PLUGINS_FOLDER + "/GCAuth/OAuth/";
        Grasscutter.setAuthenticationSystem(new GCAuthAuthenticationHandler());

        HttpServer app = Grasscutter.getHttpServer();

        app.addRouter(JsonHandler.class);
        app.addRouter(RequestHandler.class);
        app.addRouter(sdkHandler.class);
        app.addRouter(VerifyHandler.class);

        app.getHandle().config.addStaticFiles("/gcauth_oauth", folder_name, Location.EXTERNAL);
    }
}
