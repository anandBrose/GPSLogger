package com.anand.brose.gpslogger;

/**
 * Created by Anand on 22-06-2017.
 */

public class LoginPresenterImpl implements ILoginPresenter, UserModel.AuthListener {

    ILoginView loginView;
    UserModel userModel;
    public LoginPresenterImpl(ILoginView loginView, UserDatabaseReader databaseReader) {
        this.loginView = loginView;
        userModel = new UserModel(databaseReader);
    }
    @Override
    public void onAuthSuccess(String username) {
        loginView.authenticationSuccess(username);
    }

    @Override
    public void onAuthFailure() {
        loginView.authenticationFailure();
    }

    @Override
    public void login(String username, String password) {
        if(username.isEmpty()){
            loginView.showUserNameError();
            return;
        }
        if(username.isEmpty()){
            loginView.showPasswordError();
            return;
        }
        userModel.authenticate(username, password, this);
    }
}
