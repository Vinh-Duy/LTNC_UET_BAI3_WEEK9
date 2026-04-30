package bank_system;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class OSPathTest {

    @Test
    public void testFilePath() {
        String folder = "logs";
        String file = "transaction.log";
        
        // Cố tình fix cứng bằng dấu gạch chéo ngược của Windows (\)
        String hardcodedPath = folder + "\\" + file; 

        // Paths.get() sẽ lấy đường dẫn chuẩn theo OS đang chạy
        // Trên Windows nó là "logs\transaction.log"
        // Trên Ubuntu/Mac nó là "logs/transaction.log"
        String actualOsPath = Paths.get(folder, file).toString();

        // So sánh: Pass trên Windows, TẠCH trên Ubuntu và Mac!
        assertEquals(hardcodedPath, actualOsPath, "Lỗi đường dẫn hệ điều hành!");
    }
}