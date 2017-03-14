package genetic;

import org.jenetics.Gene;
import org.jenetics.Optimize;
import org.jenetics.Population;
import org.jenetics.Selector;


public class InertSelector<G extends Gene<?,G>,C extends Comparable<? super C>> implements Selector<G, C>{
    
    @Override
    public Population<G, C> select(Population<G, C> population, int count,
            Optimize opt) {
        return population;
    }

}
