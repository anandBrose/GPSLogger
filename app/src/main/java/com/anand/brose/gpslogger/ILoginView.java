package com.anand.brose.gpslogger;

/**
 * Created by Anand on 22-06-2017.
 */

public interface ILoginView {
    void authenticationSuccess(String username);
    void authenticationFailure();
    void showUserNameError();
    void showPasswordError();
}
