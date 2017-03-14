package genetic;

import genetic.modded.LatticeAlterer;
import genetic.modded.LatticeHelper;

import org.jenetics.IntegerGene;
import org.jenetics.Population;


public class SelfLearnOperator extends LatticeAlterer{
    private final int size;

    protected SelfLearnOperator(int size, int latticeWidth, int latticeHeight, LatticeHelper<Double> helper) {
        super(0, latticeWidth, latticeHeight, helper);
        this.size = size;
    }
    
    protected SelfLearnOperator(int size, int latticeSize, LatticeHelper<Double> helper) {
        this(size, latticeSize, latticeSize, helper);
    }
    

    @Override
    public int alter(Population<IntegerGene, Double> population, final long generation) {
        // TODO Auto-generated method stub
        return 0;
    }

}
