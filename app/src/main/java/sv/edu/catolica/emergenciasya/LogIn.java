package sv.edu.catolica.emergenciasya;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LogIn extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private UserDatabase userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userDatabase = UserDatabase.getInstance(this);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        // Si ya está logueado, saltar login
        boolean isLoggedIn = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                .getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            Intent intent = new Intent(LogIn.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }


        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty()) {
                etEmail.setError("Requerido");
                etEmail.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                etPassword.setError("Requerido");
                etPassword.requestFocus();
                return;
            }

            // Validar en la base de datos
            User user = userDatabase.userDao().login(email, password);

            if (user == null) {
                Toast.makeText(LogIn.this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            } else {
                // Guardar sesión
                getSharedPreferences("UserPrefs", MODE_PRIVATE)
                        .edit()
                        .putBoolean("isLoggedIn", true)
                        .putString("userEmail", email)
                        .apply();

                // Redirigir al MainActivity
                Intent intent = new Intent(LogIn.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LogIn.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
