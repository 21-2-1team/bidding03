package bidding.external;

import org.springframework.stereotype.Component;

@Component
public class BiddingManagementServiceFallback implements BiddingManagementService{

    @Override
    public boolean registSucessBidder(String noticeNo,String succBidderNm, String phoneNumber){
        System.out.println("★★★★★★★★★★★Circuit breaker has been opened. Fallback returned instead.★★★★★★★★★★★");
        return false;
    }
}
