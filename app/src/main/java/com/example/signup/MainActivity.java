package com.example.signup;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        EditText fname = findViewById(R.id.fullname);
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        EditText retype_password = findViewById(R.id.retype_password);
        Button button = findViewById(R.id.register_btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = fname.getText().toString().trim();
                String userEmail = email.getText().toString().trim();
                String userPassword = password.getText().toString().trim();
                String retypePassword = retype_password.getText().toString().trim();

                if (fullName.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty() || retypePassword.isEmpty()) {
                    Toast.makeText(MainActivity.this, "FAILED: FILL ALL THE FIELDS", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!userPassword.equals(retypePassword)) {
                    Toast.makeText(MainActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // All fields are filled and passwords match, proceed with registration

                Map<String, Object> user = new HashMap<>();
                user.put("fullname", fullName);
                user.put("email", userEmail);
                user.put("password", userPassword);

                db.collection("users")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(MainActivity.this, "Registration Success", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Failed Registration: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}