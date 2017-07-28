package nuonuo.icm.automation.model;

/**
 * Created by YuRui on 2017/6/30.
 */
public class Task {
    private int id;//任务ID
    private String organization;//组织机构
    private String region;//地区
    private String files;//文件名称，以','分割，最多5个
    private String timeFrame;//时间范围
    private int status;//任务状态,0-初始状态(文件上传成功),10-文件解析成功,11-文件解析失败,20-数据匹配成功,21-数据匹配失败,30-生成结果文件成功,31-生成结果文件失败,40-上传文件成功,41-上传文件失败
    private String failMessage;//失败信息

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public String getTimeFrame() {
        return timeFrame;
    }

    public void setTimeFrame(String timeFrame) {
        this.timeFrame = timeFrame;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFailMessage() {
        return failMessage;
    }

    public void setFailMessage(String failMessage) {
        this.failMessage = failMessage;
    }
}
