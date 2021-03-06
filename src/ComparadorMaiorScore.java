
import java.util.Comparator;

public class ComparadorMaiorScore implements Comparator {

    @Override
    public int compare(Object obj1, Object obj2) {
        int retorno = 0;
        if (obj1 == null || obj2 == null) {
            return retorno;
        }
        Avaliacao objeto1 = (Avaliacao) obj1;
        Avaliacao objeto2 = (Avaliacao) obj2;
        if (objeto1.getQuantMaiorScore() > objeto2.getQuantMaiorScore()) {
            retorno = -1;
        } else if (objeto1.getQuantMaiorScore() == objeto2.getQuantMaiorScore()) {
            retorno = 0;
        } else {
            retorno = 1;
        }
        return retorno;
    }
}

