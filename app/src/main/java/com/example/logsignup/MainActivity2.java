package com.example.logsignup;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity2 extends AppCompatActivity {

    Button signUpButton;
    TextInputEditText usernameSignUp, passwordSignUp, nimPengguna, emailPengguna;
    FirebaseAuth auth;
    DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("Users");

        signUpButton = findViewById(R.id.signUpBotton);
        usernameSignUp = findViewById(R.id.userSignUp);
        passwordSignUp = findViewById(R.id.password);
        nimPengguna = findViewById(R.id.nimPengguna);
        emailPengguna = findViewById(R.id.email);

        signUpButton.setOnClickListener(view -> {
            String userName = usernameSignUp.getText().toString().trim();
            String password = passwordSignUp.getText().toString().trim();
            String email = emailPengguna.getText().toString().trim();
            String NIM = nimPengguna.getText().toString().trim();

            if (TextUtils.isEmpty(userName)) {
                usernameSignUp.setError("Masukkan Username!");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                passwordSignUp.setError("Masukkan Password!");
                return;
            }
            if (password.length() < 6) {
                passwordSignUp.setError("Password minimal 6 karakter!");
                return;
            }
            if (TextUtils.isEmpty(email)) {
                emailPengguna.setError("Masukkan Email!");
                return;
            }
            if (TextUtils.isEmpty(NIM)) {
                nimPengguna.setError("Masukkan NIM!");
                return;
            }

            registerUser(userName, email, password, NIM);
        });
    }

    private void registerUser(String username, String email, String password, String NIM) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity2.this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser fUser = auth.getCurrentUser();
                if (fUser != null) {
                    String uid = fUser.getUid();

                    User userDetails = new User(uid, username, email, password, NIM);

                    databaseRef.child(uid).setValue(userDetails).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            fUser.sendEmailVerification();
                            Toast.makeText(MainActivity2.this, "Akun berhasil dibuat. Verifikasi email Anda!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity2.this, "Registrasi akun gagal!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Register: Error", task1.getException());
                        }
                    });
                }
            } else {
                Toast.makeText(MainActivity2.this, "Registrasi gagal: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Register: Error", task.getException());
            }
        });
    }
}
