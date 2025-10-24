import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import spock.lang.Specification

class BCryptSpec extends Specification {

    def "test BCrypt hash matches devpass"() {
        given:
        def encoder = new BCryptPasswordEncoder()
        def rawPassword = "devpass"
        def hashedPassword = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'

        when:
        def matches = encoder.matches(rawPassword, hashedPassword)

        then:
        println "Raw password: $rawPassword"
        println "Hashed password: $hashedPassword"
        println "Matches: $matches"
        matches == true
    }

    def "generate new BCrypt hash for devpass"() {
        given:
        def encoder = new BCryptPasswordEncoder()
        def rawPassword = "devpass"

        when:
        def newHash = encoder.encode(rawPassword)

        then:
        println "New hash for 'devpass': $newHash"
        encoder.matches(rawPassword, newHash)
    }
}