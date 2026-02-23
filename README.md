# Control&RemoteServer

Một ứng dụng Android đơn giản làm ví dụ cho màn hình đăng nhập (Login). Dự án chứa một activity chính với giao diện đăng nhập và logic kiểm tra tài khoản tĩnh (username/password). README này hướng dẫn cách thiết lập, build và chạy ứng dụng trên máy phát triển.

Các điểm chính:
- Ngôn ngữ: Java
- Compile SDK: 36, Min SDK: 26
- JDK: 11 (yêu cầu)

Ngược lại với mô tả giao diện, logic đăng nhập được xử lý trong `app/src/main/java/com/emdeded/controlremoteserver/MainActivity.java` và layout chính là `app/src/main/res/layout/activity_login.xml`.

Nhanh (Setup)

1. Cài đặt phần mềm cần thiết:
   - Android Studio (mạnh khuyên dùng) hoặc Android SDK + Gradle
   - JDK 11

2. Clone repository:

```
git clone <repo-url>
cd ControlRemoteServer
```

3. Mở dự án:
- Mở trực tiếp trong Android Studio: `File > Open` và chọn thư mục của dự án. Android Studio sẽ tải SDK/Gradle cần thiết theo cấu hình.
- Hoặc sử dụng command-line với Gradle wrapper:

```
./gradlew build
```

4. Chạy trên thiết bị/emulator:
- Từ Android Studio: bấm Run (chọn Emulator hoặc thiết bị thật).
- Từ dòng lệnh (cần thiết bị kết nối hoặc emulator đang chạy):

```
./gradlew installDebug    # cài APK lên thiết bị/emulator
adb logcat -s ActivityManager # (tùy chọn) theo dõi log
```

Thông tin hữu ích
- Tên ứng dụng: `Control&RemoteServer` (string resource tại `app/src/main/res/values/strings.xml`).
- Activity chính: `app/src/main/java/com/emdeded/controlremoteserver/MainActivity.java`.
- Giao diện đăng nhập: `app/src/main/res/layout/activity_login.xml`.
- AndroidManifest khai báo `MainActivity` làm LAUNCHER tại `app/src/main/AndroidManifest.xml`.

Thông tin đăng nhập mặc định (dùng để thử nghiệm):
- Username: `admin`
- Password: `1234`

Lưu ý / Troubleshooting nhanh
- Nếu Gradle yêu cầu SDK platform 36, cài bổ sung qua SDK Manager trong Android Studio.
- Nếu build báo lỗi JDK, kiểm tra `JAVA_HOME` trỏ tới JDK 11.
- Để cài lên thiết bị thật, bật USB debugging và cho phép cài đặt từ nguồn không xác định khi cần.

Đóng góp
- Mở issue hoặc gửi pull request. Nếu thêm tính năng xác thực thực sự, hãy tránh lưu mật khẩu cứng trong mã nguồn.

Tiếp theo gợi ý (tùy bạn):
1) Thay backend thật (API) cho xác thực;
2) Thêm kiểm thử đơn vị cho logic UI;
3) Di chuyển sang Kotlin để tận dụng các API hiện đại của Android.
