package nuonuo.icm.automation.model;

import java.util.Set;

/**
 * Created by liuyang on 2017/6/27.
 */
public class ExcelRecord {
    private String companyName;
    private String identifyNumber;
    private Set<String> goodsNumber;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getIdentifyNumber() {
        return identifyNumber;
    }

    public void setIdentifyNumber(String identifyNumber) {
        this.identifyNumber = identifyNumber;
    }

    public Set<String> getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(Set<String> goodsNumber) {
        this.goodsNumber = goodsNumber;
    }
}
