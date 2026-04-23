import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class FakeURLMaker {
    static String  URL ="jdbc:mysql://localhost:3307/_";
    static int count = 1_000_000;//1M
    static String filePath = "/Users/funzhou/Documents/javacode/TransferURL/src/main/resources/LongURL.txt";
    static  int status =1;//defaut0  after insert turn it =1
    public FakeURLMaker() {
        //字符串生成拼接
        //record start time
        long startTime = System.currentTimeMillis();
        if (status == 0) {
            {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
                    for (int i = 1; i <= count; i++) {
                        String randomStr = UUID.randomUUID().toString().replace("-", "");
                        String result = URL + randomStr;
                        bw.write(result);
                        bw.newLine();
                        if (i % 100_000 == 0) {
                            System.out.println("Progress: " + i + " lines written...");
                        }
                    }
                    status = 1;

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }else   {
            System.out.println("Data already generated. Skipping file writing.");
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Done! Total time: " + (endTime - startTime) + "ms");
    }
}
