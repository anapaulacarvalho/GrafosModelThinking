
import java.util.ArrayList;
import java.util.List;


public class Grafo {

    private List<Vertice> vertices;
    private int tamanho;
    private int[][] matrizAdj;
    
    
    public Grafo(int tamanho){
        vertices = new ArrayList<>();
        this.tamanho = tamanho;
        matrizAdj = new int[tamanho][tamanho];
    }

    public List<Vertice> getVertices() {
        return vertices;
    }

    public void setVertices(List<Vertice> vertices) {
        this.vertices = vertices;
    }
 
    
    
    public int getTamanho() {
        return tamanho;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }

    public int[][] getMatrizAdj() {
        return matrizAdj;
    }

    public void setMatrizAdj(int[][] matrizAdj) {
        this.matrizAdj = matrizAdj;
    }

   

}
