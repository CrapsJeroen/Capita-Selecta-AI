package genetic;

import static org.jenetics.internal.math.random.indexes;
import genetic.modded.LatticeAlterer;
import genetic.modded.LatticeHelper;

import java.util.List;
import java.util.stream.Collectors;

import org.jenetics.Gene;
import org.jenetics.Genotype;
import org.jenetics.Phenotype;
import org.jenetics.Population;
import org.jenetics.internal.util.IntRef;
import org.jenetics.util.ISeq;


public class SelfLearnInitialMutator<G extends Gene<Integer, G>, C extends Comparable<? super C>> extends LatticeAlterer<G,C>{

    protected SelfLearnInitialMutator(double probability, int latticeWidth, int latticeHeight, LatticeHelper<G,C> helper) {
        super(probability, latticeWidth, latticeHeight, helper);
    }
    
    protected SelfLearnInitialMutator(double probability, int latticeSize, LatticeHelper<G,C> helper) {
        super(probability, latticeSize, latticeSize, helper);
    }
    
    public Genotype<G> getSingleMutated(Population<G, C> population, int index){
        List<Genotype<G>> neighbors = getNeighbors(population, index).stream()
                .map(ph -> ph.getGenotype())
                .collect(Collectors.toList());
        final Phenotype<G, C> pt = population.get(index); 
        final Genotype<G> mgt = mutate(pt.getGenotype(), neighbors, new IntRef(), _probability);
        return mgt;
    }
    
    private Genotype<G> mutate(
            final Genotype<G> gt1, 
            final List<Genotype<G>> neighbors,
            IntRef alterations, 
            double adaptedProb){
        
        List<G> genes = gt1.getChromosome().stream().collect(Collectors.toList());
        
        alterations.value += (int)indexes(random, genes.size(), adaptedProb)
                .peek(i -> genes.set(i, neighbors.get(random.nextInt(neighbors.size())).getChromosome().getGene(i)))
                .count();
        
        return Genotype.of(gt1.getChromosome().newInstance( ISeq.of(genes)));
    }

}
