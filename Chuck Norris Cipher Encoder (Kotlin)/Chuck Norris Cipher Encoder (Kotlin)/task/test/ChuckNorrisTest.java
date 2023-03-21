import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.TestedProgram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChuckNorrisTest extends StageTest {

  @DynamicTest()
  CheckResult test() {
    TestedProgram pr = new TestedProgram();
    String output = pr.start().strip();
    List<String> list = new ArrayList<>(Arrays.asList(output.split("\n")));
    list.removeAll(Arrays.asList(""));

    if(list.size()!=1 || !list.get(0).contains("Please input operation (encode/decode/exit):")){
      return CheckResult.wrong("When the program just started, output should contain exactly 1 non-empty line, " +
              "containing \"Please input operation (encode/decode/exit):\" as it shown in the example, followed by an input");
    }

    output = pr.execute("encode").strip().toLowerCase();
    list = new ArrayList<>(Arrays.asList(output.split("\n")));
    list.removeAll(Arrays.asList(""));
    if(list.size()!=1 || !list.get(0).contains("input string")){
      return CheckResult.wrong("When the user has chosen \"encode\" as an operation, there should be printed exactly 1 " +
              "non-empty line, containing \"input string\" substring, followed by an input");
    }
    output = pr.execute("H W!").strip().toLowerCase();
    list = new ArrayList<>(Arrays.asList(output.split("\n")));
    list.removeAll(Arrays.asList(""));
    if(list.size()!=3 || !list.get(0).contains("encoded string") ||
            !list.get(2).contains("please input operation (encode/decode/exit):")){
      return CheckResult.wrong("When the user provided a string for encoding, there should be printed exactly 3 " +
              "non-empty lines, the first one with \"encoded string\" substring, the second one, with an encoded string," +
              "and the last one is \"Please input operation (encode/decode/exit):\" as an operation is finished and" +
              "the program is looped");
    }
    if(!list.get(1).contains("0 0 00 00 0 0 00 0000 0 0 00 00000 0 0 00 0 0 0 00 0 0 000 00 0 0 0 00 0000 0 0")){
      return CheckResult.wrong("When the user provided a string for encoding, the second non-empty " +
              "line of an output should contain an encoded string. Make sure, that encryption is correct and the " +
              "encoding of a single character sequence is not separated.");
    }

    output = pr.execute("decode").strip().toLowerCase();
    list = new ArrayList<>(Arrays.asList(output.split("\n")));
    list.removeAll(Arrays.asList(""));
    if(list.size()!=1 || !list.get(0).contains("encoded string")){
      return CheckResult.wrong("When the user has chosen \"decode\" as an operation, there should be printed exactly 1 " +
              "non-empty line, containing \"encoded string\" substring, followed by an input");
    }
    output = pr.execute("0 0 00 00 0 0 00 0000 0 0 00 00000 0 0 00 0 0 0 00 0 0 000 00 0 0 0 00 0000 0 0").strip().toLowerCase();
    list = new ArrayList<>(Arrays.asList(output.split("\n")));
    list.removeAll(Arrays.asList(""));
    if(list.size()!=3 || !list.get(0).contains("decoded string") ||
            !list.get(2).contains("please input operation (encode/decode/exit):")){
      return CheckResult.wrong("When the user provided a string for decoding, there should be printed exactly 3 " +
              "non-empty lines, the first one with \"decoded string\" substring, the second one, with a decoded string," +
              "and the last one is \"Please input operation (encode/decode/exit):\" as an operation is finished and" +
              "the program is looped");
    }
    if(!list.get(1).contains("h w!")){
      return CheckResult.wrong("When the user provided a string for decoding, the second non-empty " +
              "line of an output should contain a decoded string. Make sure, that decryption is correct.");
    }

    output = pr.execute("Hello world!").strip();
    list = new ArrayList<>(Arrays.asList(output.split("\n")));
    list.removeAll(Arrays.asList(""));
    if(list.size()!=2 || !list.get(0).contains("There is no 'Hello world!' operation") ||
            !list.get(1).contains("Please input operation (encode/decode/exit):")){
      return CheckResult.wrong("When the user had chosen an operation, that is not \"decode\", \"encode\" or \"exit\"," +
              " there should be printed exactly 2 non-empty lines, the one that contains " +
              "\"There is no '<user's input>' operation\" substring, and the \"Please input operation" +
              " (encode/decode/exit):\" one as the program is looped");
    }
    for(String s : new String[]{"hello world!",
            "0 0 00 00 0 0 00 0000 0 0 00 0 0 00 0 0 0 00 0 0 000 00 0 0 0 00 0000 0 0",
            "0 0 00 00 0 0 00 000 0 0 00 0000 0 0 00 0 0 0 00 0 0 000 00 0 0 0 00 0000 0 0",
            "0 0 00 00 0 0 000 000"}) {
      pr.execute("decode");
      output = pr.execute(s).strip().toLowerCase();
      list = new ArrayList<>(Arrays.asList(output.split("\n")));
      list.removeAll(Arrays.asList(""));
      if (list.size() != 2 || !list.get(0).contains("not valid") ||
              !list.get(1).contains("please input operation (encode/decode/exit):")) {
        return CheckResult.wrong("When the user provided not valid encoded string, there should be printed exactly 2 " +
                "non-empty lines, the one that contains \"not valid\" substring, and the \"Please input " +
                "operation (encode/decode/exit):\" one as an operation is finished and the program is looped");
      }
    }
    output = pr.execute("exit").strip().toLowerCase();
    if(!output.contains("bye") || !pr.isFinished()){
      return CheckResult.wrong("When the user has chosen \"exit\" as an operation, there should be \"Bye\" substring" +
              " in output and the program should finish it's execution");
    }
    return CheckResult.correct();
  }
}