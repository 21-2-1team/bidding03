package bidding;

import bidding.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{
    @Autowired SmsHistoryRepository smsHistoryRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverSuccessBidderRegistered_SendSms(@Payload SuccessBidderRegistered successBidderRegistered){

        if(!successBidderRegistered.validate()) return;

        System.out.println("\n\n##### listener SendSms : " + successBidderRegistered.toJson() + "\n\n");

        if(successBidderRegistered.isMe()){
            SmsHistory smsHistory = new SmsHistory();
            smsHistory.setPhoneNumber(successBidderRegistered.getPhoneNumber());
            smsHistory.setContents("낙찰 되었습니다~~~");

            smsHistoryRepository.save(smsHistory);
        }
            
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverBiddingParticipationCanceled_SendSms(@Payload BiddingParticipationCanceled biddingParticipationCanceled){

        if(!biddingParticipationCanceled.validate()) return;

        System.out.println("\n\n##### listener SendSms : " + biddingParticipationCanceled.toJson() + "\n\n");

        if(biddingParticipationCanceled.isMe()){
            SmsHistory smsHistory = new SmsHistory();
            smsHistory.setPhoneNumber(biddingParticipationCanceled.getPhoneNumber());
            smsHistory.setContents("입찰서 등록 취소");

            smsHistoryRepository.save(smsHistory);
        }
            
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverBiddingParticipated_SendSms(@Payload BiddingParticipated biddingParticipated){

        if(!biddingParticipated.validate()) return;

        System.out.println("\n\n##### listener SendSms : " + biddingParticipated.toJson() + "\n\n");

        if(biddingParticipated.isMe()){
            SmsHistory smsHistory = new SmsHistory();
            smsHistory.setPhoneNumber(biddingParticipated.getPhoneNumber());
            smsHistory.setContents("입찰서 등록 완료");

            smsHistoryRepository.save(smsHistory);
        }
            
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){}


}