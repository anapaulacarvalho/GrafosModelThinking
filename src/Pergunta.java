
import java.util.ArrayList;
import java.util.Date;

public class Pergunta extends Post {
    private String tags;
    private int acceptedAnswerId;
    private int answerCount;
    private String title;
    
    ArrayList<Resposta> respostas;
    private Date dataAcceptedAnswerId; //eu criei para armazenar a data de criação da melhor resposta
    private int peso;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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
        setId(id);
        respostas = new ArrayList<>();
    }

 
    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getAcceptedAnswerId() {
        return acceptedAnswerId;
    }

    public void setAcceptedAnswerId(int acceptedAnswerId) {
        this.acceptedAnswerId = acceptedAnswerId;
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
