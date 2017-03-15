package genetic;

import static org.jenetics.internal.math.random.indexes;
import genetic.modded.LatticeAlterer;
import genetic.modded.LatticeHelper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.jenetics.Genotype;
import org.jenetics.IntegerGene;
import org.jenetics.Phenotype;
import org.jenetics.Population;
import org.jenetics.internal.util.IntRef;
import org.jenetics.util.ISeq;


public class AdaptiveMutator extends LatticeAlterer{

    protected AdaptiveMutator(double probability, int latticeWidth, int latticeHeight, LatticeHelper<Double> helper) {
        super(probability, latticeWidth, latticeHeight, helper);
    }
    
    protected AdaptiveMutator(double probability, int latticeSize, LatticeHelper<Double> helper) {
        super(probability, latticeSize, latticeSize, helper);
    }
    
    @Override
    public int alter(Population<IntegerGene, Double> population, final long generation) {
        double adaptedProb = ((helper.getSteadyGenerations() / helper.maxSteadyGenerations) + 1) * _probability;
        final IntRef alterations = new IntRef(0);

        IntStream.range(0, population.size()).forEach(i -> {
            final Phenotype<IntegerGene, Double> pt = population.get(i);            
            final Genotype<IntegerGene> mgt = mutate(pt.getGenotype(), alterations, adaptedProb);

            final Phenotype<IntegerGene, Double> mpt = pt.newInstance(mgt, generation);

            population.set(i, mpt);
        });
        return alterations.value;     
    }
    
    private Genotype<IntegerGene> mutate(
            final Genotype<IntegerGene> gt1, IntRef alterations, double adaptedProb){
        
        List<IntegerGene> genes = gt1.getChromosome().stream().collect(Collectors.toList());
        
        alterations.value += (int)indexes(random, genes.size(), adaptedProb)
                .peek(i -> genes.set(i, mutate(genes.get(i), i)))
                .count();
        
        return Genotype.of(gt1.getChromosome().newInstance( ISeq.of(genes)));
    }
    
    private IntegerGene mutate(IntegerGene current, int index){
        Optional<Integer> result = getAlleles(index).stream().findAny();
        if(result.isPresent()){
            return current.newInstance(result.get());
        }else{
            return current;
        }
    }

}
