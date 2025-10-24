import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptTest {
	public static void main(String[] args) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String rawPassword = "devpass";
		String hashedPassword = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";
		
		System.out.println("Raw password: " + rawPassword);
		System.out.println("Hashed password: " + hashedPassword);
		System.out.println("Matches: " + encoder.matches(rawPassword, hashedPassword));
		
		// Generate a new hash
		String newHash = encoder.encode(rawPassword);
		System.out.println("\nNew hash for 'devpass': " + newHash);
		System.out.println("New hash matches: " + encoder.matches(rawPassword, newHash));
	}
}
