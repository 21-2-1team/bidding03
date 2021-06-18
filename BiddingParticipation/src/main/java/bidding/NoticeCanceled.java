
package bidding;

public class NoticeCanceled extends AbstractEvent {

    private Long id;
    private String noticeNo;
    private String title;
    
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

    
}

