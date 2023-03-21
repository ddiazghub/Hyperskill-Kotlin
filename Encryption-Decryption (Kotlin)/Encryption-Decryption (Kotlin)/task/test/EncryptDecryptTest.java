import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.SimpleTestCase;

import java.util.List;

public class EncryptDecryptTest extends StageTest {

    @Override
    public List<SimpleTestCase> generate() {
        return List.of(
            new SimpleTestCase(
                "enc\n" +
                    "Welcome to hyperskill!\n" +
                    "5",
                "\\jqhtrj%yt%m~ujwxpnqq&"),
            new SimpleTestCase(
                "enc\n" +
                    "Hello\n" +
                    "0",
                "Hello"),
            new SimpleTestCase(
                "enc\n" +
                    "012345678\n" +
                    "1",
                "123456789"),
            new SimpleTestCase(
                "dec\n" +
                    "\\jqhtrj%yt%m~ujwxpnqq&\n" +
                    "5",
                "Welcome to hyperskill!"),
            new SimpleTestCase(
                "dec\n" +
                    "Hello\n" +
                    "0",
                "Hello"),
            new SimpleTestCase(
                "dec\n" +
                    "222233334444\n" +
                    "1",
                "111122223333")
        );
    }
}
