import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


def encoder = new BCryptPasswordEncoder()

print(encoder.encode("devpass"))


