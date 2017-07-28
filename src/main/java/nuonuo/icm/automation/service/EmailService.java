package nuonuo.icm.automation.service;

import nuonuo.icm.automation.model.AnalysisResult;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.List;

/**
 * Created by YuRui on 2017/6/30.
 */
public interface EmailService {
    /*
    * 发送邮件给用户
    * */
    public void sendEmail(AnalysisResult analysisResult) throws Exception;
    /*
    * 创建邮件
    * */
    public MimeMessage createMimeMessage(Session session, String sendMail, String[] receiveMail,
                                                AnalysisResult analysisResult) throws Exception;
    /*
   * 生成邮件正文
   * */
    public String getContent(AnalysisResult analysisResult) throws Exception;
}
