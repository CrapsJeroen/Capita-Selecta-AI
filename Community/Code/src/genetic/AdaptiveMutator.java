package genetic;

import genetic.modded.LatticeAlterer;
import genetic.modded.LatticeHelper;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.jenetics.Genotype;
import org.jenetics.IntegerGene;
import org.jenetics.Population;

import common.Graph;


public class AdaptiveMutator extends LatticeAlterer{

    protected AdaptiveMutator(double probability, int latticeWidth, int latticeHeight, LatticeHelper helper) {
        super(probability, latticeWidth, latticeHeight, helper);
    }
    
    protected AdaptiveMutator(double probability, int latticeSize, LatticeHelper helper) {
        super(probability, latticeSize, latticeSize, helper);
    }

    @Override
    public int alter(Population<IntegerGene, Double> population, final long generation) {
        // TODO Auto-generated method stub
        return 0;
    }

}
