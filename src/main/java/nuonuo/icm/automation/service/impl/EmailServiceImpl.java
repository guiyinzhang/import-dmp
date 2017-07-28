package nuonuo.icm.automation.service.impl;

import nuonuo.icm.automation.model.AnalysisResult;
import nuonuo.icm.automation.service.EmailService;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;

/**
 * Created by YuRui on 2017/6/30.
 */
public class EmailServiceImpl implements EmailService {
    public static String emailAccount;//发送邮件的账户
    public static String emailPassword;//发送邮件的密码
    public static String emailSMTPHost;//发送邮件的SMTP服务器地址
    public static String[] receiveMailAccountList;//接收邮件的地址

    /*
    * 获取发送邮件端的用户名和密码以及SMTP服务器地址
    * */
    static {
        Properties p = new Properties();
        InputStream ins = EmailServiceImpl.class.getClassLoader().getResourceAsStream("authorityAccount.properties");
        try {
            p.load(ins);
        } catch (IOException e) {
            e.printStackTrace();
        }
        emailAccount = p.getProperty("myEmailAccount");
        emailPassword = p.getProperty("myEmailPassword");
        emailSMTPHost = p.getProperty("myEmailSMTPHost");
        receiveMailAccountList = p.getProperty("receiveMailAccount").split(",");
    }

    /*
    * 执行相应的sql语句
    * */
    public static void executeSQL(String[] sql) throws Exception {
        InputStream ins = EmailServiceImpl.class.getClassLoader().getResourceAsStream( "jdbcConfig.properties");
        Properties p = new Properties();

        System.out.println("p: " + p);

        try {
            p.load(ins);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = p.getProperty("url");
        String username = p.getProperty("username");
        String password = p.getProperty("password");

        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(url, username, password);
        PreparedStatement ptmt= conn.prepareStatement(sql[0]);
        ptmt.executeUpdate();
        ptmt= conn.prepareStatement(sql[1]);
        ptmt.executeUpdate();

        ptmt.close();
    }

    /*
    * 发送邮件给用户
    * */
    @Override
    public void sendEmail(AnalysisResult analysisResult) throws Exception {
        Properties props = new Properties();                    // 参数配置
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", emailSMTPHost);   // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);    //根据参数配置，创建会话对象
        session.setDebug(true);

        MimeMessage message = createMimeMessage(session, emailAccount, receiveMailAccountList, analysisResult);
        Transport transport = session.getTransport();
        transport.connect(emailAccount, emailPassword);
        transport.sendMessage(message, message.getAllRecipients());

        transport.close();
    }

    /*
    * 创建邮件
    * */
    @Override
    public MimeMessage createMimeMessage(Session session, String sendMail, String[] receiveMailAccountList, AnalysisResult analysisResult) throws Exception {
        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(sendMail, "aisino", "UTF-8"));//设置邮箱发件人

        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMailAccountList[0], "AISINO_USER", "UTF-8"));//设置邮箱收件人
        for (int i = 1; i < receiveMailAccountList.length; i++) {
            message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMailAccountList[i], "AISINO_USER", "UTF-8"));
        }

        message.setSubject("浙江爱信诺极速开票文件解析结果", "UTF-8");//设置邮件主题
        String content = getContent(analysisResult);//获取邮件正文
        message.setContent(content, "text/html;charset=UTF-8");
        message.setSentDate(new Date());//设置发件时间

        message.saveChanges();
        return message;
    }

    /*
    * 生成邮件正文
    * */
    public String getContent(AnalysisResult analysisResult) throws Exception{
        StringBuffer content = new StringBuffer();
        content.append("To All:<br>");
        /*
        * 将java.util.Date转换成java.time.LocalDateTime
        * */
        Date date = analysisResult.getStart_time();
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime startLocalDateTime = LocalDateTime.ofInstant(instant, zone);

        content.append(startLocalDateTime.getYear() + "年" + startLocalDateTime.getMonthValue() + "月" + startLocalDateTime.getDayOfMonth()
                + "日" + startLocalDateTime.getHour() + "时" + startLocalDateTime.getMinute() + "分，");

        content.append("收到" + analysisResult.getTask().getRegion() + "地区" + analysisResult.getTask().getOrganization()
                + "单位的文件,文件包含：");
        String[] fileNames = analysisResult.getTask().getFiles().split(",");
        for (String fileName : fileNames) {
            content.append("[" + fileName + "]");
        }
        content.append("。");

        int flag = analysisResult.getStep_status();
        String executeSql = null;
        if (flag == 0) {//文件解析成功
            content.append("文件解析成功，生成的数据量有" + analysisResult.getData_count() + "条<br>");
            executeSql = "update intell_code_match_task set status = 10 where id = " + analysisResult.getTask().getId();
        }else {//文件解析失败
            content.append("文件解析失败，失败的原因是：" + analysisResult.getTask().getFailMessage() + "。<br>");
            executeSql = "update intell_code_match_task set status = 11, failMessage = '" + analysisResult.getTask().getFailMessage() +
                    "' where id = " + analysisResult.getTask().getId();
        }

        String insertSql = "insert intell_code_match_step(task_id, step, step_result, start_time, end_time, step_status) " +
                "values(" + analysisResult.getTask().getId() + "," + analysisResult.getStep() + ",'" +
                analysisResult.getStep_result() + "','" + (analysisResult.getStart_time()).toLocaleString() + "','" +
                (new Date()).toLocaleString() + "'," + analysisResult.getStep_status() + ")";

        executeSQL(new String[]{executeSql, insertSql});

        content.append("<div align = 'right'>浙江爱信诺极速开票项目组<br>");
        LocalDateTime nowDateTime = LocalDateTime.now();
        content.append(nowDateTime.getYear() + "年" + nowDateTime.getMonthValue() + "月" + nowDateTime.getDayOfMonth()
                        + "日" + nowDateTime.getHour() + "时" + nowDateTime.getMinute() + "分</div>");
        
        return content.toString();
    }
}
