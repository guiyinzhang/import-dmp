package nuonuo.icm.automation.util;

import org.junit.Test;

/**
 * Created by liuya on 2017/7/7.
 */
public class DmpUtilTest {
    @Test
    public void impOracleDmp() throws Exception {
        String file = "E:\\FULL.dmp";
        String analyzeResult[] = DmpUtil.impOracleDmp(file);
        System.out.println("------------------------------------------------------------------------------------------");
        for (String s : analyzeResult) {
            System.out.println("分析结果："+s);
        }
    }

}