package genetic;

import genetic.modded.LatticeAlterer;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.jenetics.Genotype;
import org.jenetics.IntegerGene;
import org.jenetics.Population;

import common.Graph;


public class SelfLearnOperator extends LatticeAlterer<IntegerGene, Double>{
    private final int size;

    protected SelfLearnOperator(int size, int latticeWidth, int latticeHeight, Graph graph) {
        super(0, latticeWidth, latticeHeight, graph);
        this.size = size;
    }
    
    protected SelfLearnOperator(int size, int latticeSize, Graph graph) {
        this(size, latticeSize, latticeSize, graph);
    }
    

    @Override
    public int alter(Population<IntegerGene, Double> population, final long generation, final Function<Genotype<IntegerGene>, Map<Integer, Set<Integer>>> communityCache) {
        // TODO Auto-generated method stub
        return 0;
    }

}
