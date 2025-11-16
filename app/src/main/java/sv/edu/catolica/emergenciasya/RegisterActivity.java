package sv.edu.catolica.emergenciasya;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etEmailRegister, etPasswordRegister, etConfirmPassword;
    private Button btnRegister;
    private TextView tvGoToLogin;
    private UserDatabase userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userDatabase = UserDatabase.getInstance(this);

        etName = findViewById(R.id.etName);
        etEmailRegister = findViewById(R.id.etEmailRegister);
        etPasswordRegister = findViewById(R.id.etPasswordRegister);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvGoToLogin = findViewById(R.id.tvGoToLogin);

        btnRegister.setOnClickListener(v -> registerUser());

        tvGoToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LogIn.class);
            // Para que no se acumulen activities
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String email = etEmailRegister.getText().toString().trim();
        String password = etPasswordRegister.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validaciones básicas
        if (name.isEmpty()) {
            etName.setError("Requerido");
            etName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            etEmailRegister.setError("Requerido");
            etEmailRegister.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmailRegister.setError("Correo no válido");
            etEmailRegister.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPasswordRegister.setError("Requerido");
            etPasswordRegister.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPasswordRegister.setError("Mínimo 6 caracteres");
            etPasswordRegister.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Las contraseñas no coinciden");
            etConfirmPassword.requestFocus();
            return;
        }

        // ¿Ya existe el correo?
        User existing = userDatabase.userDao().getUserByEmail(email);
        if (existing != null) {
            Toast.makeText(this, "Ya existe un usuario con este correo", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear usuario
        User user = new User(name, email, password);
        userDatabase.userDao().insertUser(user);

        Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();

        // Ir al login
        Intent intent = new Intent(RegisterActivity.this, LogIn.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
