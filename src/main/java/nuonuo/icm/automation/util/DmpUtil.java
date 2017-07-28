package nuonuo.icm.automation.util;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static nuonuo.icm.automation.util.ImpCommand.*;

/**
 * Created by liuya on 2017/7/5.
 */
public class DmpUtil {
    /**
     * 导入数据库文件
     *
     * @param file
     */
    public static String[] impOracleDmp(String file) {
        String FILE = " file=" + file;
        //导入数据库cmd命令
        String cmd = "imp " + USERNAME + "/" + PASSWORD + "@" + SID
                + FILE  + LOG+FULL + ROWS
                + IGNORE + GRANTS + INDEXES;
        String result[] = new String[3];//执行结果：导入dmp返回值（成功0|失败-1），导入的表格，数据量总和或错误信息；
        String outputInfo = "";
        String analyzeResult[] = new String[2];
        Process process = null;

        int exitCode = 0;
        try {
            // 执行CMD输入导入命令
            process = Runtime.getRuntime().exec(cmd);
            //获取Process标准输入流
            OutputThread inputThread = new OutputThread(process.getInputStream());
            //获取Process标准错误流
            OutputThread errorThread = new OutputThread(process.getErrorStream());
            //启动线程
            inputThread.start();
            errorThread.start();
            // 等待线程结束
            inputThread.join();
            errorThread.join();
            //阻塞当前线程，等待命令执行完毕
            exitCode = process.waitFor();
            outputInfo += inputThread.getOutputInfo();
            outputInfo += errorThread.getOutputInfo();
            analyzeResult = analyzeData(outputInfo);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //关闭相关流
            try {
                if (process != null) {
                    process.getInputStream().close();
                    process.getErrorStream().close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (exitCode == SUCCESS) {
//                System.out.println(SUCCESS_MESSAGE);
                result[0] = IMP_SUCCESS;//导入成功返回值
                result[1] = analyzeResult[0];//表名
                result[2] = analyzeResult[1];//数据总量
            } else {
//                System.err.println(ERROR_MESSAGE);
                result[0] = IMP_FAIL;//导入失败返回值
                result[1] = analyzeResult[0];//表名
                result[2] = outputInfo;//错误信息
            }
        }
        return result;
    }

    /**
     * 分析imp命令输出的信息
     *
     * @param str
     */
    public static String[] analyzeData(String str) {
        String analyzeResult[] = new String[2];
        char split = ',';
        //从输出信息中截取与表和数据的相关信息
        String regex = "\"[A-Za-z_0-9]+\"\\s*.*\\s+\\d+";
        String result = DmpUtil.regexMatch(str, regex, split);
//        System.out.println("results: "+result);
        //截取表名
        String regexTableName = "\"[A-Za-z_0-9]+\"";
        String tables = DmpUtil.regexMatch(result, regexTableName, split);
//        System.out.println("tables: "+tables);
        //截取导入的数据量
        String regexTableCount = "\\s+\\d+";
        String dataSize = DmpUtil.regexMatch(result, regexTableCount, split);
//        System.out.println(dataSize);
        String[] sizes = dataSize.split(",");
        int sum = 0;
        for (String s : sizes) {
            if (!s.trim().equals("")) {
                sum += Integer.parseInt(s.trim());
            }
        }
//        System.out.println("sum=" + sum);
        analyzeResult[0] = tables;
        analyzeResult[1] = sum + "";
        return analyzeResult;
    }

    /**
     * 正则匹配字符串
     *
     * @param inputStr 输入字符串
     * @param regexStr 正则规则
     * @param split    拼装分割符
     * @return
     */
    public static String regexMatch(String inputStr, String regexStr, char split) {
        String result = "";
        StringBuffer buffer = new StringBuffer();
        Pattern pattern = Pattern.compile(regexStr);
        Matcher matcher = pattern.matcher(inputStr);
        while (matcher.find()) {
            buffer.append(matcher.group() + split);
        }
        if (buffer.length() > 0) {
            result = buffer.substring(0, buffer.length() - 1);
//            System.out.println(result);
        }
        return result;
    }

}

