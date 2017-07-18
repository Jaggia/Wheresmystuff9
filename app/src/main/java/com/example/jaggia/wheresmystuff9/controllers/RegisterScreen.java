package com.example.jaggia.wheresmystuff9.controllers;
import com.example.jaggia.wheresmystuff9.R;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.jaggia.wheresmystuff9.model.Model;
import com.example.jaggia.wheresmystuff9.model.error_coding.*;
import com.example.jaggia.wheresmystuff9.model.user_system.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * This is controller for Register Screen
 * @author AnT. & Annette
 * @version 1.0
 */

public class RegisterScreen extends AppCompatActivity {
    private final String TAG = "RegisterScreen";
    private final String ERRORTAG = "Registration failed becase: ";
    private final String NONAME = "Registration failed because a name was not entered";
    private final String INVALIDUSERNAME = "Registration failed because the username is not valid";
    private final String INVALIDEMAIL = "Registration failed because the email is not valid";
    private final String DUPLICATEUSERNAME = "Registration failed because the username has been taken";
    private final String DUPLICATEEMAIL = "Registration failed because the email is already tied to another account";
    private final String ILLEGALPASSWORD = "Registration failed because the password is illegal. Must be at least 7 characters";
    private final String PASSWORDMISMATCH = "Registration failed because the passwords do not match";

    private FirebaseAuth myAuth;
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseReference = firebaseDatabase.getReference();
    private EditText registerEmail;
    private EditText registerName;
    private EditText registerUsername;
    private EditText registerPW;
    private EditText registerPW2;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerEmail = (EditText) findViewById(R.id.emailRegistering);
        registerName =
                (EditText) findViewById(R.id.nameRegistering);
        registerUsername =
                (EditText) findViewById(R.id.userNameRegistering);
        registerPW =
                (EditText) findViewById(R.id.passwordRegister);
        registerPW2 =
                (EditText) findViewById(R.id.password);
        Spinner registerUserType = (Spinner) findViewById(R.id.typeSpinner);

        register = (Button) findViewById(R.id.ButtonRegisterReg);
        Button cancelRegister = (Button) findViewById(R.id.ButtonCancelReg);

        String userTypes[] = {"User", "Admin"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, userTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        registerUserType.setAdapter(adapter);
        databaseReference.child("app").child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> usersChildren = dataSnapshot.getChildren();
                for (DataSnapshot  child: usersChildren){
                    User value = child.getValue(User.class);
                    Model.registerNewUser(value);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, databaseError.getMessage());
            }
        });
        cancelRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancel2Intent =
                        new Intent(RegisterScreen.this, WelcomeScreen.class);
                RegisterScreen.this.startActivity(cancel2Intent);
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
//        FirebaseUser currentUser = myAuth.getCurrentUser();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = registerName.getText().toString();
                final String email = registerEmail.getText().toString();
                final String username = registerUsername.getText().toString();
                final String pw = registerPW.getText().toString();
                final String pw2 = registerPW2.getText().toString();
                final boolean userType = false;
                try {
                    if (Model.validateLegalRegistration(name, username, email, pw, pw2)) {
                        Log.v(TAG, "It is attempting to make a new user");
                        myAuth.createUserWithEmailAndPassword(email, pw)
                                .addOnCompleteListener(RegisterScreen.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            final boolean registered = Model.registerNewUser(Model.createNewUser(name, username, pw, userType, email));
                                            if (registered) {
                                                firebaseDatabase.getReference().child("app").child("Users").push().setValue(Model.createNewUser(name, username, pw, userType, email));
                                                AlertDialog.Builder builder =
                                                        new AlertDialog.Builder(RegisterScreen.this);
                                                builder.setMessage("Registration Successful")
                                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                Intent registerIntent =
                                                                        new Intent(RegisterScreen.this, LoginScreen.class);
                                                                RegisterScreen.this.startActivity(registerIntent);
                                                            }
                                                        })
                                                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                            @Override
                                                            public void onDismiss(DialogInterface dialog) {
                                                                Intent registerIntent =
                                                                        new Intent(RegisterScreen.this, LoginScreen.class);
                                                                RegisterScreen.this.startActivity(registerIntent);
                                                            }
                                                        }).create().show();
                                            }
                                        } else {
                                            Exception e = task.getException();
                                            Log.w("LoginActivity", "Failed Registration", e);
                                            firebaseDatabase.getReference().child("app").child("Errors").push().setValue(e != null ? e.getMessage() : null);
                                            AlertDialog.Builder builder =
                                                    new AlertDialog.Builder(RegisterScreen.this);
                                            builder.setMessage(e != null ? e.getMessage() : null)
                                                    .setNegativeButton("Retry", null)
                                                    .create().show();
                                        }
                                    }
                                });
                    }
                } catch (NoNameException | InvalidUsernameException | DuplicateEmailException | DuplicateUsernameException | InvalidEmailException | InvalidPasswordException | PasswordMismatchException e) {
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(RegisterScreen.this);
                    builder.setMessage(ERRORTAG + e.getMessage())
                            .setNegativeButton("Retry", null)
                            .create().show();
                }
            }
        });
    }
}
