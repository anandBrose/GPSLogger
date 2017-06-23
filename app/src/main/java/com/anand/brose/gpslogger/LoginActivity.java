package com.anand.brose.gpslogger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.anand.brose.gpslogger.transport.TransportDetailsActivity;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ILoginView {

    EditText username;
    EditText password;
    ILoginPresenter loginPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        username = (EditText) findViewById(R.id.usernameInput);
        password = (EditText) findViewById(R.id.passwordInput);
        ((Button)findViewById(R.id.loginButton)).setOnClickListener(this);
        loginPresenter = new LoginPresenterImpl(this, new UserDatabaseReader(this));
    }

    @Override
    public void onClick(View v) {
        loginPresenter.login(username.getText().toString(),password.getText().toString());
    }

    @Override
    public void authenticationSuccess(String username) {
        Intent intent = new Intent(this, TransportDetailsActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    @Override
    public void authenticationFailure() {
        Toast.makeText(this,"Credentials Wrong",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUserNameError() {
        Toast.makeText(this,"Please enter username",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPasswordError() {
        Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();
    }

}
