package com.example.eventhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class CoordLoginActivity extends AppCompatActivity {
    Button submitButton;
    EditText emailTxt,passwdTxt;
    FirebaseAuth auth;
    boolean passwordVisible;

    ArrayList<String> coordinatorList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coord_login);
        ConstraintLayout constraintLayout = findViewById(R.id.coordLoginxmlID);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5500);
        animationDrawable.start();
        coordinatorList= new ArrayList<>();
        coordinatorList.add("byld@iiitd.club");
        coordinatorList.add("acm@iiitd.club");
        coordinatorList.add("designhub@iiitd.club");
        coordinatorList.add("evariste@iiitd.club");
        coordinatorList.add("biobytes@iiitd.club");
        coordinatorList.add("cyborg@iiitd.club");
        coordinatorList.add("owasp@iiitd.club");
        coordinatorList.add("darkcode@iiitd.club");
        coordinatorList.add("foobar@iiitd.club");
        coordinatorList.add("ieee@iiitd.club");
        coordinatorList.add("leanin@iiitd.club");
        coordinatorList.add("electroholics@iiitd.club");
        coordinatorList.add("muse@iiitd.club");
        coordinatorList.add("finnexia@iiitd.club");
        coordinatorList.add("girlup@iiitd.club");
        coordinatorList.add("madtoes@iiitd.club");
        coordinatorList.add("tasveer@iiitd.club");
        coordinatorList.add("tasveer@iiitd.club");
        submitButton = findViewById(R.id.submitButton);
        auth = FirebaseAuth.getInstance();
        emailTxt = findViewById(R.id.emailText);
        passwdTxt = findViewById(R.id.passwordText);
        submitButton.setOnClickListener(view -> {
            String email = emailTxt.getText().toString().trim(), passwd = passwdTxt.getText().toString();
            if (email.isEmpty()) {
                emailTxt.setError("E-mail field can't be EMPTY");
                emailTxt.requestFocus();
            } else if (passwd.length()<6) {
                passwdTxt.setError("Enter password longer than 5 chars");
                passwdTxt.requestFocus();
            } else {
                auth.signInWithEmailAndPassword(email,passwd).addOnCompleteListener(this, task-> {
                    if (task.isSuccessful() & coordinatorList.contains(email)) {
                        Intent i = new Intent(this,RegisterEventActivity.class);
                        i.putExtra("clubName", (email.split("@")[0]).toLowerCase());
                        startActivity(i);
                    } else {
                        // make user already exist error prompt
                        emailTxt.setError("Invalid Credentials");
                        emailTxt.requestFocus();
                    }
                });
            }
        });
        passwdTxt.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right = 2;
                if (event.getAction() == MotionEvent.ACTION_UP){
                    if(event.getRawX()>=passwdTxt.getRight()-passwdTxt.getCompoundDrawables()[Right].getBounds().width()){
                        int selection = passwdTxt.getSelectionEnd();
                        if(passwordVisible){
                            passwdTxt.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_visibility_off_24,0);
                            passwdTxt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible = false;
                        }
                        else {
                            passwdTxt.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_visibility_24,0);
                            passwdTxt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible = true;
                        }
                        passwdTxt.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });
    }
}