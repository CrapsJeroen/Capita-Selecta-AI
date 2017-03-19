package genetic.modded;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jenetics.Gene;
import org.jenetics.Genotype;
import org.jenetics.Population;

import common.Graph;
import common.Vertex;


public class LatticeHelper<G extends Gene<Integer, G>, C extends Comparable<? super C>> {
    private int steadyGenerations = 0;
    private C lastBestFitness;
    private Map<Genotype<G>, Map<Integer, Set<Integer>>> communityCache;
    public final Function<Genotype<G>, Map<Integer, Set<Integer>>> decodeFunction;
    private Function<Population<G, C>, Population<G, C>> evaluate = (pop -> pop);
    public final Function<Genotype<G>, C> fitnessFunction;
    public final Set<Integer> updated = new HashSet<Integer>();
    public final Graph graph;
    public final int maxSteadyGenerations;
    public final C initialFitness;
    public boolean master = false;
    public final Timer splitMergeTimer = Timer.of();
    public final Timer crossOverTimer = Timer.of();
    public final Timer mutateTimer = Timer.of();
    public final Timer selfLearnTimer = Timer.of();
    
    
    public LatticeHelper(C initialFitness, final Function<Genotype<G>, Map<Integer, Set<Integer>>> decodeFunction,
                    Graph graph, int maxSteadyGenerations, Function<Genotype<G>, C> fitnessFunction){
        communityCache = new HashMap<Genotype<G>, Map<Integer,Set<Integer>>>();
        this.initialFitness = initialFitness;
        this.lastBestFitness = initialFitness;
        this.decodeFunction = decodeFunction;
        this.graph = graph;
        this.maxSteadyGenerations = maxSteadyGenerations;
        this.fitnessFunction = fitnessFunction;
    }
    
    public Map<Integer, Set<Integer>> getCommunities(Genotype<G> gt){
        if(communityCache.containsKey(gt)) return communityCache.get(gt);
        Map<Integer, Set<Integer>> result = decodeFunction.apply(gt);
        communityCache.put(gt, result);
        return result;
    }
    
    public Map<Integer, Set<Integer>> decode(Genotype<G> gt){
        return decodeFunction.apply(gt);
    }
    
    public void setEvaluateFunction(Function<Population<G, C>, Population<G, C>> eval){
        this.evaluate = eval;
    }
    
    public Population<G, C> evaluate(Population<G, C> pop){
        return evaluate.apply(pop);
    }
    
    public void updateLastFitness(C fitness){
        if(fitness.compareTo(lastBestFitness) > 0){
            steadyGenerations = 0;
        }else{
            steadyGenerations++;
        }
        lastBestFitness = fitness;
    }
    
    public int getSteadyGenerations(){
        return steadyGenerations;
    }
    public int getMaxSteadyGenerations(){
        return maxSteadyGenerations;
    }
    
    public void startTimer(Timer timer){
        timer.start();
    }
    
    public void stopTimer(Timer timer){
        timer.stop();
    }
    
    public Duration getDuration(Timer timer){
        return timer.getTime();
    }
    
    public List<Integer> getAlleles(Integer index){
        return new ArrayList<Integer>(graph.getNeighborsIndexByIndex(index));
    }
    
    public List<Integer> getAllelesProportional(Integer index){
        List<Integer> result = new ArrayList<Integer>();
        Vertex current = graph.getVertices().get(index);
        for(Vertex vertex: current.getNeighbours()){
            for(int i = 0; i < current.getConnectionWeight(vertex); i++){
                result.add(graph.getVertexIndexById(vertex.getId()));
            }
        }
        
        return result;
    }
    
    public List<Integer> getAllelesInOtherCommunity(Integer index, final Map<Integer, Set<Integer>> communities){
        return getAlleles(index).stream()
                .filter(a -> !areInSameCommunity(index, a, communities))
                .collect(Collectors.toList());
    }
    
    public List<Integer> getAllelesInOtherCommunityProportional(Integer index, final Map<Integer, Set<Integer>> communities){
        return getAllelesProportional(index).stream()
                .filter(a -> !areInSameCommunity(index, a, communities))
                .collect(Collectors.toList());
    }
    
    public boolean areInSameCommunity(final G gene1, final G gene2, final Map<Integer, Set<Integer>> communities){
        return areInSameCommunity(gene1.getAllele(), gene2.getAllele(), communities);
    }
    
    public boolean areInSameCommunity(final Integer gene1, final Integer gene2, final Map<Integer, Set<Integer>> communities){
        return communities.get(gene1).contains(gene2);
    }
}
