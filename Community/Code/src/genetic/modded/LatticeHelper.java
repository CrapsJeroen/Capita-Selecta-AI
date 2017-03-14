package genetic.modded;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.jenetics.Genotype;
import org.jenetics.IntegerGene;

import common.Graph;


public class LatticeHelper<C extends Comparable<? super C>> {
    private int steadyGenerations = 0;
    private C lastBestFitness;
    private Map<Genotype<IntegerGene>, Map<Integer, Set<Integer>>> communityCache;
    final Function<Genotype<IntegerGene>, Map<Integer, Set<Integer>>> decodeFunction;
    public final Graph graph;
    public final int maxSteadyGenerations;
    
    public LatticeHelper(C initialFitness, final Function<Genotype<IntegerGene>, Map<Integer, Set<Integer>>> decodeFunction,
                    Graph graph, int maxSteadyGenerations){
        communityCache = new HashMap<Genotype<IntegerGene>, Map<Integer,Set<Integer>>>();
        this.lastBestFitness = initialFitness;
        this.decodeFunction = decodeFunction;
        this.graph = graph;
        this.maxSteadyGenerations = maxSteadyGenerations;
    }
    
    public Map<Integer, Set<Integer>> getCommunities(Genotype<IntegerGene> gt){
        if(communityCache.containsKey(gt)) return communityCache.get(gt);
        Map<Integer, Set<Integer>> result = decodeFunction.apply(gt);
        communityCache.put(gt, result);
        return result;
    }
    
    public Map<Integer, Set<Integer>> decode(Genotype<IntegerGene> gt){
        return decodeFunction.apply(gt);
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
}
