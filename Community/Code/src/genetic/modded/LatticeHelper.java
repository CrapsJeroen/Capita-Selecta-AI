package genetic.modded;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.jenetics.Gene;
import org.jenetics.Genotype;
import org.jenetics.Population;

import common.Graph;


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
}
