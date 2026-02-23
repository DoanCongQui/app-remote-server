package com.emdeded.controlremoteserver;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * CameraStreamActivity - Xem stream camera từ Tinker Board
 */
public class CameraStreamActivity extends AppCompatActivity {

    private ImageView ivCameraStream;
    private ProgressBar progressBar;
    private TextView tvStreamStatus;
    private boolean isStreamingActive = true;
    private static final String CAMERA_STREAM_URL = "http://100.114.246.3:8000/camera/mjpeg";
    private static final int STREAM_TIMEOUT = 10000; // 10 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_stream);

        // Khởi tạo views
        ivCameraStream = findViewById(R.id.ivCameraStream);
        progressBar = findViewById(R.id.progressBar);
        tvStreamStatus = findViewById(R.id.tvStreamStatus);

        // Bắt đầu stream camera
        startCameraStream();
    }

    /**
     * Bắt đầu stream camera MJPEG
     */
    private void startCameraStream() {
        new Thread(() -> {
            try {
                progressBar.setVisibility(ProgressBar.VISIBLE);
                tvStreamStatus.setText("Đang kết nối đến camera...");

                URL url = new URL(CAMERA_STREAM_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(STREAM_TIMEOUT);
                connection.setReadTimeout(STREAM_TIMEOUT);
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    updateStreamStatus("Lỗi kết nối: " + responseCode, true);
                    return;
                }

                // Đọc stream MJPEG
                InputStream inputStream = connection.getInputStream();
                byte[] buffer = new byte[8192];
                int bytesRead;

                updateStreamStatus("Đang phát stream...", false);
                progressBar.setVisibility(ProgressBar.GONE);

                // Parse MJPEG frames
                while (isStreamingActive && (bytesRead = inputStream.read(buffer)) != -1) {
                    // Find JPEG boundary
                    byte[] frameData = extractJpegFrame(buffer, bytesRead);
                    if (frameData != null && frameData.length > 0) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(frameData, 0, frameData.length);
                        if (bitmap != null) {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                ivCameraStream.setImageBitmap(bitmap);
                            });
                        }
                    }
                }

                inputStream.close();
                connection.disconnect();

            } catch (Exception e) {
                updateStreamStatus("Lỗi: " + e.getMessage(), true);
            }
        }).start();
    }

    /**
     * Extract JPEG frame từ MJPEG stream
     */
    private byte[] extractJpegFrame(byte[] buffer, int length) {
        // Tìm JPEG header (FFD8) và footer (FFD9)
        int start = -1;
        int end = -1;

        for (int i = 0; i < length - 1; i++) {
            if ((buffer[i] & 0xFF) == 0xFF && (buffer[i + 1] & 0xFF) == 0xD8) {
                start = i;
            }
            if ((buffer[i] & 0xFF) == 0xFF && (buffer[i + 1] & 0xFF) == 0xD9) {
                end = i + 2;
                break;
            }
        }

        if (start != -1 && end != -1) {
            byte[] frameData = new byte[end - start];
            System.arraycopy(buffer, start, frameData, 0, end - start);
            return frameData;
        }
        return null;
    }

    /**
     * Cập nhật trạng thái stream
     */
    private void updateStreamStatus(String message, boolean isError) {
        new Handler(Looper.getMainLooper()).post(() -> {
            tvStreamStatus.setText(message);
            if (isError) {
                tvStreamStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                progressBar.setVisibility(ProgressBar.GONE);
                isStreamingActive = false;
            } else {
                tvStreamStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isStreamingActive = false;
    }
}
