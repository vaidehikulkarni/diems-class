package org.diems.diemsapp;

class UserData {

    private String id, name, accessToken, loginType;

    String getLoginType() {
        return loginType;
    }

    UserData(String id, String name, String accessToken, String loginType) {
        this.id = id;
        this.name = name;
        this.accessToken = accessToken;
        this.loginType = loginType;

        MainActivity.name_user.setText(name);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    String getAccessToken() {
        return accessToken;
    }

    void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
