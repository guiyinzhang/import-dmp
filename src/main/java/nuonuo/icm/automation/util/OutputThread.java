package nuonuo.icm.automation.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static nuonuo.icm.automation.util.ImpCommand.CHARSET;

/**
 * Created by liuyang on 2017/7/5.
 */
public class OutputThread extends Thread {

    private InputStream inputStream;
    private String outputInfo;

    public String getOutputInfo() {
        return outputInfo;
    }

    public OutputThread(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        StringBuffer buffer = new StringBuffer();
        InputStreamReader inputStreamReader=null;
        BufferedReader bufferedReader=null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, CHARSET);
            bufferedReader = new BufferedReader(inputStreamReader);
            String line="";
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line + "\n");
                System.out.println(line);
            }
            outputInfo = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(inputStreamReader!=null){
                    inputStreamReader.close();
                }
                if(bufferedReader!=null){
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
//            System.out.println(outputInfo.toString());
        }

    }
}