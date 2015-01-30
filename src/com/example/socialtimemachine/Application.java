package com.example.socialtimemachine;

import com.parse.Parse;
import com.parse.PushService;

public class Application extends android.app.Application {

    public Application(){
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the Parse SDK.
        Parse.initialize(this, "CblPQNXB5bztS0zjzox1vPPb8mRCiOorvNQMD3Jb", "fcqLiSWLa2JVHMW0esKZP3ewkAJm0jYPEjhlYVmg");

        // Specify an Activity to handle all pushes by default.
        PushService.setDefaultPushCallback(this, MainActivity.class);
    }
}
