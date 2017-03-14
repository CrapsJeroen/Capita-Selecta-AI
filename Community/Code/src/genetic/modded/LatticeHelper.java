package genetic.modded;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.jenetics.Genotype;
import org.jenetics.IntegerGene;

import common.Graph;


public class LatticeHelper {
    public int steadyGenerations = 0;
    public double lastBestFitness = 0;
    private Map<Genotype<IntegerGene>, Map<Integer, Set<Integer>>> communityCache;
    final Function<Genotype<IntegerGene>, Map<Integer, Set<Integer>>> decodeFunction;
    public final Graph graph;
    
    public LatticeHelper(final Function<Genotype<IntegerGene>, Map<Integer, Set<Integer>>> decodeFunction,
                    Graph graph){
        communityCache = new HashMap<Genotype<IntegerGene>, Map<Integer,Set<Integer>>>();
        this.decodeFunction = decodeFunction;
        this.graph = graph;
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

}
