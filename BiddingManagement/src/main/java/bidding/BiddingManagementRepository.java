package bidding;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="biddingManagements", path="biddingManagements")
public interface BiddingManagementRepository extends PagingAndSortingRepository<BiddingManagement, Long>{

	BiddingManagement findByNoticeNo(String noticeNo);


}
