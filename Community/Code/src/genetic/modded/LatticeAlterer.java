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


public abstract class LatticeAlterer<G extends Gene<?,G>,C extends Comparable<? super C>> extends AbstractAlterer<G, C> {

    final int latticeWidth;
    final int latticeHeight;
    final Graph graph;
    protected final Random random = RandomRegistry.getRandom();
    
    protected LatticeAlterer(double probability, int latticeWidth, int latticeHeight, Graph graph) {
        super(probability);
        this.latticeWidth = latticeWidth;
        this.latticeHeight = latticeHeight;
        this.graph = graph;
    }
    
    protected LatticeAlterer(double probability, int latticeSize, Graph graph) {
        this(probability, latticeSize, latticeSize, graph);
    }
    
    protected LatticeAlterer(int latticeWidth, int latticeHeight, Graph graph) {
        this(0, latticeWidth, latticeHeight, graph);
    }
    
    protected LatticeAlterer(int latticeSize, Graph graph) {
        this(0, latticeSize, latticeSize, graph);
    }
    
    Set<Phenotype<G, C>> getNeighbors(Population<G, C> pop, int index){
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
    
    protected boolean areInSameCommunity(final IntegerGene gene1, final IntegerGene gene2, final Map<Integer, Set<Integer>> communities){
        return areInSameCommunity(gene1.getAllele(), gene2.getAllele(), communities);
    }
    
    protected boolean areInSameCommunity(final int gene1, final int gene2, final Map<Integer, Set<Integer>> communities){
        return communities.get(gene1).contains(gene2);
    }
    protected Set<Integer> getAlleles(int index){
        return graph.getVertices().get(index).getNeighboursSet().stream()
                .map(v -> graph.getVertexIndexById(v.getId()))
                .collect(Collectors.toSet());
    }
    
    protected Set<Integer> getAllelesInOtherCommunity(int index, final Map<Integer, Set<Integer>> communities){
        return getAlleles(index).stream()
                .filter(a -> !areInSameCommunity(index, a, communities))
                .collect(Collectors.toSet());
    }
    
    public abstract int alter(Population<G, C> population, final long generation, final Function<Genotype<G>, Map<Integer, Set<Integer>>> communityCache);
    
    @Override
    public int alter(Population<G, C> population, final long generation){
        throw new NotImplementedException();
    }
    
    /**
     * Combine the given alterers.
     *
     * @param <G> the gene type
     * @param <C> the fitness function result type
     * @param alterers the alterers to combine.
     * @return a new alterer which consists of the given one
     * @throws NullPointerException if one of the alterers is {@code null}.
     */
    @SafeVarargs
    public static <G extends Gene<?, G>, C extends Comparable<? super C>>
    LatticeAlterer<G, C> of(final LatticeAlterer<G, C>... alterers) {
        return alterers.length == 0
            ? null
            : alterers.length == 1
                ? alterers[0]
                : new LatticeCompositeAlterer<>(ISeq.of(alterers));
    }
    
    /**
     * Returns a composed alterer that first applies the {@code before} alterer
     * to its input, and then applies {@code this} alterer to the result.
     *
     * @param before the alterer to apply first
     * @return the new composed alterer
     */
    public LatticeAlterer<G, C> compose(final LatticeAlterer<G, C> before) {
        return of(before, this);
    }

    /**
     * Returns a composed alterer that applies the {@code this} alterer
     * to its input, and then applies the {@code after} alterer to the result.
     *
     * @param after the alterer to apply first
     * @return the new composed alterer
     */
    public LatticeAlterer<G, C> andThen(final LatticeAlterer<G, C> after) {
        return of(this, after);
    }

}
