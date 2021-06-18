package bidding;

import java.util.Date;

public class BiddingParticipated extends AbstractEvent {

    private Long id;
    private String noticeNo;
    private String participateNo;
    private String companyNo;
    private String companyNm;
    private Date participateDate;
    private Integer offerPrice;
    private String offerContents;
    private Boolean feePaymentFlag;
    private String phoneNumber;
    
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
    public String getParticipateNo() {
        return participateNo;
    }
    public void setParticipateNo(String participateNo) {
        this.participateNo = participateNo;
    }
    public String getCompanyNo() {
        return companyNo;
    }
    public void setCompanyNo(String companyNo) {
        this.companyNo = companyNo;
    }
    public String getCompanyNm() {
        return companyNm;
    }
    public void setCompanyNm(String companyNm) {
        this.companyNm = companyNm;
    }
    public Date getParticipateDate() {
        return participateDate;
    }
    public void setParticipateDate(Date participateDate) {
        this.participateDate = participateDate;
    }
    public Integer getOfferPrice() {
        return offerPrice;
    }
    public void setOfferPrice(Integer offerPrice) {
        this.offerPrice = offerPrice;
    }
    public String getOfferContents() {
        return offerContents;
    }
    public void setOfferContents(String offerContents) {
        this.offerContents = offerContents;
    }
    public Boolean getFeePaymentFlag() {
        return feePaymentFlag;
    }
    public void setFeePaymentFlag(Boolean feePaymentFlag) {
        this.feePaymentFlag = feePaymentFlag;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    
}