public class Avaliacao {
    private int quantMaiorScore; //foi a resposta com maior score
    private int quantMelhorResp; //foi a resposta escolhida pelo autor da resposta
    private int menorScore;
    private int maiorScore;
    private int quantResp;
    private int ownerId;

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
   
    public int getQuantMaiorScore() {
        return quantMaiorScore;
    }

    public void setQuantMaiorScore(int quantMaiorScore) {
        this.quantMaiorScore = quantMaiorScore;
    }

    public int getQuantMelhorResp() {
        return quantMelhorResp;
    }

    public void setQuantMelhorResp(int quantMelhorResp) {
        this.quantMelhorResp = quantMelhorResp;
    }

    public int getMenorScore() {
        return menorScore;
    }

    public void setMenorScore(int menorScore) {
        this.menorScore = menorScore;
    }

    public int getMaiorScore() {
        return maiorScore;
    }

    public void setMaiorScore(int maiorScore) {
        this.maiorScore = maiorScore;
    }

    public int getQuantResp() {
        return quantResp;
    }

    public void setQuantResp(int quantResp) {
        this.quantResp = quantResp;
    }

    @Override
    public String toString() {
        return "Avaliacao{" +  " ownerId=" + ownerId +  ",quantMaiorScore=" + quantMaiorScore + ", quantMelhorResp=" + quantMelhorResp + ", menorScore=" + menorScore + ", maiorScore=" + maiorScore + ", quantResp=" + quantResp +'}';
    }

   
    
    
    
    
    
    
}
