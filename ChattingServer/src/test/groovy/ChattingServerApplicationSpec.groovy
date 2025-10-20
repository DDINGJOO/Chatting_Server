import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification;
import com.teambind.chattingserver.ChattingServerApplication

@SpringBootTest(classes = ChattingServerApplication)
class ChattingServerApplicationSpec extends Specification{

	@Test
	void contextLoads() {
        expect:
        true
	}

}
