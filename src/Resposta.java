
import java.util.Date;

public class Resposta {

    private int id;
    private Date creationDate;
    private int score;
    private String body;
    
    private int postTypeId;
    private int parentId;
    private int ownerUserId;
    
 

    public int getID() {
        return id;
    }

    public void setID(int d) {
        this.id = d;
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

    

    public int getPostTypeId() {
        return postTypeId;
    }

    public void setPostTypeId(int postTypeId) {
        this.postTypeId = postTypeId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
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
        if (this.getID() == ((Resposta) o).getID()) {
            result = true;
        }
        return result;
    }

}
