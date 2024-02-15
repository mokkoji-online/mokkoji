package online.mokkoji;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import java.io.File;

@EnableCaching
@SpringBootApplication
public class MokkojiApplication {
    static {
        String libPath = System.getProperty("user.dir") + File.separator + "libs" + File.separator + "opencv_java490.dll";
        System.load(libPath);
    }
    public static void main(String[] args) {
        SpringApplication.run(MokkojiApplication.class, args);
    }
}
