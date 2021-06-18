package bidding;

import javax.persistence.*;

@Entity
@Table(name="BiddingStatusInquiry_table")
public class BiddingStatusInquiry {

        @Id
        @GeneratedValue(strategy=GenerationType.AUTO)
        private Long id;
        private String noticeNo;
        private String participateNo;
        private String companyNm;
        private Boolean biddingResult;
        
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
        public String getCompanyNm() {
            return companyNm;
        }
        public void setCompanyNm(String companyNm) {
            this.companyNm = companyNm;
        }
        public Boolean getBiddingResult() {
            return biddingResult;
        }
        public void setBiddingResult(Boolean biddingResult) {
            this.biddingResult = biddingResult;
        }


}
