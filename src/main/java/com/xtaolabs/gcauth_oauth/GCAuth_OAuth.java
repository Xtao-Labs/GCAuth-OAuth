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
    public static GCAuth_OAuth getInstance() {
        return (GCAuth_OAuth) Grasscutter.getPluginManager().getPlugin("GCAuth_OAuth");
    }

    @Override
    public void onEnable() {
        String Login_Html_Path = PLUGIN("GCAuth/OAuth/login.html");
        File Login_Html = new File(Login_Html_Path);
        if(!Login_Html.exists()) {
            getLogger().warn(String.format("%s not found", Login_Html_Path));
        } else {
            loadTwitterLogin();
            getLogger().info("GCAuth_OAuth Enabled");
        }
    }

    @Override
    public void onDisable() {
        Grasscutter.setAuthenticationSystem(new DefaultAuthentication());
        getLogger().info("GCAuth_OAuth Disabled");
    }

    public void loadTwitterLogin() {
        String folder_name = PLUGIN("GCAuth/OAuth/");
        Grasscutter.setAuthenticationSystem(new GCAuthAuthenticationHandler());

        HttpServer app = Grasscutter.getHttpServer();

        app.addRouter(RequestHandler.class);

        app.getHandle().config.addStaticFiles("/gcauth_oauth", folder_name, Location.EXTERNAL);
    }
}
