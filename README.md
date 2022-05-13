# GCAuth OAuth

The in-game login system for [Grasscutter](https://github.com/Grasscutters/Grasscutter) is based on oauth and [GCAuth](https://github.com/exzork/GCAuth).

## Current Features:

- [x] Use twitter oauth to login
- [ ] Error page

## Important Notes:

This plugin is made to run on the current [Development](https://github.com/Grasscutters/Grasscutter/tree/development) branch of Grasscutter. \

This plugin is in very early development and only have the backend now. Frontend is extermely hardcore.

## Setup

### Download Plugin Jar

See releases.

### Compile yourself

1. Pull the latest code from github using ``git clone https://github.com/Xtao-Team/GCAuth-OAuth`` in your terminal of choice.
2. Locate your grasscutter server and copy the ``grasscutter`` server jar into the newly created ``GCAuth-OAuth/lib`` folder
3. Navigate back into the project root folder called ``GCAuth-OAuth`` folder and run ``gradlew build`` (cmd) **or** ``./gradlew build`` (Powershell, Linux & Mac).
4. Assuming the build succeeded, in your file explorer navigate to the ``GCAuth-OAuth`` folder, you should have a ``gcauth_oauth.jar`` file, copy it.
5. Navigate to your ``Grasscutter`` server, find the ``plugins`` folder and paste the ``gcauth_oauth.jar`` into it.
6. Start your server.

...Jump to next section...

### Usage

7. Put the all the [frontend](#Frontend) files into the folder `GRASSCUTTER_RESOURCE/plugins/GCAuth/OAuth`. Note that your must have `login.html` for now. You are free to put any other dynamiclly loaded file(e.g. `.js`, `.css`) in that folder. Check the last section for current available frontend.

8. Put the ``gcauth.jar`` file into the ``plugins`` folder of your grasscutter server. [GCAuth](https://github.com/exzork/GCAuth)

9. Log in to your server and you should be able to log in with Twitter account. (make sure `webapi-os.account.hoyoverse.com` is in your proxy list)

Your final plugins' folder's directory structure should look similar to this
```
plugins
│   gcauth.jar
│   gcauth_oauth.jar
│   ...
└───GCAuth
    │   config.json
    └───OAuth
        │   login.html
        │   ...
```

### Frontend

[en](https://github.com/Xtao-Team/GCAuth-OAuth/tree/en) [zh](https://github.com/Xtao-Team/GCAuth-OAuth/tree/zh)

- [x] Login page
- [ ] Error page
