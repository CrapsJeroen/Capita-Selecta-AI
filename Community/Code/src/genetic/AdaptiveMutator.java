package genetic;

import genetic.modded.LatticeAlterer;
import genetic.modded.LatticeHelper;

import org.jenetics.IntegerGene;
import org.jenetics.Population;


public class AdaptiveMutator extends LatticeAlterer{

    protected AdaptiveMutator(double probability, int latticeWidth, int latticeHeight, LatticeHelper<Double> helper) {
        super(probability, latticeWidth, latticeHeight, helper);
    }
    
    protected AdaptiveMutator(double probability, int latticeSize, LatticeHelper<Double> helper) {
        super(probability, latticeSize, latticeSize, helper);
    }

    @Override
    public int alter(Population<IntegerGene, Double> population, final long generation) {
        System.out.println(helper.getSteadyGenerations());
        // TODO Auto-generated method stub
        return 0;
    }

}
