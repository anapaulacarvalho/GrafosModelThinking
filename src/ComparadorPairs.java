
import java.util.Comparator;
import org.apache.commons.math3.util.Pair;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Ana
 */
public class ComparadorPairs implements Comparator {
    @Override
    public int compare(Object obj1, Object obj2) {
         Pair<Double, Double> pair1 = (Pair<Double, Double>)obj1;
         Pair<Double, Double> pair2 = (Pair<Double, Double>)obj2;
        int compareFirst = pair1.getFirst().compareTo(pair2.getFirst());
        return compareFirst != 0 ? compareFirst : pair1.getSecond().compareTo(pair2.getSecond());
    }

}
