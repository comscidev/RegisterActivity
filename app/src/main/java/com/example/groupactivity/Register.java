package com.example.groupactivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private static final String TAG = "Registration";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        EditText fullNameEditText = findViewById(R.id.fullname);
        EditText emailEditText = findViewById(R.id.email);
        EditText passwordEditText = findViewById(R.id.password);
        EditText retypePasswordEditText = findViewById(R.id.retype_password);
        Button registerButton = findViewById(R.id.register_btn);
        EditText userTypeEditText = findViewById(R.id.user_type);
        TextView login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent GotoLogin = new Intent(Register.this, MainActivity.class);
                startActivity(GotoLogin);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = fullNameEditText.getText().toString();
                String userEmail = emailEditText.getText().toString();
                String userPassword = passwordEditText.getText().toString();
                String retypePassword = retypePasswordEditText.getText().toString();
                String userType = userTypeEditText.getText().toString();

                if (!userPassword.equals(retypePassword)) {
                    Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                DocumentReference documentReference = db.collection("users").document(userEmail);
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                Log.d(TAG, "Email is taken!");
                                Toast.makeText(Register.this, "Email is taken!", Toast.LENGTH_SHORT).show();
                            } else {
                                Map<String, Object> user = new HashMap<>();
                                user.put("fullname", fullName);
                                user.put("email", userEmail);
                                user.put("user-type", userType);
                                user.put("password", userPassword);

                                db.collection("users")
                                        .document(userEmail)
                                        .set(user)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "Registration Success!");
                                                    Toast.makeText(Register.this, "Registration Success!", Toast.LENGTH_SHORT).show();
                                                    Intent gotoLoginPage = new Intent(Register.this, MainActivity.class);
                                                    startActivity(gotoLoginPage);
                                                } else {
                                                    Log.e(TAG, "Registration Failed: " + task.getException());
                                                    Toast.makeText(Register.this, "Registration Failed! Try again!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            }
                        } else {
                            Log.e(TAG, "Error checking email: " + task.getException());
                            Toast.makeText(Register.this, "Error checking email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
