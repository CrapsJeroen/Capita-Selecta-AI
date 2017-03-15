package genetic;

import genetic.modded.LatticeAlterer;
import genetic.modded.LatticeHelper;

import org.jenetics.Genotype;
import org.jenetics.IntegerGene;
import org.jenetics.Population;


public class SelfLearnInitialSplitMergeOperator extends LatticeAlterer{
    
    boolean executed = false;
    final Genotype<IntegerGene> parent; 
    
    protected SelfLearnInitialSplitMergeOperator(double probability, int latticeWidth, int latticeHeight, LatticeHelper<Double> helper, Genotype<IntegerGene> parent) {
        super(probability, latticeWidth, latticeHeight, helper);
        this.parent = parent;
    }
    
    protected SelfLearnInitialSplitMergeOperator(double probability, int latticeSize, LatticeHelper<Double> helper, Genotype<IntegerGene> parent) {
        this(probability, latticeSize, latticeSize, helper, parent);
    }

    @Override
    public int alter(Population<IntegerGene, Double> population, final long generation) {
        if(executed) return 0;
        
        //TODO: figure out what needs to happen here!
//        final IntRef alterations = new IntRef(0);
//        Population<IntegerGene, Double> initialPop = population.copy();
//
//        IntStream.range(0, population.size()).forEach(i -> {
//            final Phenotype<IntegerGene, Double> pt = initialPop.get(i);
//            final Phenotype<IntegerGene, Double> maxNeighbor = getMaxNeighbor(population, i);
//            if(pt.getFitness() > maxNeighbor.getFitness()) return;
//            
//            final Genotype<IntegerGene> mgt = mutate(maxNeighbor.getGenotype(), alterations, helper.getCommunities(maxNeighbor.getGenotype()));
//
//            final Phenotype<IntegerGene, Double> mpt = pt.newInstance(mgt, generation);
//
//            population.set(i, mpt);
//        });
        
        executed = true;
        return 0;  
    }
    
//    private Genotype<IntegerGene> mutate(final Genotype<IntegerGene> max, 
//            IntRef alterations, Map<Integer, Set<Integer>> communities){
//        double r = random.nextDouble();
//        
//        int index = random.nextInt(max.getNumberOfGenes());
//        
//        Set<Integer> options;
//        if(r >= _probability){
//            options = getAllelesInOtherCommunity(index, communities);
//        }else{
//            options = getAlleles(index);
//        }
//        if(options.isEmpty())
//            return max;
//        
//        List<IntegerGene> genes = max.getChromosome().stream().collect(Collectors.toList());
//        genes.set(index, genes.get(0).newInstance(options.stream().findAny().get()));
//        alterations.value += 1;
//        return Genotype.of(max.getChromosome().newInstance( ISeq.of(genes)));
//    }

}
