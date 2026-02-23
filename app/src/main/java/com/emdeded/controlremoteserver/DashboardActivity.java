package com.emdeded.controlremoteserver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

/**
 * DashboardActivity - Bảng điều khiển chính
 */
public class DashboardActivity extends AppCompatActivity {

    private Button btnRemoteControl;
    private Button btnCameraStream;
    private Button btnServerStatus;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Khởi tạo views
        btnRemoteControl = findViewById(R.id.btnRemoteControl);
        btnCameraStream = findViewById(R.id.btnCameraStream);
        btnServerStatus = findViewById(R.id.btnServerStatus);
        btnBack = findViewById(R.id.btnBack);

        // Xử lý sự kiện click
        btnRemoteControl.setOnClickListener(view -> navigateToRemoteControl());
        btnCameraStream.setOnClickListener(view -> navigateToCameraStream());
        btnServerStatus.setOnClickListener(view -> navigateToServerStatus());
        btnBack.setOnClickListener(view -> finish());
    }

    /**
     * Điều hướng tới RemoteControlActivity
     */
    private void navigateToRemoteControl() {
        Intent intent = new Intent(DashboardActivity.this, RemoteControlActivity.class);
        startActivity(intent);
    }

    /**
     * Điều hướng tới CameraStreamActivity
     */
    private void navigateToCameraStream() {
        Intent intent = new Intent(DashboardActivity.this, CameraStreamActivity.class);
        startActivity(intent);
    }

    /**
     * Điều hướng tới ServerStatusActivity (sẽ tạo sau)
     */
    private void navigateToServerStatus() {
        // TODO: Tạo ServerStatusActivity
        // Intent intent = new Intent(DashboardActivity.this, ServerStatusActivity.class);
        // startActivity(intent);
    }
}
