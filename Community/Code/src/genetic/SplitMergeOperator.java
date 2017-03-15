package genetic;

import genetic.modded.LatticeAlterer;
import genetic.modded.LatticeHelper;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.jenetics.Gene;
import org.jenetics.Genotype;
import org.jenetics.Phenotype;
import org.jenetics.Population;
import org.jenetics.internal.util.IntRef;
import org.jenetics.util.ISeq;


public class SplitMergeOperator<G extends Gene<Integer, G>, C extends Comparable<? super C>> extends LatticeAlterer<G,C>{
    
    protected SplitMergeOperator(double probability, int latticeWidth, int latticeHeight, LatticeHelper<G,C> helper) {
        super(probability, latticeWidth, latticeHeight, helper);
    }
    
    protected SplitMergeOperator(double probability, int latticeSize, LatticeHelper<G,C> helper) {
        super(probability, latticeSize, latticeSize, helper);
    }

    @Override
    public int alter(Population<G, C> population, final long generation) {
        final IntRef alterations = new IntRef(0);
        Population<G, C> initialPop = population.copy();

        IntStream.range(0, population.size()).forEach(i -> {
            final Phenotype<G, C> pt = initialPop.get(i);
            final Phenotype<G, C> maxNeighbor = getMaxNeighbor(population, i);
            if(pt.getFitness().compareTo(maxNeighbor.getFitness()) > 0) return;
            
            final Genotype<G> mgt = mutate(maxNeighbor.getGenotype(), alterations, helper.getCommunities(maxNeighbor.getGenotype()));
            if(alterations.value > 0) helper.updated.add(i);
            final Phenotype<G, C> mpt = pt.newInstance(mgt, generation);

            population.set(i, mpt);
        });
        return alterations.value;     
    }
    
    private Genotype<G> mutate(final Genotype<G> max, 
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
        
        List<G> genes = max.getChromosome().stream().collect(Collectors.toList());
        genes.set(index, genes.get(0).newInstance(options.stream().findAny().get()));
        alterations.value += 1;
        return Genotype.of(max.getChromosome().newInstance( ISeq.of(genes)));
    }

}
