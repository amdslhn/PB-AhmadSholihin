package com.example.logsignup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    TextInputEditText userName, password;
    CheckBox remember;
    Button login;
    TextView forgotPassword, logNow;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userName = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        remember = findViewById(R.id.rememberMe);
        login = findViewById(R.id.login);
        forgotPassword = findViewById(R.id.forgotPassword);
        logNow = findViewById(R.id.logNow);

        login.setOnClickListener(view -> {
            String email = String.valueOf(userName.getText()).trim();
            String passwordUser = String.valueOf(password.getText()).trim();

            if (email.isEmpty()) {
                userName.setError("Email tidak boleh kosong!");
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                userName.setError("Format email tidak valid!");
                return;
            }
            if (passwordUser.isEmpty()) {
                password.setError("Password tidak boleh kosong!");
                return;
            }

            mAuth.signInWithEmailAndPassword(email, passwordUser)
                    .addOnSuccessListener(authResult -> {
                        Toast.makeText(MainActivity.this, "Login Berhasil", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MainActivity.this, HomeAja.class));
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(MainActivity.this, "Login Gagal: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });

        logNow.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, MainActivity2.class));
        });
    }
}
