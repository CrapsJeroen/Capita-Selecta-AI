package genetic;

import genetic.modded.LatticeAlterer;

import java.util.ArrayList;
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
    public int alter(Population<IntegerGene, Double> population, final long generation, 
            final Function<Genotype<IntegerGene>, Map<Integer, Set<Integer>>> communityCache) {
        final IntRef alterations = new IntRef(0);
        Population<IntegerGene, Double> initialPop = population.copy();

        IntStream.range(0, population.size()).forEach(i -> {
            final Phenotype<IntegerGene, Double> pt = initialPop.get(i);
            final Phenotype<IntegerGene, Double> maxNeighbor = getMaxNeighbor(population, i);
            if(random.nextDouble() >= probCrossover) return;
            if(pt.getFitness() > maxNeighbor.getFitness()) return;
            
            final Genotype<IntegerGene> mgt = mutate(pt.getGenotype(), maxNeighbor.getGenotype(), alterations);

            final Phenotype<IntegerGene, Double> mpt = pt.newInstance(mgt, generation);

            population.set(i, mpt);
        });
        return alterations.value;     
    }
    
    private Genotype<IntegerGene> mutate(
            final Genotype<IntegerGene> gt1,
            final Genotype<IntegerGene> gt2, 
            IntRef alterations){
        double r = random.nextDouble();
        
        List<IntegerGene> genes;
        if(r < probStrat){
            genes = uniformCrossover(gt1, gt2);
        }else{
            genes = twoPointCrossover(gt1, gt2);
        }
        
        alterations.value += 1;
        return Genotype.of(gt1.getChromosome().newInstance( ISeq.of(genes)));
    }
    
    private List<IntegerGene> uniformCrossover(Genotype<IntegerGene> gt1, Genotype<IntegerGene> gt2){
        List<IntegerGene> genes1 = gt1.getChromosome().stream().collect(Collectors.toList());
        List<IntegerGene> genes2 = gt2.getChromosome().stream().collect(Collectors.toList());
        List<IntegerGene> result = new ArrayList<IntegerGene>();
        for(int i = 0; i < genes1.size(); i++){
            if(random.nextBoolean()){
                result.add(genes1.get(i));
            }else{
                result.add(genes2.get(i));
            }
        }
        return result;
        
    }
    
    private List<IntegerGene> twoPointCrossover(Genotype<IntegerGene> gt1, Genotype<IntegerGene> gt2){
        List<IntegerGene> genes1 = gt1.getChromosome().stream().collect(Collectors.toList());
        List<IntegerGene> genes2 = gt2.getChromosome().stream().collect(Collectors.toList());
        List<IntegerGene> result;
        List<IntegerGene> other;
        
        int min = random.nextInt(genes1.size());
        int max = random.nextInt(genes1.size());
        if(min > max){
            int tmp = max;
            max = min;
            min = tmp;
        }
        
        if(random.nextBoolean()){
            result = genes1;
            other = genes2;
        }else{
            result = genes2; 
            other = genes1;
        }
        
        for(int i = min; i < max; i++){
            result.set(i, other.get(i));
        }
        
        return result;
        
    }

}
