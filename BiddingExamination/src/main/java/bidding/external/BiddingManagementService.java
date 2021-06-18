package bidding.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//@FeignClient(name="BiddingManagement", url="http://BiddingManagement:8080")
@FeignClient(name="BiddingManagement", url="http://${api.url.bidding}:8080")
public interface BiddingManagementService {

    @RequestMapping(method= RequestMethod.GET, path="/biddingManagements/registSucessBidder")
    public boolean registSucessBidder(@RequestParam("noticeNo") String noticeNo,
    @RequestParam("succBidderNm") String succBidderNm, @RequestParam("phoneNumber") String phoneNumber);

}
