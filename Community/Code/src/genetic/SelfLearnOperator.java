package genetic;

import org.jenetics.AbstractAlterer;
import org.jenetics.IntegerGene;
import org.jenetics.Population;


public class SelfLearnOperator extends AbstractAlterer<IntegerGene, Double>{

    protected SelfLearnOperator(final double probability) {
        super(probability);
    }

    @Override
    public int alter(Population<IntegerGene, Double> population, final long generation) {
        // TODO Auto-generated method stub
        return 0;
    }

}
