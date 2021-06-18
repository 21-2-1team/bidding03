package bidding;

public class ExaminationResultRegistered extends AbstractEvent {

    private Long id;
    private String noticeNo;
    private String participateNo;
    private Integer priceScore;
    private Integer skillScore;
    private Boolean successBidderFlag;
    private String comapanyNm;
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
    public Integer getPriceScore() {
        return priceScore;
    }
    public void setPriceScore(Integer priceScore) {
        this.priceScore = priceScore;
    }
    public Integer getSkillScore() {
        return skillScore;
    }
    public void setSkillScore(Integer skillScore) {
        this.skillScore = skillScore;
    }
    public Boolean getSuccessBidderFlag() {
        return successBidderFlag;
    }
    public void setSuccessBidderFlag(Boolean successBidderFlag) {
        this.successBidderFlag = successBidderFlag;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getComapanyNm() {
        return comapanyNm;
    }
    public void setComapanyNm(String comapanyNm) {
        this.comapanyNm = comapanyNm;
    }

}