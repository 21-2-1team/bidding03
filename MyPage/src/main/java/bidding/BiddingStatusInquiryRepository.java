package bidding;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BiddingStatusInquiryRepository extends CrudRepository<BiddingStatusInquiry, Long> {

    List<BiddingStatusInquiry> findByNoticeNo(String noticeNo);
    List<BiddingStatusInquiry> findByParticipateNo(String participateNo);

        void deleteByNoticeNo(String noticeNo);
        void deleteByParticipateNo(String participateNo);
}