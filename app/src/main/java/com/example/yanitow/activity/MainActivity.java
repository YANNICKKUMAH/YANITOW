package com.example.yanitow.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.yanitow.R;
import com.example.yanitow.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {


    Button sign_in, register;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;
    RelativeLayout root_layout;






    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                                        .setDefaultFontPath("fonts/OpenSans-BoldItalic.ttf")
                                        .setFontAttrId(R.attr.fontPath)
                                        .build());
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");


        sign_in = (Button) findViewById(R.id.sign_in);
        register = (Button) findViewById(R.id.register);
        root_layout = (RelativeLayout) findViewById(R.id.root_layout);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterDialogue();
            }
        });

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginDialog();
            }
        });



    }




    private void showLoginDialog() {

        AlertDialog.Builder dialogue = new AlertDialog.Builder(this);
        dialogue.setTitle("SIGN IN");
        dialogue.setMessage("Please use Email to sign in");
        LayoutInflater inflater = LayoutInflater.from(this);
        View login_layout = inflater.inflate(R.layout.login_layout, null);

        final MaterialEditText editEmail = login_layout.findViewById(R.id.editEmail);
        final MaterialEditText editpassword = login_layout.findViewById(R.id.editpassword);

        dialogue.setView(login_layout);

        dialogue.setPositiveButton("SIGN IN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                        final ProgressDialog waiting = ProgressDialog.show(MainActivity.this, "",
                                "Loading. Please wait...", true);
                        waiting.show();

                        if (TextUtils.isEmpty(Objects.requireNonNull(editEmail.getText()).toString())) {

                            Snackbar.make(root_layout, "Please enter your email address", Snackbar.LENGTH_SHORT)
                                    .show();
                            return;

                        }

                        if (TextUtils.isEmpty(editpassword.getText().toString())) {

                            Snackbar.make(root_layout, "Please enter your password", Snackbar.LENGTH_SHORT)
                                    .show();
                            return;

                        }


                        if (editpassword.getText().toString().length() < 8){

                            Snackbar.make(root_layout, "Your password is too short!!", Snackbar.LENGTH_SHORT)
                                    .show();
                            return;

                        }


                        auth.signInWithEmailAndPassword(editEmail.getText().toString(),editpassword.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        startActivity(new Intent(MainActivity.this,Welcome.class));
                                        finish();


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                waiting.dismiss();
                                Snackbar.make(root_layout, "Login Failed" + e.getMessage(), Snackbar.LENGTH_SHORT)
                                        .show();
                                sign_in.setEnabled(true);

                            }
                        });

                        sign_in.setEnabled(false);


                    }




        });

        dialogue.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });




        dialogue.show();
    }

    private void showRegisterDialogue() {
        AlertDialog.Builder dialogue = new AlertDialog.Builder(this);
        dialogue.setTitle("REGISTER");
        dialogue.setMessage("Please use Email to sign up");
        LayoutInflater inflater = LayoutInflater.from(this);
        View layout_register = inflater.inflate(R.layout.layout_register, null);

        final MaterialEditText editEmail = layout_register.findViewById(R.id.editEmail);
        final MaterialEditText editpassword = layout_register.findViewById(R.id.editpassword);
        final MaterialEditText phone = layout_register.findViewById(R.id.editphone);
        final MaterialEditText name = layout_register.findViewById(R.id.editname);

        dialogue.setView(layout_register);

        dialogue.setPositiveButton("REGISTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();

                final ProgressDialog waiting = ProgressDialog.show(MainActivity.this, "",
                        "Loading. Please wait...", true);
                waiting.show();


                if (TextUtils.isEmpty(editEmail.getText().toString())){

                    Snackbar.make(root_layout, "Please enter your email address", Snackbar.LENGTH_SHORT)
                            .show();
                    return;

                }

                if (TextUtils.isEmpty(editpassword.getText().toString())){

                    Snackbar.make(root_layout, "Please enter your password", Snackbar.LENGTH_SHORT)
                            .show();
                    return;

                }

                if (editpassword.getText().toString().length() < 8){

                    Snackbar.make(root_layout, "Your password is too short!!", Snackbar.LENGTH_SHORT)
                            .show();
                    return;

                }

                if (editpassword.getText().toString().length() > 24){

                    Snackbar.make(root_layout, "Password shoud be between 8 and 24", Snackbar.LENGTH_SHORT)
                            .show();
                    return;

                }


                if (TextUtils.isEmpty(Objects.requireNonNull(name.getText()).toString())){

                    Snackbar.make(root_layout, "Please enter your name", Snackbar.LENGTH_SHORT)
                            .show();
                    return;

                }

                if (TextUtils.isEmpty(Objects.requireNonNull(phone.getText()).toString())){

                    Snackbar.make(root_layout, "Please enter your phone number", Snackbar.LENGTH_SHORT)
                            .show();
                    return;

                }


                auth.createUserWithEmailAndPassword(editEmail.getText().toString(), editpassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                User user = new User();
                                user.setEmail(editEmail.getText().toString());
                                user.setPassword(editpassword.getText().toString());
                                user.setName(name.getText().toString());
                                user.setPhone(phone.getText().toString());

                                users.child(Objects.requireNonNull(authResult.getUser()).getUid())
                                        .setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                Snackbar.make(root_layout, "Registration Successful", Snackbar.LENGTH_SHORT)
                                                        .show();
                                                waiting.dismiss();


                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Snackbar.make(root_layout, "Registration Failed" + e.getMessage(), Snackbar.LENGTH_SHORT)
                                                        .show();
                                                waiting.dismiss();
                                            }
                                        });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(root_layout, "Registration Failed" + e.getMessage(), Snackbar.LENGTH_SHORT)
                                        .show();
                            }
                        });




            }
        });

        dialogue.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();


            }
        });


            dialogue.show();

    }
}
