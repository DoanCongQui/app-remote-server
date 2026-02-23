package com.emdeded.controlremoteserver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo các view
        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.loginButton);

        // Xử lý sự kiện click nút Login
        btnLogin.setOnClickListener(view -> handleLogin());
    }

    /**
     * Xử lý logic đăng nhập
     */
    private void handleLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Kiểm tra dữ liệu nhập vào
        if (!validateInput(username, password)) {
            return;
        }

        // Kiểm tra thông tin đăng nhập
        if (isValidCredentials(username, password)) {
            showToast("Đăng nhập thành công!");
            navigateToMainCity();
        } else {
            showToast("Tên đăng nhập hoặc mật khẩu không chính xác!");
            etPassword.setText(""); // Xóa mật khẩu khi sai
            etPassword.requestFocus(); // Focus vào password field
        }
    }

    /**
     * Kiểm tra dữ liệu nhập vào có hợp lệ không
     *
     * @param username Tên đăng nhập
     * @param password Mật khẩu
     * @return true nếu dữ liệu hợp lệ, false nếu không
     */
    private boolean validateInput(String username, String password) {
        // Kiểm tra username không để trống
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Vui lòng nhập tên đăng nhập");
            etUsername.requestFocus();
            return false;
        }

        // Kiểm tra username có ít nhất 3 ký tự
        if (username.length() < 3) {
            etUsername.setError("Tên đăng nhập phải có ít nhất 3 ký tự");
            etUsername.requestFocus();
            return false;
        }

        // Kiểm tra password không để trống
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Vui lòng nhập mật khẩu");
            etPassword.requestFocus();
            return false;
        }

        // Kiểm tra password có ít nhất 4 ký tự
        if (password.length() < 4) {
            etPassword.setError("Mật khẩu phải có ít nhất 4 ký tự");
            etPassword.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Kiểm tra thông tin đăng nhập có đúng không
     * TODO: Sau này thay bằng API call tới backend
     *
     * @param username Tên đăng nhập
     * @param password Mật khẩu
     * @return true nếu đúng, false nếu sai
     */
    private boolean isValidCredentials(String username, String password) {
        // Thông tin đăng nhập mặc định (tạm thời)
        String correctUsername = "admin";
        String correctPassword = "1234";

        return username.equals(correctUsername) && password.equals(correctPassword);
    }

    /**
     * Chuyển sang màn hình MainCity
     */
    private void navigateToMainCity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        // Xóa tất cả các activity trước đó để tránh quay lại màn hình login bằng back button
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Hiển thị Toast message
     *
     * @param message Nội dung message
     */
    private void showToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
