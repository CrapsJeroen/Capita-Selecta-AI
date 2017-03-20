package genetic;

import static org.jenetics.internal.math.random.indexes;
import genetic.modded.LatticeAlterer;
import genetic.modded.LatticeHelper;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.jenetics.Gene;
import org.jenetics.Genotype;
import org.jenetics.Phenotype;
import org.jenetics.Population;
import org.jenetics.internal.util.IntRef;
import org.jenetics.util.ISeq;


public class AdaptiveMutator<G extends Gene<Integer, G>, C extends Comparable<? super C>> extends LatticeAlterer<G,C>{
    private static final boolean ENABLED = true;

    protected AdaptiveMutator(double probability, int latticeWidth, int latticeHeight, LatticeHelper<G,C> helper) {
        super(probability, latticeWidth, latticeHeight, helper);
    }
    
    protected AdaptiveMutator(double probability, int latticeSize, LatticeHelper<G,C> helper) {
        super(probability, latticeSize, latticeSize, helper);
    }
    
    @Override
    public int alter(Population<G, C> population, final long generation) {
        if(!ENABLED) return 0;
        helper.startTimer(helper.mutateTimer);

        double adaptedProb = ((helper.getSteadyGenerations() / helper.maxSteadyGenerations) + 1) * _probability;
        final IntRef alterations = new IntRef(0);

        IntStream.range(0, population.size()).forEach(i -> {
//            System.out.println("MUTATE " + (i));
            final Phenotype<G, C> pt = population.get(i); 
            List<Genotype<G>> neighbors = getNeighbors(population, i).stream()
                    .map(ph -> ph.getGenotype())
                    .collect(Collectors.toList());
            final Phenotype<G, C> maxNeighbor = getMaxNeighbor(population, i);
            if(pt.getFitness().compareTo(maxNeighbor.getFitness()) > 0) return;
            final Genotype<G> mgt = mutate(pt.getGenotype(), neighbors, alterations, adaptedProb);
            if(alterations.value > 0) helper.updated.add(i);
            final Phenotype<G, C> mpt = pt.newInstance(mgt, generation);

            population.set(i, mpt);
        });
        helper.stopTimer(helper.mutateTimer);
        return alterations.value;     
    }
    
    private Genotype<G> mutate(
            final Genotype<G> gt1, 
            final List<Genotype<G>> neighbors,
            IntRef alterations, 
            double adaptedProb){
        
        List<G> genes = gt1.getChromosome().stream().collect(Collectors.toList());
        
//        for(int i = 0; i < genes.size(); i++){
//            if(random.nextFloat() >= adaptedProb) continue;
//            genes.set(i, neighbors.get(random.nextInt(neighbors.size())).getChromosome().getGene(i));
//            alterations.value++;
//        }
        
        alterations.value += (int)indexes(random, genes.size(), adaptedProb)
                .peek(i -> genes.set(i, neighbors.get(random.nextInt(neighbors.size())).getChromosome().getGene(i)))
                .count();
        
        return Genotype.of(gt1.getChromosome().newInstance( ISeq.of(genes)));
    }

}
