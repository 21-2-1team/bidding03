package bidding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

 @RestController
 public class BiddingManagementController {

    @Autowired
    BiddingManagementRepository biddingManagementRepository;

    @RequestMapping(value = "/biddingManagements/registSucessBidder",
       method = RequestMethod.GET,
       produces = "application/json;charset=UTF-8")
    public boolean registSucessBidder(HttpServletRequest request, HttpServletResponse response) {
       boolean status = false;

       String noticeNo = String.valueOf(request.getParameter("noticeNo"));
       
       System.out.println("@@@@@@@@@@@@@@@@@succBidderNm@" + request.getParameter("succBidderNm"));
       
       BiddingManagement biddingManagement = biddingManagementRepository.findByNoticeNo(noticeNo);
 
       if(biddingManagement.getDemandOrgNm() == null || "조달청".equals(biddingManagement.getDemandOrgNm()) == false){
            biddingManagement.setSuccBidderNm(request.getParameter("succBidderNm"));
            biddingManagement.setPhoneNumber(request.getParameter("phoneNumber"));

            biddingManagementRepository.save(biddingManagement);

            status = true;
       }

       return status;
    }

 }
