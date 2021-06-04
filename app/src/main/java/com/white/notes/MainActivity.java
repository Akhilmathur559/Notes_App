package com.white.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {
    TextView txtsignup,txtforget;
    EditText Eusername,Epassword;
    Button Loginbtn;
    FirebaseAuth fb;
    FirebaseUser firebaseUser;
    ProgressBar pbarlogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        Loginbtn=findViewById(R.id.loginbtn);
        Eusername=findViewById(R.id.Eusername);
        Epassword=findViewById(R.id.Epassword);
        fb=FirebaseAuth.getInstance();
        pbarlogin=findViewById(R.id.progressbarlogin);
        firebaseUser=fb.getCurrentUser();
        txtsignup=findViewById(R.id.txtSignup);
        txtforget=findViewById(R.id.txtforget);
        if (firebaseUser!=null){
            finish();
            startActivity(new Intent(MainActivity.this,Homepage.class));
        }

            txtsignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, Signup_Activity.class);
                    startActivity(intent);
                }
            });
        txtforget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Forget_password.class));
            }
        });
            Loginbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pbarlogin.setVisibility(View.VISIBLE);

                    String Username = Eusername.getText().toString().trim();
                    String password = Epassword.getText().toString().trim();
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                    if (Username.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Enter email id", Toast.LENGTH_SHORT).show();
                        pbarlogin.setVisibility(View.INVISIBLE);
                    } else if (password.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                        pbarlogin.setVisibility(View.INVISIBLE);
                    } else if (!Username.matches(emailPattern)) {
                        Toast.makeText(MainActivity.this, "Enter a valid email id", Toast.LENGTH_SHORT).show();
                        pbarlogin.setVisibility(View.INVISIBLE);
                    } else {
                        fb.signInWithEmailAndPassword(Username, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {

                                        if (task.isSuccessful()) {
                                            checkemailverification();

                                        }
                                        else{
                                            Toast.makeText(MainActivity.this, "Account does not exists", Toast.LENGTH_SHORT).show();
                                        }
                                        pbarlogin.setVisibility(View.INVISIBLE);
                                    }
                                });

                    }
                }
            });
        }


    public void checkemailverification(){
         firebaseUser=fb.getCurrentUser();
        if (firebaseUser.isEmailVerified()==true){
            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(MainActivity.this,Homepage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Verify Your email first", Toast.LENGTH_SHORT).show();
            fb.signOut();
        }
    }
}