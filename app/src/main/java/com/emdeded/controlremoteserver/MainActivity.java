package com.emdeded.controlremoteserver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * MainActivity - Màn hình chính (MainCity) sau khi đăng nhập thành công
 */
public class MainActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private Button btnRemoteControl;
    private Button btnDashboard;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo các view
        tvWelcome = findViewById(R.id.tvWelcome);
        btnRemoteControl = findViewById(R.id.btnRemoteControl);
        btnDashboard = findViewById(R.id.btnDashboard);
        btnLogout = findViewById(R.id.btnLogout);

        // Hiển thị lời chào
        displayWelcomeMessage();

        // Xử lý sự kiện click
        btnRemoteControl.setOnClickListener(view -> navigateToRemoteControl());
        btnDashboard.setOnClickListener(view -> navigateToDashboard());
        btnLogout.setOnClickListener(view -> handleLogout());
    }

    /**
     * Hiển thị lời chào người dùng
     */
    private void displayWelcomeMessage() {
        tvWelcome.setText("Chào mừng đến MainCity!\nBạn đã đăng nhập thành công.");
    }

    /**
     * Điều hướng tới RemoteControlActivity
     */
    private void navigateToRemoteControl() {
        Intent intent = new Intent(MainActivity.this, RemoteControlActivity.class);
        startActivity(intent);
    }

    /**
     * Điều hướng tới DashboardActivity
     */
    private void navigateToDashboard() {
        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
        startActivity(intent);
    }

    /**
     * Xử lý logout - quay lại màn hình login
     */
    private void handleLogout() {
        // Xóa dữ liệu session/preferences nếu có (tùy chọn)
        clearSession();

        // Quay lại LoginActivity
        finish();
        Toast.makeText(MainActivity.this, "Đã đăng xuất!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Xóa dữ liệu phiên làm việc
     */
    private void clearSession() {
        // TODO: Xóa token, user info từ SharedPreferences hoặc database
    }

    /**
     * Ngăn không cho người dùng quay lại màn hình login bằng back button
     */
    @Override
    public void onBackPressed() {
        // Nếu muốn cho phép back, comment dòng super.onBackPressed()
         super.onBackPressed();
        
        // Nếu không muốn cho phép back, chỉ cần logout
        handleLogout();
    }
}