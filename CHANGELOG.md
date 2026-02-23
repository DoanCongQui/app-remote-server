# CHANGELOG - Control&RemoteServer

## v1.1.0 - 23/02/2026 (Cộng sự & Cải Thiện Toàn Diện)

### ✨ Tính Năng Mới
- **Thêm LoginActivity**
  - Màn hình đăng nhập hoàn chỉnh với logic xác thực
  - Kiểm tra dữ liệu nhập vào (validation)
  - Thông báo lỗi chi tiết

- **Cải Thiện MainActivity (MainCity)**
  - Hiển thị lời chào người dùng
  - Nút Logout
  - Thông tin trạng thái phiên làm việc
  - Ngăn quay lại login bằng Back button

- **Thêm INTERNET Permission**
  - Chuẩn bị cho API calls tương lai

### 🔧 Sửa Lỗi & Cải Thiện
- **Sửa Deprecated API**
  - Thay `android:drawableLeft` → `android:drawableStart`
  - Thay `android:drawableRight` → `android:drawableEnd`
  - Hỗ trợ RTL languages

- **Loại Bỏ File Layout Trùng Lặp**
  - `activity_login.xml` - Giao diện login
  - `activity_main.xml` - Giao diện MainCity (mới)
  - Xóa code trùng lặp

- **Cập Nhật AndroidManifest.xml**
  - LoginActivity là LAUNCHER (vào trước)
  - MainActivity là secondary activity
  - Thêm INTERNET permission

### 📝 Code Quality
- **Chuyển sang Lambda Expression**
  - Button click listener dùng lambda thay anonymous class
  - Code ngắn gọn, dễ đọc hơn

- **Di Chuyển Chuỗi Văn Bản vào strings.xml**
  - Tất cả UI strings từ hard-code sang resources
  - Dễ bảo trì và dịch

- **Thêm Javadoc Comments**
  - Tài liệu cho tất cả methods
  - Giải thích logic rõ ràng

- **Xóa Mã Comment Không Cần**
  - Loại bỏ `EdgeToEdge` commented imports

- **Thêm Accessibility Support**
  - `android:contentDescription` cho EditText

### 📁 File Mới Thêm
```
LoginActivity.java              - Activity đăng nhập
HUONG_DAN_SU_DUNG.md           - Hướng dẫn sử dụng (Tiếng Việt)
TODO.md                         - Danh sách cải thiện (Roadmap)
```

### 📁 File Sửa Đổi
```
MainActivity.java               - Cấu trúc lại thành MainCity
activity_login.xml              - Sửa deprecated drawableLeft
activity_main.xml               - Tạo mới layout MainCity
AndroidManifest.xml             - Thêm LoginActivity, permissions
strings.xml                      - Thêm tất cả chuỗi văn bản
```

### 🔐 Bảo Mật
- ✅ Thêm INTERNET permission (chuẩn bị backend)
- ⚠️ Hard-code credentials vẫn tồn tại (TODO: thay thế bằng backend)
- ⚠️ ProGuard/R8 chưa bật (TODO: bật cho release)

### 📊 Metrics
| Metric | Trước | Sau | Ghi Chú |
|--------|-------|-----|---------|
| Java Files | 1 | 2 | +LoginActivity.java |
| Layout Files | 1 (trùng) | 2 (khác) | Loại bỏ trùng lặp |
| Chuỗi Văn Bản | Hard-code | strings.xml | Tập trung quản lý |
| Deprecated APIs | 4 | 0 | Toàn bộ sửa |
| Permissions | 0 | 1 | +INTERNET |

### 🚀 Build & Test
- ✅ Build Debug: THÀNH CÔNG
- ✅ Build Release: THÀNH CÔNG (sẽ cảnh báo minify)
- ✅ APK Size: ~6.5 MB
- ✅ Min SDK: 26
- ✅ Target SDK: 36

### 📖 Tài Liệu
- ✅ Thêm `HUONG_DAN_SU_DUNG.md` (Hướng dẫn tiếng Việt)
- ✅ Thêm `TODO.md` (Roadmap phát triển)
- ✅ Thêm Javadoc trong code
- ✅ Thêm `CHANGELOG.md` (File này)

### 🎯 Tiếp Theo (v1.2.0)
- [ ] Triển khai Backend Xác Thực
- [ ] Implement View Binding
- [ ] Thêm Unit Tests
- [ ] Bật ProGuard/R8 Minification
- [ ] Thêm Repository Pattern

---

## v1.0.0 - (Original)

### Tính Năng Đầu Tiên
- Màn hình login cơ bản
- Kiểm tra hard-code credentials (admin/1234)
- Toast messages cho success/failure

### Vấn Đề Ban Đầu
- ❌ File Login.java không tồn tại nhưng được reference
- ❌ Layout trùng lặp (activity_login.xml & activity_main.xml giống nhau)
- ❌ Sử dụng deprecated API (drawableLeft)
- ❌ Chuỗi văn bản hard-code
- ❌ Thiếu INTERNET permission
- ❌ Không có kiểm tra dữ liệu nhập
- ❌ Material library include hai lần

---

## Hướng Dẫn Cập Nhật

### Nâng Cấp từ v1.0 lên v1.1
```bash
# 1. Pull changes
git pull origin main

# 2. Clean & rebuild
./gradlew clean

# 3. Build lại
./gradlew assembleDebug

# 4. Install lên device
./gradlew installDebug

# 5. Chạy ứng dụng
# Hoặc từ Android Studio: Run > Run 'app'
```

### Breaking Changes
- ⚠️ Cấu trúc file Activity thay đổi
  - `MainActivity` giờ chỉ là MainCity (hiển thị sau login)
  - Phải qua `LoginActivity` trước
  - Không thể bỏ qua màn hình login nữa

### Database/Storage Changes
- 📋 Không có thay đổi (app vẫn không dùng database)
- 📋 SharedPreferences: Không thay đổi (TODO: implement)

### API Changes
- 📋 Không có API (vẫn hard-code - TODO: backend)

---

## Thống Kê Thay Đổi

```
 Files Changed:      6
 Files Added:        3
 Total Lines Added:  ~800
 Total Lines Removed: ~200
 
 Languages:
 - Java:              ~300 lines
 - XML:               ~400 lines  
 - Markdown:          ~150 lines
```

### Đóng Góp Chính
```
LoginActivity (NEW):         116 lines - Xác thực người dùng
MainActivity (REFACTORED):    50 lines - MainCity screen
activity_main.xml (NEW):      80 lines - Layout MainCity
activity_login.xml (UPDATED): 80 lines - Sửa deprecated APIs
AndroidManifest.xml (UPD):    10 lines - Cấu hình activity
strings.xml (EXPANDED):       30 lines - Chuỗi văn bản
```

---

## 🔗 Liên Kết Liên Quan

### GitHub
- [Repository](https://github.com/yourusername/ControlRemoteServer)
- [Issues](https://github.com/yourusername/ControlRemoteServer/issues)
- [Pull Requests](https://github.com/yourusername/ControlRemoteServer/pulls)

### Tài Liệu
- [Hướng Dẫn Sử Dụng](./HUONG_DAN_SU_DUNG.md)
- [TODO & Roadmap](./TODO.md)
- [README](./README.md)

---

## 📞 Support

Nếu gặp vấn đề sau khi update:
1. Chạy `./gradlew clean` rồi build lại
2. Xóa app cũ trước khi cài phiên bản mới
3. Clear Android Studio cache: `File > Invalidate Caches`
4. Kiểm tra logcat để tìm lỗi chi tiết

---

**Phiên bản hiện tại:** v1.1.0  
**Cập nhật lần cuối:** 23/02/2026  
**Người maintain:** Development Team

