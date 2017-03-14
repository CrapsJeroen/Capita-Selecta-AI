package genetic;

import genetic.modded.LatticeAlterer;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.jenetics.Genotype;
import org.jenetics.IntegerGene;
import org.jenetics.Population;

import common.Graph;


public class HybridNeighborhoodCrossover extends LatticeAlterer<IntegerGene, Double>{

    final double probCrossover;
    final double probStrat;
    
    protected HybridNeighborhoodCrossover(final double probCrossover, final double probStrat, int latticeWidth, int latticeHeight, Graph graph) {
        super(0, latticeWidth, latticeHeight, graph);
        this.probCrossover = probCrossover;
        this.probStrat = probStrat;
    }
    
    protected HybridNeighborhoodCrossover(final double probCrossover, final double probStrat, int latticeSize, Graph graph) {
        this(probCrossover, probStrat, latticeSize, latticeSize, graph);
    }

    @Override
    public int alter(Population<IntegerGene, Double> population, final long generation, final Function<Genotype<IntegerGene>, Map<Integer, Set<Integer>>> communityCache) {
        // TODO Auto-generated method stub
        return 0;
    }

}
