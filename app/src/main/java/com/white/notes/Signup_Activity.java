package com.white.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class Signup_Activity extends AppCompatActivity {
    TextView txtlogin;
    EditText Eusername,Epassword,Econfirmpassword;
    Button SignUpbtn;
    FirebaseAuth firebaseAuth;
    ProgressBar progressbarsignup;
    FirebaseFirestore fb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();
        firebaseAuth=FirebaseAuth.getInstance();
        fb=FirebaseFirestore.getInstance();
        FirebaseUser fu=firebaseAuth.getCurrentUser();

        SignUpbtn=findViewById(R.id.Signupbtn);
        progressbarsignup=findViewById(R.id.progressbarsignup);
        Eusername=findViewById(R.id.Eusername);
        Epassword=findViewById(R.id.Epassword);
        Econfirmpassword=findViewById(R.id.EConfirmpassword);
        txtlogin=findViewById(R.id.txtSignuplogin);

        SignUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressbarsignup.setVisibility(View.VISIBLE);
                String Username=Eusername.getText().toString().trim();
                String password=Epassword.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String confirmpassword=Econfirmpassword.getText().toString().trim();
                if(Username.isEmpty()){
                    Toast.makeText(Signup_Activity.this, "Enter Username or email", Toast.LENGTH_SHORT).show();
                    progressbarsignup.setVisibility(View.INVISIBLE);
                }
                else if (password.isEmpty()){
                    Toast.makeText(Signup_Activity.this, "Enter a password", Toast.LENGTH_SHORT).show();
                    progressbarsignup.setVisibility(View.INVISIBLE);

                }
                else if (!Username.matches(emailPattern))
                {
                    Toast.makeText(Signup_Activity.this, "Enter a valid email", Toast.LENGTH_SHORT).show();
                    progressbarsignup.setVisibility(View.INVISIBLE);
                }

                else if (confirmpassword.isEmpty()){
                    Toast.makeText(Signup_Activity.this, "Enter a password", Toast.LENGTH_SHORT).show();
                    progressbarsignup.setVisibility(View.INVISIBLE);

                }
                else if (!password.matches(confirmpassword)){
                    Toast.makeText(Signup_Activity.this, "Eneter same password", Toast.LENGTH_SHORT).show();
                    progressbarsignup.setVisibility(View.INVISIBLE);

                }

                else {

                    firebaseAuth.createUserWithEmailAndPassword(Username, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                    if (task.isSuccessful())
                                    {Toast.makeText(Signup_Activity.this, "Registered ", Toast.LENGTH_SHORT).show();
                                    sendemailverification();
                                    }
                                    else {
                                        Toast.makeText(Signup_Activity.this, "Registering Problem", Toast.LENGTH_SHORT).show();
                                    }
                                    progressbarsignup.setVisibility(View.INVISIBLE);
                                }

                            });

                }
            }
        });

        txtlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Loginpage();
            }
        });

    }
    public void sendemailverification(){
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    Toast.makeText(Signup_Activity.this, "verification link has been sent to your email ", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                   Loginpage();
                }
            });
        }
        else {
            Toast.makeText(this, "Error sending email Varification link ", Toast.LENGTH_SHORT).show();
        }
    }
    public void Loginpage(){
        Intent intent=new Intent(Signup_Activity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}