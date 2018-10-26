
import java.util.Comparator;

public class ComparadorScore implements Comparator {
        @Override
	public int compare(Object obj1, Object obj2) {
		int retorno = 0;
		if(obj1 == null || obj2 == null)
			return retorno;
		Vertice objeto1 = (Vertice) obj1;
		Vertice objeto2 = (Vertice) obj2;
		if(objeto1.getScore() > objeto2.getScore()) {
			retorno = -1;
		} else if(objeto1.getScore() == objeto2.getScore()) {
			retorno = 0;
		} else {
			retorno = 1;
		}
		return retorno;
	}
}
