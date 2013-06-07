package ru.vlgu.homeautomation;

import android.app.Application;

public class Globals extends Application {

    private String serverAddress = "127.0.0.1";

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }
}