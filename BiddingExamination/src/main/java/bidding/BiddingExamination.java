package bidding;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;

@Entity
@Table(name="BiddingExamination_table")
public class BiddingExamination {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String noticeNo;
    private String participateNo;
    private Integer priceScore;
    private Integer skillScore;
    private Boolean successBidderFlag;
    private String companyNm;
    private String phoneNumber;

    @PostUpdate
    public void onPostUpdate() throws Exception{
        ExaminationResultRegistered examinationResultRegistered = new ExaminationResultRegistered();
        BeanUtils.copyProperties(this, examinationResultRegistered);
        examinationResultRegistered.publishAfterCommit();

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        // 낙찰업체가 아니면 Skip.
        if(getSuccessBidderFlag() == false) return;

        try{
            // mappings goes here
            boolean isUpdated = BiddingExaminationApplication.applicationContext.getBean(bidding.external.BiddingManagementService.class)
            .registSucessBidder(getNoticeNo(), getCompanyNm(), getPhoneNumber());

            if(isUpdated == false){
                throw new Exception("입찰관리 서비스의 입찰공고에 낙찰자 정보가 갱신되지 않음");
            }
        }catch(java.net.ConnectException ce){
            throw new Exception("입찰관리 서비스 연결 실패");
        }catch(Exception e){
            throw new Exception("입찰관리 서비스 처리 실패");
        }
        

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

    public String getCompanyNm() {
        return companyNm;
    }

    public void setCompanyNm(String companyNm) {
        this.companyNm = companyNm;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}