package nuonuo.icm.automation.util;

import nuonuo.icm.automation.model.ExcelRecord;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by liuya on 2017/7/5.
 */
public class ExcelUtilTest {
    @Test
    public void exportExcel() throws Exception {
        String fileName = "E:\\Log\\log";
        Set<Object> recordSet = new HashSet<>();
        for (int i = 0; i < 80000; i++) {
            ExcelRecord record = new ExcelRecord();
            record.setCompanyName("Aisino Company");
            record.setIdentifyNumber("02017" + i);
            Set<String> set = new HashSet();
            set.add("2017" + i);
            record.setGoodsNumber(set);
            recordSet.add(record);
        }
        ExcelUtil.exportExcel(recordSet, fileName);
    }
}