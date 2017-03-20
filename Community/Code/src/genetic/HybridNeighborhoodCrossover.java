package genetic;

import genetic.modded.LatticeAlterer;
import genetic.modded.LatticeHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.jenetics.Gene;
import org.jenetics.Genotype;
import org.jenetics.Phenotype;
import org.jenetics.Population;
import org.jenetics.internal.util.IntRef;
import org.jenetics.util.ISeq;


public class HybridNeighborhoodCrossover<G extends Gene<Integer, G>, C extends Comparable<? super C>> extends LatticeAlterer<G,C>{
    
    private static final boolean ENABLED = true;
    final double probCrossover;
    final double probStrat;
    
    protected HybridNeighborhoodCrossover(final double probCrossover, final double probStrat, int latticeWidth, int latticeHeight, LatticeHelper<G,C> helper) {
        super(0, latticeWidth, latticeHeight, helper);
        this.probCrossover = probCrossover;
        this.probStrat = probStrat;
    }
    
    protected HybridNeighborhoodCrossover(final double probCrossover, final double probStrat, int latticeSize, LatticeHelper<G,C> helper) {
        this(probCrossover, probStrat, latticeSize, latticeSize, helper);
    }

    @Override
    public int alter(Population<G, C> population, final long generation) {
        if(!ENABLED) return 0;
        helper.startTimer(helper.crossOverTimer);
        final IntRef alterations = new IntRef(0);
        Population<G, C> initialPop = population.copy();

        IntStream.range(0, population.size()).forEach(i -> {
//            System.out.println("CROSS " + (i));
            final Phenotype<G, C> pt = initialPop.get(i);
            final Phenotype<G, C> maxNeighbor = getMaxNeighbor(population, i);
            if(random.nextDouble() >= probCrossover) return;
            if(pt.getFitness().compareTo(maxNeighbor.getFitness()) > 0) return;
            
            final Genotype<G> mgt = mutate(pt.getGenotype(), maxNeighbor.getGenotype(), alterations);
            if(alterations.value > 0) helper.updated.add(i);
            final Phenotype<G, C> mpt = pt.newInstance(mgt, generation);

            population.set(i, mpt);
        });  
        
        helper.stopTimer(helper.crossOverTimer);
        return alterations.value;     
    }
    
    private Genotype<G> mutate(
            final Genotype<G> gt1,
            final Genotype<G> gt2, 
            IntRef alterations){
        double r = random.nextDouble();
        
        List<G> genes;
        if(r < probStrat){
            genes = uniformCrossover(gt1, gt2);
        }else{
            genes = twoPointCrossover(gt1, gt2);
        }
        
        alterations.value += 1;
        return Genotype.of(gt1.getChromosome().newInstance( ISeq.of(genes)));
    }
    
    private List<G> uniformCrossover(Genotype<G> gt1, Genotype<G> gt2){
//        List<G> genes1 = gt1.getChromosome().stream().collect(Collectors.toList());
//        List<G> genes2 = gt2.getChromosome().stream().collect(Collectors.toList());
        int size = gt1.getNumberOfGenes();
        List<G> result = new ArrayList<G>(size);
        for(int i = 0; i < size; i++){
            if(random.nextBoolean()){
                result.add(gt1.get(0, i));
            }else{
                result.add(gt2.get(0, i));
            }
        }
        return result;
        
    }
    
    private List<G> twoPointCrossover(Genotype<G> gt1, Genotype<G> gt2){
        
        int size = gt1.getNumberOfGenes();
        List<G> result;
        List<G> other;
        
        int min = random.nextInt(size);
        int max = random.nextInt(size);
        if(min > max){
            int tmp = max;
            max = min;
            min = tmp;
        }
        
        if(random.nextBoolean()){
            result = new ArrayList<G>(gt1.getChromosome().toSeq().asList());
            other = new ArrayList<G>(gt2.getChromosome().toSeq().asList());
        }else{
            result = new ArrayList<G>(gt2.getChromosome().toSeq().asList());
            other = new ArrayList<G>(gt1.getChromosome().toSeq().asList());
        }
        
        for(int i = min; i < max; i++){
            result.set(i, other.get(i));
        }
        
        return result;
        
    }

}
