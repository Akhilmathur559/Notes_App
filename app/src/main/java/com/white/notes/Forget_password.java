package com.white.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class Forget_password extends AppCompatActivity {
EditText Eusername;
Button forgetbtn;
FirebaseAuth fb;
FirebaseUser fu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        Eusername=findViewById(R.id.Eusername);
        forgetbtn=findViewById(R.id.forgetbtn);
        fb=FirebaseAuth.getInstance();
        fu=fb.getCurrentUser();
        forgetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=Eusername.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (mail.isEmpty()){
                    Toast.makeText(Forget_password.this, "Enter email", Toast.LENGTH_SHORT).show();
                }
                else if (!mail.matches(emailPattern)) {
                    Toast.makeText(Forget_password.this, "Enter a valid email id", Toast.LENGTH_SHORT).show();
                    //pbarlogin.setVisibility(View.INVISIBLE);
                }
                else {
                    fb.sendPasswordResetEmail(mail)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(Forget_password.this, "Password has been reset! please check your email", Toast.LENGTH_SHORT).show();
                                        Loginpage();
                                    }
                                    else{
                                        Toast.makeText(Forget_password.this, "Error in reseting Your Password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });



                }
            }
        });
    }
    public void Loginpage(){
        Intent intent=new Intent(Forget_password.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}