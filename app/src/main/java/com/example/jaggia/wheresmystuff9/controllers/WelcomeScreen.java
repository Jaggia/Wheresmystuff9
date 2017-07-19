package com.example.jaggia.wheresmystuff9.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import com.example.jaggia.wheresmystuff9.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * This is controller for Welcome Screen
 * @author AnT. & Annette
 * @version 1.0
 */
public class WelcomeScreen extends AppCompatActivity {
    private CallbackManager callbackManager;
    private FirebaseAuth myAuth;
    // --Commented out by Inspection (7/16/17, 7:37 PM):private final String TAG = "Welcome Screen";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        myAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

//        final TextView title = (TextView) findViewById(R.id.welcome);
        LoginButton facebookbutton = (LoginButton) findViewById(R.id.fblogin_button);
        final Button registerbtn = (Button) findViewById(R.id.register);
        final Button loginbtn = (Button) findViewById(R.id.login);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent =
                        new Intent(WelcomeScreen.this, LoginScreen.class);
                WelcomeScreen.this.startActivity(loginIntent);
            }
        });

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent =
                        new Intent(WelcomeScreen.this, RegisterScreen.class);
                WelcomeScreen.this.startActivity(registerIntent);
            }
        });

        facebookbutton.setReadPermissions("email");
        facebookbutton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
                Intent loginIntent =
                        new Intent(WelcomeScreen.this, MainUserScreen.class);
                WelcomeScreen.this.startActivity(loginIntent);
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(WelcomeScreen.this);
                builder.setMessage(error.getMessage())
                        .setNegativeButton("Retry", null)
                        .create().show();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        myAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = myAuth.getCurrentUser();
                        }
                    }
                });
    }
}

