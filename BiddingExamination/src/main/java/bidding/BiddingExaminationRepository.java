package bidding;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="biddingExaminations", path="biddingExaminations")
public interface BiddingExaminationRepository extends PagingAndSortingRepository<BiddingExamination, Long>{

	BiddingExamination findByNoticeNo(String noticeNo);


}
