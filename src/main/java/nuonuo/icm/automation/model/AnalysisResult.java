package nuonuo.icm.automation.model;

import java.util.Date;

/**
 * Created by YuRui on 2017/6/30.
 */
public class AnalysisResult {
    private Task task;
    private int step;//步骤：1-解析入库,2-数据匹配,3-生成结果文件,4-上传FTP
    private String step_result;//步骤结果：表名集或文件名集
    private Date start_time;//开始时间
    private Date end_time;//结束时间
    private int step_status;//状态：0-失败，1-成功
    private int data_count;//生成的数据量

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getStep_result() {
        return step_result;
    }

    public void setStep_result(String step_result) {
        this.step_result = step_result;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public int getStep_status() {
        return step_status;
    }

    public void setStep_status(int step_status) {
        this.step_status = step_status;
    }

    public int getData_count() {
        return data_count;
    }

    public void setData_count(int data_count) {
        this.data_count = data_count;
    }
}
