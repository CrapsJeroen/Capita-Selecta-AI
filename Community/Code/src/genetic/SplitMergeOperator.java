package genetic;

import genetic.modded.LatticeAlterer;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.jenetics.Genotype;
import org.jenetics.IntegerGene;
import org.jenetics.Phenotype;
import org.jenetics.Population;
import org.jenetics.internal.util.IntRef;
import org.jenetics.util.ISeq;

import common.Graph;


public class SplitMergeOperator extends LatticeAlterer<IntegerGene, Double>{
    
    protected SplitMergeOperator(double probability, int latticeWidth, int latticeHeight, Graph graph) {
        super(probability, latticeWidth, latticeHeight, graph);
    }
    
    protected SplitMergeOperator(double probability, int latticeSize, Graph graph) {
        super(probability, latticeSize, latticeSize, graph);
    }

    @Override
    public int alter(Population<IntegerGene, Double> population, final long generation, 
            final Function<Genotype<IntegerGene>, Map<Integer, Set<Integer>>> communityCache) {
        final IntRef alterations = new IntRef(0);
        Population<IntegerGene, Double> initialPop = population.copy();

        IntStream.range(0, population.size()).forEach(i -> {
            final Phenotype<IntegerGene, Double> pt = initialPop.get(i);
            final Phenotype<IntegerGene, Double> maxNeighbor = getMaxNeighbor(population, i);
            if(pt.getFitness() > maxNeighbor.getFitness()) return;
            
            final Genotype<IntegerGene> mgt = mutate(maxNeighbor.getGenotype(), alterations, communityCache.apply(maxNeighbor.getGenotype()));

            final Phenotype<IntegerGene, Double> mpt = pt.newInstance(mgt, generation);

            population.set(i, mpt);
        });
        return alterations.value;     
    }
    
    private Genotype<IntegerGene> mutate(final Genotype<IntegerGene> max, 
            IntRef alterations, Map<Integer, Set<Integer>> communities){
        double r = random.nextDouble();
        
        int index = random.nextInt(max.getNumberOfGenes());
        
        Set<Integer> options;
        if(r >= _probability){
            options = getAllelesInOtherCommunity(index, communities);
        }else{
            options = getAlleles(index);
        }
        if(options.isEmpty())
            return max;
        
        List<IntegerGene> genes = max.getChromosome().stream().collect(Collectors.toList());
        genes.set(index, genes.get(0).newInstance(options.stream().findAny().get()));
        alterations.value += 1;
        return Genotype.of(max.getChromosome().newInstance( ISeq.of(genes)));
    }

}
