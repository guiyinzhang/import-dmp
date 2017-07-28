package nuonuo.icm.automation.util;

import org.junit.Test;

/**
 * Created by liuya on 2017/7/4.
 */
public class DmpUtilTest {
    @Test
    public void impOracleDmp() throws Exception {
        String anaResult[]=DmpUtil.impOracleDmp("E:\\FULL.dmp");
        for (String s : anaResult) {
            System.out.println("分析结果： "+s);
        }
    }

    @Test
    public void analyzeDataSize() throws Exception {
        String filePath = "E:\\Log\\impLogfile.log";
        String str = DmpUtil.readLog(filePath);
        String result[]=DmpUtil.analyzeData(str);
        for(String s:result){
            System.out.println("--------"+s);
        }
    }

    @Test
    public void printString(){
        for(int i=0;i<10000000;i++){
            System.out.println("print i="+i);
        }
    }


}