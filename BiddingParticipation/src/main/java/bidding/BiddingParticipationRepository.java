package bidding;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="biddingParticipations", path="biddingParticipations")
public interface BiddingParticipationRepository extends PagingAndSortingRepository<BiddingParticipation, Long>{

	BiddingParticipation findByNoticeNo(String noticeNo);


}
