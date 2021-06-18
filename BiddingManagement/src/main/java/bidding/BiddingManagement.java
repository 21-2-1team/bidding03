package bidding;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.Date;

@Entity
@Table(name="BiddingManagement_table")
public class BiddingManagement {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String noticeNo;
    private String title;
    private Date dueDate;
    private Integer price;
    private String demandOrgNm;
    private String bizInfo;
    private String qualifications;
    private String succBidderNm;
    private String phoneNumber;

    @PostPersist
    public void onPostPersist(){
        NoticeRegistered noticeRegistered = new NoticeRegistered();
        BeanUtils.copyProperties(this, noticeRegistered);
        noticeRegistered.publishAfterCommit();
    }

    @PostUpdate
    public void onPostUpdate(){
        SuccessBidderRegistered successBidderRegistered = new SuccessBidderRegistered();
        BeanUtils.copyProperties(this, successBidderRegistered);
        successBidderRegistered.publishAfterCommit();
    }

    @PostRemove
    public void onPostRemove(){
        NoticeCanceled noticeCanceled = new NoticeCanceled();
        BeanUtils.copyProperties(this, noticeCanceled);
        noticeCanceled.publishAfterCommit();
    }


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
    
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}