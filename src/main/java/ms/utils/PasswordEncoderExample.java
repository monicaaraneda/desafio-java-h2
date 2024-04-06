package ms.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderExample {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword1 = encoder.encode("hunter2");
        String encodedPassword2 = encoder.encode("pink123");

        System.out.println("Encoded password for 'hunter2': " + encodedPassword1);
        System.out.println("Encoded password for 'pink123': " + encodedPassword2);
    }
}
