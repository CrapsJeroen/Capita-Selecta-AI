package genetic;

import genetic.modded.LatticeAlterer;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.jenetics.Genotype;
import org.jenetics.IntegerGene;
import org.jenetics.Population;

import common.Graph;


public class AdaptiveMutator extends LatticeAlterer<IntegerGene, Double>{

    protected AdaptiveMutator(double probability, int latticeWidth, int latticeHeight, Graph graph) {
        super(probability, latticeWidth, latticeHeight, graph);
    }
    
    protected AdaptiveMutator(double probability, int latticeSize, Graph graph) {
        super(probability, latticeSize, latticeSize, graph);
    }

    @Override
    public int alter(Population<IntegerGene, Double> population, final long generation, final Function<Genotype<IntegerGene>, Map<Integer, Set<Integer>>> communityCache) {
        // TODO Auto-generated method stub
        return 0;
    }

}
