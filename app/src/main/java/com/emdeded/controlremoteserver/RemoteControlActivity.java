package com.emdeded.controlremoteserver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.emdeded.controlremoteserver.api.TinkerBoardApiClient;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * RemoteControlActivity - Điều khiển các thiết bị từ xa
 * Giao diện chính để bật/tắt Relay trên Tinker Board với auto-refresh
 */
public class RemoteControlActivity extends AppCompatActivity {
    private static final String TAG = "RemoteControl";
    private static final long AUTO_REFRESH_INTERVAL = 30000; // 30 giây

    private TextView tvStatus;
    private TextView tvServerStatus;
    private ProgressBar pbLoading;
    private LinearLayout llRelayContainer;
    private Button btnRefresh;
    private Handler uiHandler;
    private Handler autoRefreshHandler;
    private Runnable autoRefreshRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_control);

        uiHandler = new Handler(Looper.getMainLooper());
        autoRefreshHandler = new Handler(Looper.getMainLooper());

        // Khởi tạo views
        tvStatus = findViewById(R.id.tvStatus);
        tvServerStatus = findViewById(R.id.tvServerStatus);
        pbLoading = findViewById(R.id.pbLoading);
        llRelayContainer = findViewById(R.id.llRelayContainer);
        btnRefresh = findViewById(R.id.btnRefresh);

        // Set up refresh button
        btnRefresh.setOnClickListener(v -> {
            cancelAutoRefresh();
            fetchStatus();
            scheduleAutoRefresh();
        });

        // Lấy status lần đầu
        fetchStatus();
        
        // Bắt đầu auto-refresh
        scheduleAutoRefresh();
    }

    /**
     * Lập lịch auto-refresh
     */
    private void scheduleAutoRefresh() {
        autoRefreshRunnable = () -> {
            fetchStatus();
            scheduleAutoRefresh();
        };
        autoRefreshHandler.postDelayed(autoRefreshRunnable, AUTO_REFRESH_INTERVAL);
    }

    /**
     * Hủy auto-refresh
     */
    private void cancelAutoRefresh() {
        if (autoRefreshRunnable != null) {
            autoRefreshHandler.removeCallbacks(autoRefreshRunnable);
        }
    }

    /**
     * Lấy status từ API
     */
    private void fetchStatus() {
        showLoading(true);
        tvStatus.setText("Đang kết nối...");

        // Chạy API call trên background thread
        new Thread(() -> {
            try {
                JSONObject response = TinkerBoardApiClient.getStatus();
                handleStatusResponse(response);
            } catch (Exception e) {
                Log.e(TAG, "Error fetching status", e);
                handleStatusError(e.getMessage());
            }
        }).start();
    }

    /**
     * Xử lý response thành công từ API
     */
    private void handleStatusResponse(JSONObject response) {
        uiHandler.post(() -> {
            try {
                // Parse relay states
                List<Map<String, Object>> relays = TinkerBoardApiClient.parseRelayStates(response);

                // Parse server status
                String serverStatus = parseServerStatus(response);
                updateServerStatusUI(serverStatus);

                // Update UI
                updateRelayUI(relays);
                tvStatus.setText("Kết nối thành công ✓");
                showLoading(false);

                Log.d(TAG, "Status fetched successfully. Relays: " + relays.size());
            } catch (Exception e) {
                Log.e(TAG, "Error handling status response", e);
                tvStatus.setText("Lỗi: " + e.getMessage());
                showLoading(false);
            }
        });
    }

    /**
     * Xử lý lỗi khi lấy status
     */
    private void handleStatusError(String error) {
        uiHandler.post(() -> {
            Log.e(TAG, "Status error: " + error);
            tvStatus.setText("❌ Lỗi: " + error);
            tvServerStatus.setText("Trạng thái: Offline");
            tvServerStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            llRelayContainer.removeAllViews();
            showLoading(false);
            Toast.makeText(RemoteControlActivity.this, "Không thể kết nối: " + error, Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Parse server status từ response
     */
    private String parseServerStatus(JSONObject response) {
        try {
            if (response.has("servers")) {
                JSONObject servers = response.getJSONObject("servers");
                boolean online = servers.optBoolean("online", false);
                return online ? "Online ✓" : "Offline ✗";
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing server status", e);
        }
        return "Unknown";
    }

    /**
     * Cập nhật Server Status UI
     */
    private void updateServerStatusUI(String status) {
        tvServerStatus.setText("Trạng thái Server: " + status);
        if (status.contains("Online")) {
            tvServerStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            tvServerStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }
    }

    /**
     * Cập nhật UI với relay states
     */
    private void updateRelayUI(List<Map<String, Object>> relays) {
        llRelayContainer.removeAllViews();

        for (Map<String, Object> relay : relays) {
            int relayId = ((Number) relay.get("id")).intValue();
            boolean state = (boolean) relay.get("state");

            // Tạo relay item view
            View relayItemView = createRelayItemView(relayId, state);
            llRelayContainer.addView(relayItemView);
        }
    }

    /**
     * Tạo view cho một relay item
     */
    private View createRelayItemView(int relayId, boolean currentState) {
        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);
        itemLayout.setPadding(16, 16, 16, 16);

        // Label
        TextView tvLabel = new TextView(this);
        tvLabel.setText("Relay " + relayId);
        tvLabel.setTextSize(16);
        tvLabel.setTextColor(getResources().getColor(android.R.color.black));
        tvLabel.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        // Toggle Button
        Button btnToggle = new Button(this);
        btnToggle.setText(currentState ? "BẬT ✓" : "TẮT");
        btnToggle.setBackgroundColor(currentState ?
                getResources().getColor(android.R.color.holo_green_dark) :
                getResources().getColor(android.R.color.holo_red_dark)
        );
        btnToggle.setTextColor(getResources().getColor(android.R.color.white));
        btnToggle.setLayoutParams(new LinearLayout.LayoutParams(
                200,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Xử lý click
        btnToggle.setOnClickListener(v -> toggleRelay(relayId, !currentState, btnToggle));

        itemLayout.addView(tvLabel);
        itemLayout.addView(btnToggle);

        return itemLayout;
    }

    /**
     * Bật/tắt relay
     */
    private void toggleRelay(int relayId, boolean newState, Button button) {
        button.setEnabled(false);
        String stateStr = newState ? "on" : "off";

        new Thread(() -> {
            try {
                JSONObject response = TinkerBoardApiClient.setRelay(relayId, stateStr);
                uiHandler.post(() -> {
                    // Cập nhật button UI
                    button.setText(newState ? "BẬT ✓" : "TẮT");
                    button.setBackgroundColor(newState ?
                            getResources().getColor(android.R.color.holo_green_dark) :
                            getResources().getColor(android.R.color.holo_red_dark)
                    );
                    button.setEnabled(true);
                    Toast.makeText(RemoteControlActivity.this,
                            "Relay " + relayId + " đã " + (newState ? "bật" : "tắt"),
                            Toast.LENGTH_SHORT).show();

                    // Cập nhật lại status
                    fetchStatus();
                });
            } catch (Exception e) {
                Log.e(TAG, "Error toggling relay", e);
                uiHandler.post(() -> {
                    button.setEnabled(true);
                    Toast.makeText(RemoteControlActivity.this,
                            "Lỗi: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    /**
     * Hiển thị/ẩn loading indicator
     */
    private void showLoading(boolean show) {
        pbLoading.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelAutoRefresh();
    }
}
