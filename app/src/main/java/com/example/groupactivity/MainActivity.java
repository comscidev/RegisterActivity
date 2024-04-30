package com.example.groupactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_main);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            EditText email_address = findViewById(R.id.email_input);
            EditText password = findViewById(R.id.password_input);
            Button login_btn = findViewById(R.id.login_btn);
            TextView Sign_up = findViewById(R.id.Sign_up);


            Sign_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent GotoRegisterPage = new Intent(MainActivity.this, Register.class);
                    startActivity(GotoRegisterPage);
                }
            });

            login_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String Login_email = email_address.getText().toString();
                    String Login_password = password.getText().toString();

                    DocumentReference documentReference = db.collection("users").document(Login_email);

                    documentReference.get().addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot.exists()){
                                String specificpassword = documentSnapshot.getString("password");
                                if(specificpassword.equals(Login_password)){
                                    String user_type = documentSnapshot.getString("user-type");
                                    if(user_type.equals("admin")){
                                        Intent GotoAdminDashboard = new Intent(MainActivity.this, AdminDashboard.class);
                                        startActivity(GotoAdminDashboard);
                                        Toast.makeText(MainActivity.this, "Welcome Admin", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Intent GoToStudentDashboard = new Intent(MainActivity.this, StudentDashboard.class);
                                        startActivity(GoToStudentDashboard);
                                        Toast.makeText(MainActivity.this,"Welcome Student", Toast.LENGTH_SHORT).show();
                                    }

                                }else{
                                    Toast.makeText(MainActivity.this, "Incorrect passsword", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(MainActivity.this, "Does not exist", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(MainActivity.this, "Error Logging in", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });

        }
    }