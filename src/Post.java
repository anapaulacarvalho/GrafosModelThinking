
import java.util.Date;

public class Post {
    private int id;
    private int postTypeId;
    private Date creationDate;
    private int score;
    private String body;
    private int ownerUserId;

   
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostTypeId() {
        return postTypeId;
    }

    public void setPostTypeId(int postTypeId) {
        this.postTypeId = postTypeId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(int ownerUserId) {
        this.ownerUserId = ownerUserId;
    }
    
    @Override
    public boolean equals(Object o) {
        boolean result = false;
        if (this.getId() == ((Post) o).getId()) {
            result = true;
        }
        return result;
    }
    
    
    
}
