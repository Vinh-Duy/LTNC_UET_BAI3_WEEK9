

public class MathUtils {
    
}
// Loại bỏ hoàn toàn JUnit 4: Đã xóa đoạn <dependency> của junit 4.10 trong mã cũ. Nếu không xóa, 
// project sẽ bị lẫn lộn giữa các thư viện test cũ và mới.

// Khắc phục lỗi phiên bản Java (Xung đột với Hibernate 6): Mã cũ sử dụng thẻ <maven.compiler.source>1.8</maven.compiler.source>. 
// Vì Hibernate 6.2.0.Final được build trên nền tảng Java 11+, nếu giữ nguyên 1.8, quá trình biên dịch (build) sẽ 
// văng lỗi UnsupportedClassVersionError. Mình đã chuyển 1.8 thành 17 ở cả thẻ <properties> và phần cấu hình của maven-compiler-plugin. 
// (Lưu ý: cần chắc chắn IDE hoặc môi trường máy tính của đã cài đặt JDK 11 hoặc 17).

// Khắc phục lỗi Maven không chạy Test JUnit 5: Phiên bản Maven mặc định hoặc các bản plugin cũ không tự hiểu được JUnit 5. 
// Do đó, đã thêm maven-surefire-plugin (phiên bản 3.1.2) vào thẻ <build>. Nếu không có plugin này, 
// khi chạy lệnh mvn test, Maven sẽ bỏ qua toàn bộ các bài test.