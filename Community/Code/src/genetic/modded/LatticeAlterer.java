package genetic.modded;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.jenetics.AbstractAlterer;
import org.jenetics.Gene;
import org.jenetics.Phenotype;
import org.jenetics.Population;
import org.jenetics.util.RandomRegistry;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;


public abstract class LatticeAlterer<G extends Gene<Integer, G>, C extends Comparable<? super C>> extends AbstractAlterer<G, C> {

    final int latticeWidth;
    final int latticeHeight;
    protected final LatticeHelper<G,C> helper;
    protected final Random random = RandomRegistry.getRandom();
    
    protected LatticeAlterer(double probability, int latticeWidth, int latticeHeight, LatticeHelper<G,C> helper) {
        super(probability);
        this.latticeWidth = latticeWidth;
        this.latticeHeight = latticeHeight;
        this.helper = helper;
    }
    
    protected LatticeAlterer(double probability, int latticeSize, LatticeHelper<G,C> helper) {
        this(probability, latticeSize, latticeSize, helper);
    }
    
    protected LatticeAlterer(int latticeWidth, int latticeHeight, LatticeHelper<G,C> helper) {
        this(0, latticeWidth, latticeHeight, helper);
    }
    
    protected LatticeAlterer(int latticeSize, LatticeHelper<G,C> helper) {
        this(0, latticeSize, latticeSize, helper);
    }
    
    protected Set<Phenotype<G, C>> getNeighbors(Population<G, C> pop, int index){
        Set<Phenotype<G, C>> result = new HashSet<Phenotype<G, C>>();
        
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
    
    protected Phenotype<G, C> getMaxNeighbor(Population<G, C> pop, int index){
        Optional<Phenotype<G, C>> result = getNeighbors(pop, index).stream().max(Phenotype<G, C>::compareTo);
        
        if(result.isPresent()){
            return result.get();
        }else{
            return pop.get(index);
        }
    }
    
    protected boolean areInSameCommunity(final G gene1, final G gene2, final Map<Integer, Set<Integer>> communities){
        return areInSameCommunity(gene1.getAllele(), gene2.getAllele(), communities);
    }
    
    protected boolean areInSameCommunity(final Integer gene1, final Integer gene2, final Map<Integer, Set<Integer>> communities){
        return communities.get(gene1).contains(gene2);
    }
    
    protected Set<Integer> getAlleles(Integer index){
        return helper.graph.getNeighborsIndexByIndex(index);
    }
    
    protected Set<Integer> getAllelesInOtherCommunity(Integer index, final Map<Integer, Set<Integer>> communities){
        return getAlleles(index).stream()
                .filter(a -> !areInSameCommunity(index, a, communities))
                .collect(Collectors.toSet());
    }
        
    @Override
    public int alter(Population<G, C> population, final long generation){
        throw new NotImplementedException();
    }

}
