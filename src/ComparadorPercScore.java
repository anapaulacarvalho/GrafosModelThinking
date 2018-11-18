
import java.util.Comparator;
/**
 *
 * @author Ana
 */
public class ComparadorPercScore implements Comparator {
    @Override
    public int compare(Object obj1, Object obj2) {
        int retorno = 0;
        if (obj1 == null || obj2 == null) {
            return retorno;
        }
        Avaliacao objeto1 = (Avaliacao) obj1;
        Avaliacao objeto2 = (Avaliacao) obj2;
        
        
        double perc1 = objeto1.getPercScore();
        double perc2 = objeto2.getPercScore();
                
                
                
        if (perc1 > perc2) {
            retorno = -1;
        } else if (perc1 == perc2) {
            if(objeto1.getQuantMaiorScore() > objeto2.getQuantMaiorScore()){
                retorno = -1;
            }else if (objeto1.getQuantMaiorScore() < objeto2.getQuantMaiorScore()){
                retorno = 1;
            }else{
                retorno = 0;
            }
            
        } else {
            retorno = 1;
        }
        return retorno;
    }
    
}
