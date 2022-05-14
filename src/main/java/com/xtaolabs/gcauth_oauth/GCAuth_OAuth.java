package com.xtaolabs.gcauth_oauth;

import emu.grasscutter.Grasscutter;
import emu.grasscutter.plugin.Plugin;
import static emu.grasscutter.Configuration.*;

import com.xtaolabs.gcauth_oauth.handler.JsonHandler;
import com.xtaolabs.gcauth_oauth.handler.VerifyHandler;
import com.xtaolabs.gcauth_oauth.handler.RequestHandler;

import express.Express;

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
        Express app = Grasscutter.getDispatchServer().getServer();
        app.disable("/Api/twitter_login");
        Grasscutter.getLogger().info("[GCAuth_OAuth] Disabled");
    }

    public void loadTwitterLogin() {
        String folder_name = PLUGINS_FOLDER + "/GCAuth/OAuth/";
        Express app = Grasscutter.getDispatchServer().getServer();

        app.get("/Api/twitter_login", new JsonHandler());

        app.post("/gcauth_oauth/login", new RequestHandler());

        app.post("/hk4e_global/mdk/shield/api/loginByThirdparty", new VerifyHandler());

        app.raw().config.addStaticFiles("/gcauth_oauth", folder_name, Location.EXTERNAL);
    }
}
