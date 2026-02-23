# Hướng Dẫn Sử Dụng - Control&RemoteServer

## 📋 Tổng Quan

Ứng dụng **Control&RemoteServer** là một ứng dụng Android đơn giản với:
- 🔐 **Màn hình đăng nhập** (LoginActivity) - xác thực người dùng
- 🏙️ **Màn hình chính** (MainActivity) - hiển thị sau khi đăng nhập thành công
- 📱 **Giao diện thân thiện** - UI modern với Material Design

---

## 🚀 Bắt Đầu

### Yêu Cầu Hệ Thống

- **Android Studio:** Phiên bản mới nhất
- **Android SDK:** 36 (Compile), 26+ (Minimum)
- **JDK:** 11 hoặc cao hơn
- **Gradle:** 8.12.3 (bao gồm trong dự án)

### Cài Đặt & Chạy Ứng Dụng

```bash
# 1. Clone repository
git clone <repo-url>
cd AppRemoteServer

# 2. Build ứng dụng
./gradlew clean assembleDebug

# 3. Cài đặt lên thiết bị/emulator
./gradlew installDebug

# 4. Chạy ứng dụng
# Hoặc bấm Run từ Android Studio
```

---

## 🔑 Thông Tin Đăng Nhập

### Tài Khoản Demo
- **Tên đăng nhập:** `admin`
- **Mật khẩu:** `1234`

⚠️ **CẢNH BÁO:** Đây chỉ là thông tin demo. Trong production, phải sử dụng backend xác thực thực sự!

---

## 📱 Màn Hình Chính

### 1. LoginActivity (Màn Hình Đăng Nhập)

**Vị trí File:**
- Java: `app/src/main/java/com/emdeded/controlremoteserver/LoginActivity.java`
- Layout: `app/src/main/res/layout/activity_login.xml`

**Tính Năng:**
- ✅ Nhập tên đăng nhập
- ✅ Nhập mật khẩu (ẩn ký tự)
- ✅ Kiểm tra dữ liệu nhập vào:
  - Tên đăng nhập phải ≥ 3 ký tự
  - Mật khẩu phải ≥ 4 ký tự
  - Không được để trống
- ✅ Thông báo lỗi chi tiết
- ✅ Chuyển sang MainCity nếu đăng nhập thành công
- ✅ Xóa mật khẩu nếu đăng nhập thất bại

**Luồng Đăng Nhập:**

```
Người dùng nhập thông tin
    ↓
Kiểm tra rỗng/độ dài
    ↓
Kiểm tra thông tin chính xác
    ↓
✓ Đúng → Hiển thị "Đăng nhập thành công!" → Vào MainCity
✗ Sai  → Hiển thị "Tên hoặc mật khẩu sai!" → Ở lại LoginActivity
```

### 2. MainActivity (Màn Hình Chính - MainCity)

**Vị trí File:**
- Java: `app/src/main/java/com/emdeded/controlremoteserver/MainActivity.java`
- Layout: `app/src/main/res/layout/activity_main.xml`

**Tính Năng:**
- 🏙️ Hiển thị lời chào "Chào mừng đến MainCity!"
- 📊 Hiển thị trạng thái: "Đã đăng nhập"
- 🔚 Nút "Đăng Xuất" - quay lại màn hình login
- ⏱️ Thông tin: "Phiên làm việc sẽ hết hạn sau 30 phút"

**Tính Năng Bảo Mật:**
- ✅ Khi người dùng press "Back" → Tự động logout
- ✅ Khi vào MainCity → Xóa stack activity login (không quay lại được)

---

## 🔧 Cấu Trúc Code

```
app/src/main/
├── java/com/emdeded/controlremoteserver/
│   ├── LoginActivity.java      ← Xử lý đăng nhập
│   └── MainActivity.java        ← Hiển thị màn hình chính
├── res/
│   ├── layout/
│   │   ├── activity_login.xml  ← Giao diện login
│   │   └── activity_main.xml   ← Giao diện main
│   ├── drawable/
│   │   ├── ic_baseline_person_24.xml  ← Icon người dùng
│   │   ├── ic_lock_24.xml             ← Icon khóa
│   │   ├── backgrond.png              ← Hình nền
│   │   └── custom_edittext.xml        ← Style EditText
│   ├── values/
│   │   ├── strings.xml         ← Chuỗi văn bản
│   │   ├── colors.xml          ← Màu sắc
│   │   ├── themes.xml          ← Chủ đề
│   │   └── styles.xml          ← Style
│   └── values-night/
│       └── themes.xml          ← Chủ đề dark mode
└── AndroidManifest.xml         ← Cấu hình ứng dụng
```

---

## 🎨 Giao Diện & Tùy Chỉnh

### Thay Đổi Màu Sắc

**File:** `app/src/main/res/values/colors.xml`

```xml
<color name="blue_background">#013ABD</color>  <!-- Màu xanh chính -->
<color name="white">#FFFFFFFF</color>          <!-- Trắng -->
<color name="black">#FF000000</color>          <!-- Đen -->
<color name="purple">#8692f7</color>           <!-- Tím -->
```

### Thay Đổi Chuỗi Văn Bản

**File:** `app/src/main/res/values/strings.xml`

```xml
<string name="login_title">Đăng Nhập</string>
<string name="hint_username">Tên đăng nhập</string>
<string name="hint_password">Mật khẩu</string>
```

---

## 🔐 Bảo Mật

### Hiện Tại (Demo)
- ⚠️ Hard-code tài khoản demo trong code
- ⚠️ Không mã hóa password
- ⚠️ Không bật minification

### TODO - Cải Thiện Bảo Mật

1. **Triển Khai Backend Xác Thực:**
   ```java
   // Thay vì kiểm tra hard-code
   private boolean isValidCredentials(String username, String password) {
       // TODO: Gọi API backend
       // return apiService.login(username, password);
   }
   ```

2. **Lưu Token Bảo Mật:**
   ```java
   // Sử dụng EncryptedSharedPreferences
   EncryptedSharedPreferences preferences = 
       EncryptedSharedPreferences.create(...);
   preferences.edit().putString("auth_token", token).apply();
   ```

3. **Bật ProGuard/R8:**
   ```gradle
   buildTypes {
       release {
           minifyEnabled = true  // Bật minification
           shrinkResources = true
       }
   }
   ```

4. **Yêu Cầu HTTPS:**
   ```java
   // Trong logrequests
   OkHttpClient client = new OkHttpClient.Builder()
       .certificatePinner(/* config */)
       .build();
   ```

---

## 📝 Ghi Chép Kỹ Thuật

### Các Thay Đổi Từ Phiên Bản Cũ

| Vấn Đề | Trước | Sau |
|--------|-------|-----|
| File Activity Login | ❌ Không tồn tại | ✅ LoginActivity.java |
| Layout Activity | 1 file (trùng lặp) | ✅ 2 file (khác nhau) |
| drawableLeft | ❌ Deprecated | ✅ drawableStart |
| findViewById | ❌ Không an toàn | ✅ Vẫn dùng (TODO: View Binding) |
| Hard-code String | ❌ Trong XML/Java | ✅ Trong strings.xml |
| Hard-code Credentials | ❌ Vẫn hard-code | ⚠️ Vẫn hard-code (TODO) |
| INTERNET Permission | ❌ Thiếu | ✅ Thêm vào |

### Cải Thiện Mã Nguồn

- ✅ Thêm kiểm tra dữ liệu nhập vào
- ✅ Thêm Accessibility (contentDescription)
- ✅ Sử dụng Lambda expression
- ✅ Thêm comment và JavaDoc
- ✅ Cấu trúc code rõ ràng
- ✅ Loại bỏ mã comment
- ✅ Xóa dependencies trùng lặp

---

## 🧪 Kiểm Thử

### Kiểm Thử Thủ Công

1. **Kiểm Thử Đăng Nhập Thành Công**
   - Nhập: `admin` / `1234`
   - Kết quả: ✅ Vào MainCity

2. **Kiểm Thử Đăng Nhập Thất Bại**
   - Nhập: `user` / `wrong`
   - Kết quả: ❌ Thông báo lỗi, ở lại LoginActivity

3. **Kiểm Thử Validation**
   - Nhập: `ab` (< 3 ký tự)
   - Kết quả: ❌ Lỗi "Ít nhất 3 ký tự"

4. **Kiểm Thử Logout**
   - Bấm "Đăng Xuất" hoặc Back button
   - Kết quả: ✅ Quay về LoginActivity

### TODO - Unit Tests

```java
public class LoginActivityTest {
    @Test
    public void testValidateInputEmpty() {
        // Test dữ liệu trống
    }

    @Test
    public void testValidateInputTooShort() {
        // Test dữ liệu quá ngắn
    }

    @Test
    public void testLoginSuccess() {
        // Test đăng nhập thành công
    }

    @Test
    public void testLoginFailed() {
        // Test đăng nhập thất bại
    }
}
```

---

## 🐛 Troubleshooting

### Lỗi Build

**Lỗi:** `Android resource compilation failed`
```
Nguyên nhân: File hình ảnh bị hỏng hoặc format không hỗ trợ
Cách sửa: Kiểm tra file backgrond.png hoặc thay bằng file PNG khác
```

**Lỗi:** `Gradle sync failed`
```
Nguyên nhân: JDK version không khớp
Cách sửa: Kiểm tra JAVA_HOME trỏ tới JDK 11+
```

### Lỗi Runtime

**Lỗi:** `Cannot resolve symbol 'LoginActivity'`
```
Nguyên nhân: File chưa được tạo
Cách sửa: Tạo LoginActivity.java
```

**Lỗi:** `Activity not found`
```
Nguyên nhân: Chưa khai báo trong AndroidManifest.xml
Cách sửa: Thêm <activity android:name=".LoginActivity" />
```

---

## 📞 Hỗ Trợ & Phản Hồi

Nếu gặp vấn đề, hãy:
1. Kiểm tra lại steps trong hướng dẫn
2. Xem phần Troubleshooting
3. Kiểm tra logcat cho thông báo lỗi
4. Tạo issue trên GitHub

---

## 📚 Tài Liệu Liên Quan

- [Android Developers Guide](https://developer.android.com)
- [Material Design 3](https://m3.material.io)
- [Android Security Best Practices](https://developer.android.com/topic/security/best-practices)

---

## 📄 Changelog

### v1.1 (Hiện Tại)
- ✨ Thêm LoginActivity
- ✨ Cải thiện MainActivity (MainCity)
- 🔧 Sửa deprecated API (drawableLeft → drawableStart)
- 📝 Thêm kiểm tra dữ liệu nhập vào
- 📝 Thêm chuỗi văn bản vào strings.xml
- ✅ Thêm INTERNET permission
- 🎨 Cải thiện giao diện

### v1.0 (Original)
- 🎯 Version đầu tiên với màn hình login đơn giản

---

**Cập nhật lần cuối:** 23/02/2026

