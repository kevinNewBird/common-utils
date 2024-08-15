package com.common.introspector;

/**
 * description: com.common.introspector
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/3/8
 * version: 1.0
 */
public class Address {

    private String commitId;

    private String buildNum;

    private String province;

    private String city;

    public Address() {
    }

    public Address(String commitId, String buildNum, String province, String city) {
        this.commitId = commitId;
        this.buildNum = buildNum;
        this.province = province;
        this.city = city;
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public String getBuildNum() {
        return buildNum;
    }

    public void setBuildNum(String buildNum) {
        this.buildNum = buildNum;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Address{" +
                "commitId='" + commitId + '\'' +
                ", buildNum='" + buildNum + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
