package nuonuo.icm.automation.util;

import nuonuo.icm.automation.model.ExcelRecord;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by liuyang on 2017/6/27.
 */
public class ExcelUtil {

    public static void exportExcel(Set<Object> objectSet, String filename) {
        String companyName = "销方企业名称";
        String identifyNumber = "销方纳税人识别号";
        String exceptionNumber = "异常商品数量";
        String excelTitle[] = new String[]{companyName, identifyNumber, exceptionNumber};
        //建立工作簿
        Workbook wb = new XSSFWorkbook();
        FileOutputStream out=null;

        try {
            //创建工作表
            Sheet sheet = wb.createSheet("Sheet0");
            int rowIndex = 0;
            Row row = sheet.createRow(rowIndex++);
            for (int i = 0; i < excelTitle.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(excelTitle[i]);
                sheet.setColumnWidth(i,excelTitle[i].getBytes().length*256);
            }

            Iterator<Object> iterator = objectSet.iterator();
            while (iterator.hasNext()) {
                ExcelRecord record = (ExcelRecord) iterator.next();
                Row xssfRow = sheet.createRow(rowIndex++);
                Cell xssfCell = xssfRow.createCell(0);
                xssfCell.setCellValue(record.getCompanyName());
                xssfCell = xssfRow.createCell(1);
                xssfCell.setCellValue(record.getIdentifyNumber());
                xssfCell = xssfRow.createCell(2);
                xssfCell.setCellValue(record.getGoodsNumber().size());
            }

            //解析文件路径，并创建文件夹
            String filePath = filename.substring(0, filename.lastIndexOf("\\")+1);
            File fp = new File(filePath);
            if(!fp.exists()){
                fp.mkdirs();
            }
            if(!filename.endsWith(".xls")&&!filename.endsWith(".xlsx")){
                filename += ".xlsx";
            }
            out = new FileOutputStream(filename);
            wb.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(out!=null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
