package bidding;

import bidding.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BiddingStatusInquiryViewHandler {


    @Autowired
    private BiddingStatusInquiryRepository biddingStatusInquiryRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenBiddingParticipated_then_CREATE_1 (@Payload BiddingParticipated biddingParticipated) {
        try {

            if (!biddingParticipated.validate()) return;

            // view 객체 생성
            BiddingStatusInquiry biddingStatusInquiry = new BiddingStatusInquiry();
            // view 객체에 이벤트의 Value 를 set 함
            biddingStatusInquiry.setId(biddingParticipated.getId());
            biddingStatusInquiry.setNoticeNo(biddingParticipated.getNoticeNo());
            biddingStatusInquiry.setParticipateNo(biddingParticipated.getParticipateNo());
            biddingStatusInquiry.setCompanyNm(biddingParticipated.getCompanyNm());
            // view 레파지 토리에 save
            biddingStatusInquiryRepository.save(biddingStatusInquiry);
        
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whenExaminationResultRegistered_then_UPDATE_1(@Payload ExaminationResultRegistered examinationResultRegistered) {
        try {
            if (!examinationResultRegistered.validate()) return;
                // view 객체 조회
            Optional<BiddingStatusInquiry> biddingStatusInquiryOptional = biddingStatusInquiryRepository.findById(examinationResultRegistered.getId());
            if( biddingStatusInquiryOptional.isPresent()) {
                BiddingStatusInquiry biddingStatusInquiry = biddingStatusInquiryOptional.get();
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                    biddingStatusInquiry.setBiddingResult(examinationResultRegistered.getSuccessBidderFlag());
                // view 레파지 토리에 save
                biddingStatusInquiryRepository.save(biddingStatusInquiry);
            }
            List<BiddingStatusInquiry> biddingStatusInquiryList = biddingStatusInquiryRepository.findByNoticeNo(examinationResultRegistered.getNoticeNo());
            for(BiddingStatusInquiry biddingStatusInquiry : biddingStatusInquiryList){
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                biddingStatusInquiry.setBiddingResult(examinationResultRegistered.getSuccessBidderFlag());
                // view 레파지 토리에 save
                biddingStatusInquiryRepository.save(biddingStatusInquiry);
            }
            List<BiddingStatusInquiry> biddingStatusInquiryList2 = biddingStatusInquiryRepository.findByNoticeNo(examinationResultRegistered.getParticipateNo());
            for(BiddingStatusInquiry biddingStatusInquiry : biddingStatusInquiryList2){
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                biddingStatusInquiry.setBiddingResult(examinationResultRegistered.getSuccessBidderFlag());
                // view 레파지 토리에 save
                biddingStatusInquiryRepository.save(biddingStatusInquiry);
            }
            
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenBiddingParticipationCanceled_then_DELETE_1(@Payload BiddingParticipationCanceled biddingParticipationCanceled) {
        try {
            if (!biddingParticipationCanceled.validate()) return;
            // view 레파지 토리에 삭제 쿼리
            biddingStatusInquiryRepository.deleteById(biddingParticipationCanceled.getId());
            // view 레파지 토리에 삭제 쿼리
            biddingStatusInquiryRepository.deleteByNoticeNo(biddingParticipationCanceled.getNoticeNo());
            // view 레파지 토리에 삭제 쿼리
            biddingStatusInquiryRepository.deleteByParticipateNo(biddingParticipationCanceled.getParticipateNo());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}