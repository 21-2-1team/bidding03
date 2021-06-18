
package bidding;

import java.util.Date;

public class NoticeRegistered extends AbstractEvent {

    private Long id;
    private String noticeNo;
    private String title;
    private Date dueDate;
    private Integer price;
    private String demandOrgNm;
    private String bizInfo;
    private String qualifications;
    private String succBidderNm;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNoticeNo() {
        return noticeNo;
    }
    public void setNoticeNo(String noticeNo) {
        this.noticeNo = noticeNo;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Date getDueDate() {
        return dueDate;
    }
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    public Integer getPrice() {
        return price;
    }
    public void setPrice(Integer price) {
        this.price = price;
    }
    public String getDemandOrgNm() {
        return demandOrgNm;
    }
    public void setDemandOrgNm(String demandOrgNm) {
        this.demandOrgNm = demandOrgNm;
    }
    public String getBizInfo() {
        return bizInfo;
    }
    public void setBizInfo(String bizInfo) {
        this.bizInfo = bizInfo;
    }
    public String getQualifications() {
        return qualifications;
    }
    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }
    public String getSuccBidderNm() {
        return succBidderNm;
    }
    public void setSuccBidderNm(String succBidderNm) {
        this.succBidderNm = succBidderNm;
    }

    
    
}

