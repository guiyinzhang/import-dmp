package nuonuo.icm.automation.util;

import java.io.*;
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
        String result[] = new String[3];//执行结果：导入dmp返回值（成功0|失败-1），导入的表格，数据量总和或错误信息；
        String FILE = " file=" + file;
        //导入数据库cmd命令
        String cmd = "imp " + USERNAME + "/" + PASSWORD + "@" + SID
                + FILE + FULL + ROWS + IGNORE + GRANTS + INDEXES;
        String outputInfo = "";
        String tables = "";//表名
        int size = 0;//导入数据大小
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

            //从流中获取返回的表明和数据量信息
            if (inputThread.getAnalyzeResult().getName() != null) {
                tables += inputThread.getAnalyzeResult().getName();
                size += inputThread.getAnalyzeResult().getSize();
            }
            if (errorThread.getAnalyzeResult().getName() != null) {
                tables += errorThread.getAnalyzeResult().getName();
                size += errorThread.getAnalyzeResult().getSize();
            }

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
                result[0] = IMP_SUCCESS;
                result[1] = tables;
                result[2] = size + "";
            } else {
                result[0] = IMP_FAIL;
                result[1] = tables;
                result[2] = outputInfo;
            }
            System.out.println("-------------------------------------------------------------------------");
//            for (String s : result) {
//                System.out.println(s);
//            }
            System.out.println("tables are : " + tables + " , sum : " + size);
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
        //截取表明
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

    /**
     * 读取Log日志文件
     *
     * @param file
     * @return
     */
    public static String readLog(String file) {
        File logFile = new File(file);
        FileInputStream in = null;

        if (!logFile.exists()) {
            System.out.println("file is not exist! ");
            return "";
        }

        String logString = null;
        try {
            in = new FileInputStream(logFile);
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            logString = new String(buffer, "GB2312");
//            System.out.println(logString);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return logString;
    }
}

