
import java.util.ArrayList;
import java.util.Date;

public class Pergunta {
    private int id;
    private Date creationDate;
    private int score;
    private String body;
    private String tags;
    private int postTypeId;
    private int acceptedAnswerId;
    private int ownerUserId;
    private int answerCount;
    ArrayList<Resposta> respostas;
    private Date dataAcceptedAnswerId; //eu criei para armazenar a data de criação da melhor resposta
    private int peso;

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }
    
    

    public Date getDataAcceptedAnswerId() {
        return dataAcceptedAnswerId;
    }

    public void setDataAcceptedAnswerId(Date dataAcceptedAnswerId) {
        this.dataAcceptedAnswerId = dataAcceptedAnswerId;
    }
    
    
    
    public Pergunta(){
        respostas = new ArrayList<>();
    }
    
    public Pergunta(int id){
        this.id = id;
        respostas = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getPostTypeId() {
        return postTypeId;
    }

    public void setPostTypeId(int postTypeId) {
        this.postTypeId = postTypeId;
    }

    public int getAcceptedAnswerId() {
        return acceptedAnswerId;
    }

    public void setAcceptedAnswerId(int acceptedAnswerId) {
        this.acceptedAnswerId = acceptedAnswerId;
    }

    public int getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(int ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    public ArrayList<Resposta> getRespostas() {
        return respostas;
    }

    public void setRespostas(ArrayList<Resposta> respostas) {
        this.respostas = respostas;
    }
    
            
            
     @Override
    public boolean equals(Object o) {
        boolean result = false;
        if (this.getId() == ((Pergunta) o).getId()) {
            result = true;
        }
        return result;
    }
}
