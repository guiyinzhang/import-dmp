package nuonuo.icm.automation.util;

/**
 * Created by liuya on 2017/7/6.
 */
public interface ImpCommand {
    String FILE_PATH = "E:\\full.dmp";//文件路径
    String LOG_PATH = "E:\\Log\\logfile.log";//日志输出路径
    String USERNAME = "scott";//Oracle用户名
    String PASSWORD = "123456";//Oracle密码
    String SID = "orcl";//Oracle SID
    //imp命令相关参数
    String FULL = " full=y";//导入所有数据库表
    String ROWS = " rows=y";
    String GRANTS=" grants=y";
    String IGNORE=" ignore=y";
    String INDEXES = " indexes=y";
    String LOG = " log=" + LOG_PATH;

    int SUCCESS = 0;// 表示命令执行成功返回值
    String IMP_SUCCESS="1";//导入dmo成功返回值
    String IMP_FAIL="-1";//导入dmp失败返回值
    String CHARSET = "GBK";//输出信息编码格式
    String SUCCESS_MESSAGE = "数据导入成功：";
    String ERROR_MESSAGE = "数据未能按预期导入：";
}
