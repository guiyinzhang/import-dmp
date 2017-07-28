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
    private String outputInfo;//日志信息
    private TableSize analyzeResult;//解析结果

    public String getOutputInfo() {
        return outputInfo;
    }

    public TableSize getAnalyzeResult() {
        return analyzeResult;
    }

    public OutputThread(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        StringBuffer lineBuffer = new StringBuffer();
        StringBuffer tableBuffer = new StringBuffer();
        InputStreamReader isr = null;
        BufferedReader br = null;
        int sum = 0;//总数据量

        try {
            isr = new InputStreamReader(inputStream, CHARSET);
            br = new BufferedReader(isr);
            String line = "";

            while ((line = br.readLine()) != null) {
                lineBuffer.append(line + "\n");
                System.out.println(line);
                TableSize table = analyzeByLine(line);//按行解析
                if (!table.getName().equals("")) {
                    tableBuffer.append(table.getName() + ",");
                    sum += table.getSize();
                }
            }

            if (analyzeResult == null) {
                analyzeResult = new TableSize();
            }
            if (tableBuffer.length() > 0) {
                analyzeResult.setName(tableBuffer.substring(0, tableBuffer.length() - 1));
                analyzeResult.setSize(sum);
            }
            outputInfo = lineBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (isr != null) {
                    isr.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 分析单行mp命令输出的信息
     *
     * @param str
     */
    public static TableSize analyzeByLine(String str) {
        TableSize oracleTable = new TableSize();
        char split = ',';
        //从输出信息中截取与表和数据的相关信息
        String regex = "\"[A-Za-z_0-9]+\"\\s*.*\\s+\\d+";
        String result = DmpUtil.regexMatch(str, regex, split);
//        System.out.println("results: "+result);
        //截取表名
        String regexTableName = "\"[A-Za-z_0-9]+\"";
        String tableName = DmpUtil.regexMatch(result, regexTableName, split);
        oracleTable.setName(tableName);
//        System.out.println("tables: "+tables);
        //截取导入的数据量
        String regexTableCount = "\\s+\\d+";
        String tableSize = DmpUtil.regexMatch(result, regexTableCount, split);
        oracleTable.setSize(0);
        if (!tableSize.trim().equals("")) {
            int size = Integer.parseInt(tableSize.trim());
            oracleTable.setSize(size);
        }
        return oracleTable;
    }
}

