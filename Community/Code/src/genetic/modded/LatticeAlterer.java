package genetic.modded;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jenetics.AbstractAlterer;
import org.jenetics.Gene;
import org.jenetics.Genotype;
import org.jenetics.IntegerGene;
import org.jenetics.Phenotype;
import org.jenetics.Population;
import org.jenetics.util.ISeq;
import org.jenetics.util.RandomRegistry;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import common.Graph;


public abstract class LatticeAlterer extends AbstractAlterer<IntegerGene, Double> {

    final int latticeWidth;
    final int latticeHeight;
    protected final LatticeHelper helper;
    protected final Random random = RandomRegistry.getRandom();
    
    protected LatticeAlterer(double probability, int latticeWidth, int latticeHeight, LatticeHelper helper) {
        super(probability);
        this.latticeWidth = latticeWidth;
        this.latticeHeight = latticeHeight;
        this.helper = helper;
    }
    
    protected LatticeAlterer(double probability, int latticeSize, LatticeHelper helper) {
        this(probability, latticeSize, latticeSize, helper);
    }
    
    protected LatticeAlterer(int latticeWidth, int latticeHeight, LatticeHelper helper) {
        this(0, latticeWidth, latticeHeight, helper);
    }
    
    protected LatticeAlterer(int latticeSize, LatticeHelper helper) {
        this(0, latticeSize, latticeSize, helper);
    }
    
    Set<Phenotype<IntegerGene, Double>> getNeighbors(Population<IntegerGene, Double> pop, int index){
        Set<Phenotype<IntegerGene, Double>> result = new HashSet<Phenotype<IntegerGene, Double>>();
        
        int startX = index % latticeWidth;
        int startY = index / latticeWidth;
        
        for(int i = -1; i <= 1; i++){
            for(int j = -1; j <= 1; j++){
                int newX = (latticeWidth + startX + i) % latticeWidth;
                int newY = (latticeHeight + startY + 1) % latticeHeight;
                int newIndex = latticeWidth * newY + newX;
                result.add(pop.get(newIndex));
            }
        }
        
        return result;
    }
    
    protected Phenotype<IntegerGene, Double> getMaxNeighbor(Population<IntegerGene, Double> pop, int index){
        Optional<Phenotype<IntegerGene, Double>> result = getNeighbors(pop, index).stream().max(Phenotype<IntegerGene, Double>::compareTo);
        
        if(result.isPresent()){
            return result.get();
        }else{
            return pop.get(index);
        }
    }
    
    protected boolean areInSameCommunity(final IntegerGene gene1, final IntegerGene gene2, final Map<Integer, Set<Integer>> communities){
        return areInSameCommunity(gene1.getAllele(), gene2.getAllele(), communities);
    }
    
    protected boolean areInSameCommunity(final int gene1, final int gene2, final Map<Integer, Set<Integer>> communities){
        return communities.get(gene1).contains(gene2);
    }
    
    protected Set<Integer> getAlleles(int index){
        return helper.graph.getVertices().get(index).getNeighboursSet().stream()
                .map(v -> helper.graph.getVertexIndexById(v.getId()))
                .collect(Collectors.toSet());
    }
    
    protected Set<Integer> getAllelesInOtherCommunity(int index, final Map<Integer, Set<Integer>> communities){
        return getAlleles(index).stream()
                .filter(a -> !areInSameCommunity(index, a, communities))
                .collect(Collectors.toSet());
    }
        
    @Override
    public int alter(Population<IntegerGene, Double> population, final long generation){
        throw new NotImplementedException();
    }

}
