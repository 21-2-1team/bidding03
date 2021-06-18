![image](https://user-images.githubusercontent.com/70736001/122499258-8ba5c480-d02b-11eb-987b-b097b2d87107.png)

# 서비스 시나리오

### 기능적 요구 사항

```
• 입찰담당부서는 입찰공고를 등록한다.
• 입찰공고가 등록되면 입찰공고가 접수된다.
• 조달업체는 입찰서를 등록한다.
• 입찰서가 등록되면 입찰심사가 접수(등록)된다.
• 심사부서는 심사결과를 등록한다.
• 심사결과가 등록되면 입찰공고에 낙찰자 정보가 등록(공지)된다.
• 입찰담당부서는 입찰공고를 취소 할 수 있다.
• 입찰공고가 취소되면 입찰서 등록 및 심사결과도 취소된다.
• 입찰서 등록, 입찰서 등록 취소, 낙찰자 등록 시 조달업체 담당자에게 SMS를 발송한다.
• 조달업체는 입찰현황을 조회 할 수 있다.
※ 위 시나리오는 가상의 절차로, 실제 업무와 다를 수 있습니다.
```

### 비기능적 요구 사항

```
1. 트랜잭션
  - 심사결과가 등록되면 입찰공고에 낙찰자 정보가 등록되어야 한다. (Sync 호출)
2. 장애격리
  - 입찰심사 기능이 수행되지 않더라도 입찰관리, 입찰참여 기능은 365일 24시간 받을 수 있어야 한다. Async (event-driven), Eventual Consistency
  - 입찰참여 기능이 과중되면 사용자를 잠시 동안 받지 않고 입찰참여를 잠시후에 하도록 유도한다. Circuit breaker, fallback
3. 성능
  - 조달업체는 입찰현황조회 화면에서 입찰 상태를 확인 할 수 있어야 한다.CQRS - 조회전용 서비스
```

### Microservice명

```
입찰관리 – BiddingManagement
입찰참여 - BiddingParticipation
입찰심사 - BiddingExamination
```



# 분석/설계

### AS-IS 조직 (Horizontally-Aligned)

![1  AS-IS조직](https://user-images.githubusercontent.com/84000922/122162394-7b1c0f80-ceae-11eb-95c4-8952596bb623.png)




### TO-BE 조직 (Vertically-Aligned)

![2  TO-BE 조직](https://user-images.githubusercontent.com/84000922/122162398-7c4d3c80-ceae-11eb-88b9-863f1e58ba41.png)






### 이벤트 도출

![3  이벤트 도출](https://user-images.githubusercontent.com/84000922/122162410-7fe0c380-ceae-11eb-9822-adb2c3b8d62a.png)




### 부적격 이벤트 탈락

![4  부적격 이벤트 탈락](https://user-images.githubusercontent.com/84000922/122162412-7fe0c380-ceae-11eb-8aba-20f04b2a4dbb.png)

```
- 과정중 도출된 잘못된 도메인 이벤트들을 걸러내는 작업을 수행
- 이의제기등록됨 : 후행 시나리오라서 제외
- 가격심사점수등록됨 : 속성 정보여서 제외
- 입찰공고메뉴선택됨, 입찰현황조회됨 : UI 의 이벤트이지, 업무적인 의미의 이벤트가 아니라서 제외 
```




### 액터, 커맨드 부착하여 읽기 좋게

![5  액터, 커맨드 부착하여 읽기 좋게](https://user-images.githubusercontent.com/84000922/122162413-80795a00-ceae-11eb-9b06-668274f351f7.png)




### 어그리게잇으로 묶기

![6  어그리게잇으로 묶기](https://user-images.githubusercontent.com/84000922/122162415-80795a00-ceae-11eb-8b57-846e1779e420.png)

```
- 입찰관리, 입찰참여, 입찰심사는 그와 연결된 command 와 event 들에 의하여 트랜잭션이 유지되어야 하는 단위로 그들 끼리 묶어줌
```




### 바운디드 컨텍스트로 묶기

![7  바운디드 컨텍스트로 묶기](https://user-images.githubusercontent.com/84000922/122162416-8111f080-ceae-11eb-87be-10c03082eab2.png)

```
도메인 서열 분리
- Core Domain: 입찰관리, 입찰참여: 없어서는 안될 핵심 서비스이며, 연간 Up-time SLA 수준을 99.999% 목표, 입찰관리배포주기는 1개월 1회 미만, 입찰참여 배포주기는 1주일 1회 미만
 - Supporting Domain: 입찰심사 : 경쟁력을 내기 위한 서비스이며, SLA 수준은 연간 60% 이상 uptime 목표, 배포주기는 각 팀의 자율이나 표준 스프린트 주기가 1주일 이므로 1주일 1회 이상을 기준으로 함. 
- General Domain: Notification : 알림서비스로 3rd Party 외부 서비스를 사용하는 것이 경쟁력이 높음 (핑크색으로 이후 전환할 예정)
```




### 폴리시 부착, 이동 및 컨텍스트 매핑(점선은 Pub/Sub, 실선은 Req/Resp)

![image](https://user-images.githubusercontent.com/84000959/122225382-fc47c680-ceef-11eb-859f-70c06e195310.png)



### 완성된 1차 모형

![image](https://user-images.githubusercontent.com/84000959/122225301-e76b3300-ceef-11eb-8263-847226aba7a6.png)



### 1차 완성본에 대한 기능적 요구사항을 커버하는지 검증 (1/2)

![10  1차 완성본에 대한 기능적](https://user-images.githubusercontent.com/84000922/122162421-82431d80-ceae-11eb-8a7d-18df4aff613a.png)

```
1) 입찰담당부서는 입찰공고를 등록한다. 입찰공고가 등록되면 입찰공고가 접수된다.
2) 조달업체는 입찰서를 등록한다. 입찰서가 등록되면 입찰심사가 접수(등록)된다.
3) 심사부서는 심사결과를 등록한다. 심사결과가 등록되면 입찰공고에 낙찰자 정보가 등록(공지)된다.
```




### 1차 완성본에 대한 기능적 요구사항을 커버하는지 검증 (2/2)

![11  1차 완성본에 대한 기능적 요구사항](https://user-images.githubusercontent.com/84000922/122162422-82431d80-ceae-11eb-9645-c57cd204c18e.png)

```
1) 입찰담당부서는 입찰공고를 취소 할 수 있다. 
   입찰공고가 취소되면 입찰서 등록 및 심사결과도 취소된다.
2) 입찰서 등록, 입찰서 등록 취소, 낙찰자 등록 시 조달업체 담당자에게 SMS를 발송한다.
3) 조달업체는 입찰현황을 조회 할 수 있다.
```




### 1차 완성본에 대한 비기능적 요구사항을 커버하는지 검증

![12  1차 완성본에 대한 비기능적](https://user-images.githubusercontent.com/84000922/122162424-82dbb400-ceae-11eb-92d5-9f938ac7d6cf.png)

```
1. 트랜잭션
  - 심사결과가 등록되면 입찰공고에 낙찰자 정보가 등록되어야 한다. (Sync 호출)
2. 장애격리
  - 입찰심사 기능이 수행되지 않더라도 입찰관리, 입찰참여 기능은 365일 24시간 받을 수 있어야 한다. 
    Async (event-driven), Eventual Consistency
  - 입찰참여 기능이 과중되면 사용자를 잠시 동안 받지 않고 입찰참여를 잠시후에 하도록 유도한다.
    Circuit breaker, fallback
3. 성능
  - 조달업체는 입찰현황조회 화면에서 입찰 상태를 확인 할 수 있어야 한다.CQRS - 조회전용 서비스
```




### 헥사고날 아키텍처 다이어그램 도출

![13  헥사고날 아키텍처 다이어그램 도출](https://user-images.githubusercontent.com/84000922/122162425-82dbb400-ceae-11eb-9e47-eef31b055935.png)




### Git Organization / Repositories

![14  Git Organization Repositories](https://user-images.githubusercontent.com/84000922/122162427-83744a80-ceae-11eb-987e-b214c71f4213.png)


# 구현:

(서비스 별 포트) 분석/설계 단계에서 도출된 헥사고날 아키텍처에 따라, 각 BC별로 대변되는 마이크로 서비스들을 스프링부트 등으로 구현하였다. 구현한 각 서비스를 로컬에서 실행하는 방법은 아래와 같다 (각자의 포트넘버는 8081 ~ 8085, 8088 이다)

```
cd BiddingManagement
mvn spring-boot:run

cd BiddingParticipation
mvn spring-boot:run 

cd BiddingExamination
mvn spring-boot:run  

cd Notification
mvn spring-boot:run

cd MyPage
mvn spring-boot:run

cd gateway
mvn spring-boot:run
```

## DDD 의 적용

- (Entity 예시) 각 서비스내에 도출된 핵심 Aggregate Root 객체를 Entity 로 선언하였다: (아래 예시는 입찰관리 마이크로 서비스). 이때 가능한 현업에서 사용하는 언어 (유비쿼터스 랭귀지)를 그대로 사용하려고 노력했다.

```
package bidding;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.Date;

@Entity
@Table(name="BiddingManagement_table")
public class BiddingManagement {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String noticeNo;
    private String title;
    private Date dueDate;
    private Integer price;
    private String demandOrgNm;
    private String bizInfo;
    private String qualifications;
    private String succBidderNm;
    private String phoneNumber;

    @PostPersist
    public void onPostPersist(){
        NoticeRegistered noticeRegistered = new NoticeRegistered();
        BeanUtils.copyProperties(this, noticeRegistered);
        noticeRegistered.publishAfterCommit();
    }

    @PostUpdate
    public void onPostUpdate(){
        SuccessBidderRegistered successBidderRegistered = new SuccessBidderRegistered();
        BeanUtils.copyProperties(this, successBidderRegistered);
        successBidderRegistered.publishAfterCommit();
    }

    @PostRemove
    public void onPostRemove(){
        NoticeCanceled noticeCanceled = new NoticeCanceled();
        BeanUtils.copyProperties(this, noticeCanceled);
        noticeCanceled.publishAfterCommit();
    }


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
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
    public String getDemandOrgNm() {
        return demandOrgNm;
    }

    public void setDemandOrgNm(String demandOrgNm) {
        this.demandOrgNm = demandOrgNm;
    }
    public String getBizInfo() {
        return bizInfo;
    }

    public void setBizInfo(String bizInfo) {
        this.bizInfo = bizInfo;
    }
    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }
    public String getSuccBidderNm() {
        return succBidderNm;
    }

    public void setSuccBidderNm(String succBidderNm) {
        this.succBidderNm = succBidderNm;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
```
- (Repository 예시) Entity Pattern 과 Repository Pattern 을 적용하여 JPA 를 통하여 다양한 데이터소스 유형 (RDB or NoSQL) 에 대한 별도의 처리가 없도록 데이터 접근 어댑터를 자동 생성하기 위하여 Spring Data REST 의 RestRepository 를 적용하였다
```
package bidding;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="biddingManagements", path="biddingManagements")
public interface BiddingManagementRepository extends PagingAndSortingRepository<BiddingManagement, Long>{

    BiddingManagement findByNoticeNo(String noticeNo);
}
```

- 적용 후 REST API 의 테스트

![image](https://user-images.githubusercontent.com/84000959/122253612-47b99f00-cf07-11eb-85c1-bc9736d97ec9.png)

![image](https://user-images.githubusercontent.com/84000959/122253640-5011da00-cf07-11eb-8b8d-b954879ab902.png)

![image](https://user-images.githubusercontent.com/84000959/122253676-586a1500-cf07-11eb-8d1a-7b7b966a27bf.png)

![image](https://user-images.githubusercontent.com/84000959/122253698-5ef88c80-cf07-11eb-8b40-5ae0ccbbd91e.png)

![image](https://user-images.githubusercontent.com/84000959/122327901-9f88f200-cf69-11eb-8aa7-3edffac01e7a.png)

![image](https://user-images.githubusercontent.com/84000959/122253729-66b83100-cf07-11eb-8d38-bfb30aabfa7e.png)

![image](https://user-images.githubusercontent.com/84000959/122253779-720b5c80-cf07-11eb-88c7-8e6c687c63a3.png)

![image](https://user-images.githubusercontent.com/84000959/122253810-79cb0100-cf07-11eb-8557-fad7d460bd75.png)

![image](https://user-images.githubusercontent.com/84000959/122331399-b5011a80-cf6f-11eb-8cdc-27bd84ef5cfd.png)

![image](https://user-images.githubusercontent.com/84000959/122337586-7f612f00-cf79-11eb-94cd-d5af9ff136de.png)


## 폴리글랏 퍼시스턴스

(H2DB, HSQLDB 사용) Notification(문자알림) 서비스는 문자알림 이력이 많이 쌓일 수 있으므로 자바로 작성된 관계형 데이터베이스인 HSQLDB를 사용하기로 하였다. 이를 위해 pom.xml 파일에 아래 설정을 추가하였다.

```
# pom.xml
<dependency>
	<groupId>org.hsqldb</groupId>
    	<artifactId>hsqldb</artifactId>
	<scope>runtime</scope>
</dependency>
```
![image](https://user-images.githubusercontent.com/84000959/122328060-e7a81480-cf69-11eb-9955-954f88b7ec1b.png)

- 입찰관리, 입찰참여, 입찰심사 등 나머지 서비스는 H2 DB를 사용한다.
```
<dependency>
	<groupId>com.h2database</groupId>
	<artifactId>h2</artifactId>
	<scope>runtime</scope>
</dependency>
```

## 동기식 호출 과 Fallback 처리

분석단계에서의 조건 중 하나로 심사결과등록(입찰심사)->낙찰자정보등록(입찰관리) 간의 호출은 동기식 일관성을 유지하는 트랜잭션으로 처리하기로 하였다. 호출 프로토콜은 이미 앞서 Rest Repository 에 의해 노출되어있는 REST 서비스를 FeignClient 를 이용하여 호출하도록 한다. 

- (동기호출-Req)낙찰자정보 등록 서비스를 호출하기 위하여 Stub과 (FeignClient) 를 이용하여 Service 대행 인터페이스 (Proxy) 를 구현 
```
# (BiddingExamination) BiddingManagementService.java
package bidding.external;

@FeignClient(name="BiddingManagement", url="http://${api.url.bidding}:8080", fallback=BiddingManagementServiceFallback.class)
public interface BiddingManagementService {

    @RequestMapping(method= RequestMethod.GET, path="/biddingManagements/registSucessBidder")
    public boolean registSucessBidder(@RequestParam("noticeNo") String noticeNo,
    @RequestParam("succBidderNm") String succBidderNm, @RequestParam("phoneNumber") String phoneNumber);

}
```

- (Fallback) 낙찰자정보 등록 서비스가 정상적으로 호출되지 않을 경우 Fallback 처리
```
# (BiddingExamination) BiddingManagementServiceFallback.java
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
```

```
feign:
  hystrix:
    enabled: true
```

- (동기호출-Res) 낙찰자자정보 등록 서비스 (정상 호출)
```
# (BiddingManagement) BiddingManagementController.java
package bidding;

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
```

- (동기호출-PostUpdate) 심사결과가 등록된 직후(@PostUpdate) 낙찰자정보 등록을 요청하도록 처리 (낙찰자가 아닌 경우, 이후 로직 스킵)
```
# BiddingExamination.java (Entity)

    @PostUpdate
    public void onPostUpdate(){
        // 낙찰업체가 아니면 Skip.
        if(getSuccessBidderFlag() == false) return;

        try{
            // mappings goes here
            boolean isUpdated = BiddingExaminationApplication.applicationContext.getBean(bidding.external.BiddingManagementService.class)
            .registSucessBidder(getNoticeNo(), getCompanyNm(), getPhoneNumber());

            if(isUpdated == false){
                throw new Exception("입찰관리 서비스의 입찰공고에 낙찰자 정보가 갱신되지 않음");
            }
        }catch(java.net.ConnectException ce){
            throw new Exception("입찰관리 서비스 연결 실패");
        }catch(Exception e){
            throw new Exception("입찰관리 서비스 처리 실패");
        }
```

- (동기호출-테스트) 동기식 호출에서는 호출 시간에 따른 타임 커플링이 발생하며, 입찰관리 시스템이 장애가 나면 입찰심사 등록도 못 한다는 것을 확인:

```
# 입찰관리(BiddingManagement) 서비스를 잠시 내려놓음 (ctrl+c)

#심사결과 등록 : Fail
http PATCH http://localhost:8083/biddingExaminations/1 noticeNo=n01 participateNo=p01 successBidderFlag=true

#입찰관리 서비스 재기동
cd BiddingManagement
mvn spring-boot:run

#심사결과 등록 : Success
http PATCH http://localhost:8083/biddingExaminations/1 noticeNo=n01 participateNo=p01 successBidderFlag=true
```

- 또한 과도한 요청시에 서비스 장애가 도미노 처럼 벌어질 수 있다. (서킷브레이커, 폴백 처리는 운영단계에서 설명한다.)




## 비동기식 호출 / 시간적 디커플링 / 장애격리 / 최종 (Eventual) 일관성 테스트


입찰공고가 등록된 후에 입찰참여 시스템에 알려주는 행위는 동기식이 아니라 비 동기식으로 처리하여 입찰참여 시스템의 처리를 위하여 입찰공고 트랜잭션이 블로킹 되지 않도록 처리한다.
 
- (Publish) 이를 위하여 입찰공고 기록을 남긴 후에 곧바로 등록 되었다는 도메인 이벤트를 카프카로 송출한다(Publish)
 
```
@Entity
@Table(name="BiddingManagement_table")
public class BiddingManagement {

 ...
    @PostPersist
    public void onPostPersist(){
        NoticeRegistered noticeRegistered = new NoticeRegistered();
        BeanUtils.copyProperties(this, noticeRegistered);
        noticeRegistered.publishAfterCommit();
    }
```
- (Subscribe-등록) 입찰참여 서비스에서는 입찰공고 등록됨 이벤트를 수신하면 입찰공고 번호를 등록하는 정책을 처리하도록 PolicyHandler를 구현한다:

```
@Service
public class PolicyHandler{

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverNoticeRegistered_RecieveBiddingNotice(@Payload NoticeRegistered noticeRegistered){

        if(!noticeRegistered.validate()) return;

        if(noticeRegistered.isMe()){
            BiddingParticipation biddingParticipation = new BiddingParticipation();
            biddingParticipation.setNoticeNo(noticeRegistered.getNoticeNo());

            biddingParticipationRepository.save(biddingParticipation);
        }
    }

```
- (Subscribe-취소) 입찰참여 서비스에서는 입찰공고가 취소됨 이벤트를 수신하면 입찰참여 정보를 삭제하는 정책을 처리하도록 PolicyHandler를 구현한다:
  
```
@Service
public class PolicyHandler{
    @Autowired BiddingParticipationRepository biddingParticipationRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverNoticeCanceled_CancelBiddingParticipation(@Payload NoticeCanceled noticeCanceled){

        if(!noticeCanceled.validate()) return;

        if(noticeCanceled.isMe()){
            BiddingParticipation biddingParticipation = biddingParticipationRepository.findByNoticeNo(noticeCanceled.getNoticeNo());
            biddingParticipationRepository.delete(biddingParticipation);
        }
            
    }

```

- (장애격리) 입찰관리, 입찰참여 시스템은 입찰심사 시스템과 완전히 분리되어 있으며, 이벤트 수신에 따라 처리되기 때문에, 입찰심사 시스템이 유지보수로 인해 잠시 내려간 상태라도 입찰관리, 입찰참여 서비스에 영향이 없다:
```
# 입찰심사 서비스 (BiddingExamination) 를 잠시 내려놓음 (ctrl+c)

#입찰공고 등록 : Success
http POST localhost:8081/biddingManagements noticeNo=n33 title=title33
#입찰참여 등록 : Success
http PATCH http://localhost:8082/biddingParticipations/2 noticeNo=n33 participateNo=p33 companyNo=c33 companyNm=doremi33 phoneNumber=010-1234-1234

#입찰관리에서 낙찰업체명 갱신 여부 확인
http localhost:8081/biddingManagements/2     # 낙찰업체명 갱신 안 됨 확인

#입찰심사 서비스 기동
cd BiddingExamination
mvn spring-boot:run

#심사결과 등록 : Success
http PATCH http://localhost:8083/biddingExaminations/2 noticeNo=n33 participateNo=p33 successBidderFlag=true

#입찰관리에서 낙찰업체명 갱신 여부 확인
http localhost:8081/biddingManagements/2     # 낙찰업체명 갱신됨 확인
```

# 운영:

OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO

## Deploy

- GitHub 와 연결 후 로컬빌드를 진행 진행
```
	cd team
	mkdir sourcecode
	cd sourcecode
	git clone --recurse-submodules https://github.com/21-2-1team/bidding03.git
	
	cd bidding
	cd BiddingExamination
	mvn package
	
	cd ../BiddingManagement
	mvn package
	
	cd ../BiddingParticipation
	mvn package
	
	cd ../MyPage
	mvn package
	
	
	cd ../Notification
	mvn package
	
	
	cd ../gateway
        mvn package
```
- namespace 등록 및 변경
```
kubectl config set-context --current --namespace=bidding  --> bidding namespace 로 변경

kubectl create ns bidding
```

- ACR Build 등록
```
az acr build --registry user01skccacr --image user01skccacr.azurecr.io/biddingexamination:latest .
```
![image](https://user-images.githubusercontent.com/70736001/122502677-096cce80-d032-11eb-96e7-84a8024ab45d.png)

나머지 프로세스에 대해서도 동일하게 등록을 진행함
```
az acr build --registry user01skccacr --image user01skccacr.azurecr.io/biddingmanagement:latest .
az acr build --registry user01skccacr --image user01skccacr.azurecr.io/biddingparticipation:latest .
az acr build --registry user01skccacr --image user01skccacr.azurecr.io/biddingparticipation:latest .
az acr build --registry user01skccacr --image user01skccacr.azurecr.io/mypage:latest  .
az acr build --registry user01skccacr --image user01skccacr.azurecr.io/notification:latest  .
az acr build --registry user01skccacr --image user01skccacr.azurecr.io/gateway:latest .
```

- 배포진행
1.bidding/BiddingExamination/kubernetes/deployment.yml 파일 수정 (BiddingManagement/BiddingParticipation/MyPage/Notification/gateway 동일)
![image](https://user-images.githubusercontent.com/70736001/122503045-b0ea0100-d032-11eb-8cb0-7fb906d5d939.png)

2.bidding/BiddingExamination/kubernetes/service.yaml 파일 수정 (BiddingManagement/BiddingParticipation/MyPage/Notification 동일)
![image](https://user-images.githubusercontent.com/70736001/122503096-c6f7c180-d032-11eb-93ff-8199c74fbb64.png)

3.bidding/gateway/kubernetes/service.yaml 파일 수정

![image](https://user-images.githubusercontent.com/70736001/122503123-da0a9180-d032-11eb-9283-224d7860c9c3.png)

4. 배포작업 수행
``` 
	cd gateway/kubernetes
	kubectl apply -f deployment.yml
	kubectl apply -f service.yaml
	
	cd ../../BiddingExamination/kubernetes
	kubectl apply -f deployment.yml
	kubectl apply -f service.yaml
	
	cd ../../BiddingManagement/kubernetes
	kubectl apply -f deployment.yml
	kubectl apply -f service.yaml
	
	
	cd ../../BiddingParticipation/kubernetes
	kubectl apply -f deployment.yml
	kubectl apply -f service.yaml
	
	
	cd ../../MyPage/kubernetes
	kubectl apply -f deployment.yml
	kubectl apply -f service.yaml
	
	
	cd ../../Notification/kubernetes
	kubectl apply -f deployment.yml
	kubectl apply -f service.yaml
``` 

5. 배포결과 확인
``` 
kubectl get all
``` 
![image](https://user-images.githubusercontent.com/70736001/122503307-2b1a8580-d033-11eb-83fc-63b0f2154e3b.png)

- Kafka 설치
``` 
curl https://raw.githubusercontent.com/helm/helm/master/scripts/get > get_helm.sh
chmod 700 get_helm.sh
./get_helm.sh

kubectl --namespace kube-system create sa tiller 
kubectl create clusterrolebinding tiller --clusterrole cluster-admin --serviceaccount=kube-system:tiller
helm init --service-account tiller

helm repo add incubator https://charts.helm.sh/incubator
helm repo update

kubectl create ns kafka
helm install --name my-kafka --namespace kafka incubator/kafka

kubectl get all -n kafka
``` 
설치 후 서비스 재기동

## Autoscale (HPA)
OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO

- 리소스에 대한 사용량 정의(bidding/BiddingManagement/kubernetes/deployment.yml)
![image](https://user-images.githubusercontent.com/70736001/122503960-49cd4c00-d034-11eb-8ab4-b322e7383cc0.png)

- Autoscale 설정
OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
```
kubectl autoscale deployment biddingmanagement --cpu-percent=20 --min=1 --max=10
```

- siege 생성
```
kubectl apply -f - <<EOF
apiVersion: v1
kind: Pod
metadata:
  name: siege
  namespace: bidding
spec:
  containers:
  - name: siege
    image: apexacme/siege-nginx
EOF
```
- 부하발생
```
kubectl exec -it pod/siege  -c siege -n bidding -- /bin/bash
siege -c50 -t30S -v --content-type "application/json" 'http://52.231.8.61:8080/biddingManagements POST {"noticeNo":1,"title":"AAA"}'
```
- 시스템 부하 발생에 대한 결과 확인
```
watch kubectl get al
```
1.테스트전

![image](https://user-images.githubusercontent.com/70736001/122504322-0aebc600-d035-11eb-883f-35110d9d0457.png)

2.테스트후

![image](https://user-images.githubusercontent.com/70736001/122504349-1e972c80-d035-11eb-814e-a5ab909215c4.png)

3.부하발생 결과

![image](https://user-images.githubusercontent.com/70736001/122504389-31a9fc80-d035-11eb-976e-f43261d1a8c2.png)

## Config Map
OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO

- 파일 수정

![image](https://user-images.githubusercontent.com/70736001/122505096-9dd93000-d036-11eb-91b7-0ec57b6e1b10.png)

- Yaml 파일 수정

![image](https://user-images.githubusercontent.com/70736001/122505177-c5c89380-d036-11eb-91b3-f399547b50ff.png)

- Config Map 생성 및 생성 확인
```
kubectl create configmap bidding-cm --from-literal=url=BiddingManagement
kubectl get cm
```

![image](https://user-images.githubusercontent.com/70736001/122505221-dc6eea80-d036-11eb-8757-b97f8d75baff.png)

```
kubectl get cm bidding-cm -o yaml
```

![image](https://user-images.githubusercontent.com/70736001/122505270-f6103200-d036-11eb-8c96-513f95448989.png)

```
kubectl get pod
```

![image](https://user-images.githubusercontent.com/70736001/122505313-0fb17980-d037-11eb-9b57-c0d14f468a1c.png)


## Zero-Downtime deploy (Readiness Probe)
OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
- deployment.yml에 정상 적용되어 있는 readinessProbe
```
readinessProbe:
httpGet:
  path: '/biddingManagements'
  port: 8080
initialDelaySeconds: 10
timeoutSeconds: 2
periodSeconds: 5
failureThreshold: 10
```

- Readness 적용 전 테스트 (deployment.yml에서 readiness 설정 제거 후 (주석처리), 배포중 siege 테스트 진행)

![image](https://user-images.githubusercontent.com/70736001/122505873-2906f580-d038-11eb-86b8-2f8388f82dd1.png)

```
kubectl exec -it pod/siege  -c siege -n bidding -- /bin/bash
siege -c100 -t5S -v --content-type "application/json" 'http://20.194.120.4:8080/biddingManagements POST {"noticeNo":1,"title":"AAA"}
```
1.부하테스트 전

![image](https://user-images.githubusercontent.com/70736001/122506020-75eacc00-d038-11eb-99df-4a4b90478bc3.png)

2.부하테스트 후

![image](https://user-images.githubusercontent.com/70736001/122506060-84d17e80-d038-11eb-8449-b94b28a0f385.png)

3.생성중인 Pod 에 대한 요청이 들어가 오류발생

![image](https://user-images.githubusercontent.com/70736001/122506129-a03c8980-d038-11eb-8822-5ec57926b900.png)

- biddingmanagement가 배포되는 중,정상 실행중인 biddingmanagement으로의 요청은 성공(201),배포중인 biddingmanagement으로의 요청은 실패(503 - Service Unavailable) 확인
hpa 설정은 아래와 같이 되어 있다고 전제
kubectl autoscale deployment biddingmanagement --cpu-percent=20 --min=1 --max=10
- hpa 설정에 의해 target 지수 초과하여 biddingmanagement scale-out 진행됨


- Readness 적용 후 테스트(deployment.yml에서 readiness 설정 제거 후 (주석처리), 배포중 siege 테스트 진행)

![image](https://user-images.githubusercontent.com/70736001/122506358-2527a300-d039-11eb-84cb-62eb09687bda.png)

1.부하테스트 전

![image](https://user-images.githubusercontent.com/70736001/122506400-3c669080-d039-11eb-8e5e-a4f76b0e2956.png)

2.부하테스트 후

![image](https://user-images.githubusercontent.com/70736001/122506421-4be5d980-d039-11eb-92a2-44e7827299bf.png)

3.생성중인 Pod 에 대한 요청이 들어가 오류발생

![image](https://user-images.githubusercontent.com/70736001/122506471-61f39a00-d039-11eb-9077-608f375e27f3.png)


## Self-healing (Liveness Probe)
OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO

- deployment.yml에 정상 적용되어 있는 LivenessProbe
```
 livenessProbe:
    httpGet:
        path: '/biddingmanagement/failed'
        port: 8090
      initialDelaySeconds: 30
      timeoutSeconds: 2
      periodSeconds: 5
      failureThreshold: 5
```

-  deployment.yml에서 liveness 설정 추가 후 잘못된 정보로 URL 정보로 설정 시 프로세스 리스타트 테스트

![image](https://user-images.githubusercontent.com/70736001/122506714-d75f6a80-d039-11eb-8bd0-223490797b58.png)

- 재배포 후 OOOOOOOO

1.배포 전

![image](https://user-images.githubusercontent.com/70736001/122506797-fb22b080-d039-11eb-9a0b-754e0fea45b2.png)

2.배포 후

![image](https://user-images.githubusercontent.com/70736001/122506831-0c6bbd00-d03a-11eb-880c-dc8d3e00798f.png)

## Circuit Breaker
OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO

- OOOOOOOO
```
OOOOOOOO
```

- OOOOOOOO
```
OOOOOOOO
```

- OOOOOOOO
```
OOOOOOOO
```

- OOOOOOOO
```
OOOOOOOO
```

- OOOOOOOO
```
OOOOOOOO
```

- OOOOOOOO
```
OOOOOOOO
```
