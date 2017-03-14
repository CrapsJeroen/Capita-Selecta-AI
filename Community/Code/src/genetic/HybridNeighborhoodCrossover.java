package genetic;

import org.jenetics.AbstractAlterer;
import org.jenetics.IntegerGene;
import org.jenetics.Population;


public class HybridNeighborhoodCrossover extends AbstractAlterer<IntegerGene, Double>{

    protected HybridNeighborhoodCrossover(final double probability) {
        super(probability);
    }

    @Override
    public int alter(Population<IntegerGene, Double> population, final long generation) {
        // TODO Auto-generated method stub
        return 0;
    }

}
