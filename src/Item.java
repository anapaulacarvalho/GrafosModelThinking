public class Item {
    private int origem;
    private int destino;
    private int peso;

    public Item(int origem, int destino, int peso) {
        this.origem = origem;
        this.destino = destino;
        this.peso = peso;
    }
    
    

    public int getOrigem() {
        return origem;
    }

    public void setOrigem(int origem) {
        this.origem = origem;
    }

    public int getDestino() {
        return destino;
    }

    public void setDestino(int destino) {
        this.destino = destino;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }
    
    @Override
    public boolean equals(Object o) {
        
        if (this.destino == ((Item) o).getDestino() &&  this.origem == ((Item) o).getOrigem() ) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + this.origem;
        hash = 23 * hash + this.destino;
        return hash;
    }

    @Override
    public String toString() {
        return origem + "," + destino;
    }
    
    
    
    
    
    
}
