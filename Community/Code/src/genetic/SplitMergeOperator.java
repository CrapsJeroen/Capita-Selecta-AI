package genetic;

import genetic.modded.LatticeAlterer;
import genetic.modded.LatticeHelper;

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


public class SplitMergeOperator extends LatticeAlterer{
    
    protected SplitMergeOperator(double probability, int latticeWidth, int latticeHeight, LatticeHelper helper) {
        super(probability, latticeWidth, latticeHeight, helper);
    }
    
    protected SplitMergeOperator(double probability, int latticeSize, LatticeHelper helper) {
        super(probability, latticeSize, latticeSize, helper);
    }

    @Override
    public int alter(Population<IntegerGene, Double> population, final long generation) {
        final IntRef alterations = new IntRef(0);
        Population<IntegerGene, Double> initialPop = population.copy();

        IntStream.range(0, population.size()).forEach(i -> {
            final Phenotype<IntegerGene, Double> pt = initialPop.get(i);
            final Phenotype<IntegerGene, Double> maxNeighbor = getMaxNeighbor(population, i);
            if(pt.getFitness() > maxNeighbor.getFitness()) return;
            
            final Genotype<IntegerGene> mgt = mutate(maxNeighbor.getGenotype(), alterations, helper.getCommunities(maxNeighbor.getGenotype()));

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
