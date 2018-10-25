
public class Vertice  {

    private int nome;
    private double score;
    
    public Vertice(int nome){
        this.nome = nome;
    }

    public int getNome() {
        return nome;
    }

    public void setNome(int nome) {
        this.nome = nome;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    
    @Override
    public boolean equals(Object o) {
        
        Vertice v = (Vertice)o;
        if (v.nome == this.nome){
            return true;
        }
        return false;
    }
    
    
    
    

}
