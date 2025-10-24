import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
	public static void main(String[] args) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String rawPassword = "devpass";
		String encodedPassword = encoder.encode(rawPassword);
		System.out.println("BCrypt hash for 'devpass': " + encodedPassword);
	}
}
