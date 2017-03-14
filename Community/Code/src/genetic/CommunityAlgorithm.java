package genetic;

import genetic.modded.LatticeEngine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.jenetics.Chromosome;
import org.jenetics.Genotype;
import org.jenetics.IntegerChromosome;
import org.jenetics.IntegerGene;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.engine.EvolutionStatistics;
import org.jenetics.stat.DoubleMomentStatistics;

import common.Graph;
import common.Vertex;



public class CommunityAlgorithm {
    
    public final Graph graph;
    public final int numEdges;
  private final double PROB_CROSSOVER = 0.5;
  private final double PROB_MUTATE = 0.5;
  private final double PROB_HYBRID_STRAT = 0.5;
  private final int MAX_STEADY_GENS = 10;
  private final int SL_SIZE = 3;
  private final int SL = SL_SIZE * SL_SIZE;
    public CommunityAlgorithm(Graph graph){
        this.graph = graph;
        

        // external edges
        int tmp = graph.getEdges().stream().map(edge -> edge.getWeight()).reduce(0, (count, current) -> count + current);
        
        // internal edges (within cliques)
        tmp += graph.getVertices().stream().map(v -> v.getInternalEdges().size()).reduce(0, (count, current) -> count + current);
        
        numEdges = tmp;
    }
    
    
    public static Map<Integer, Set<Integer>> decodePartitionMap(Genotype<IntegerGene> individual){
        Map<Integer, Set<Integer>> communityMap = new HashMap<Integer, Set<Integer>>();        
        Chromosome<IntegerGene> chrom = individual.getChromosome();

        // Iterate over every gene
        for(int i = 0; i < chrom.length(); i++){
            
            Integer value = i;
            
            // If we have already seen this gene, continue
            if(communityMap.containsKey(value)) continue;
                        
            // Start a new community from this gene
            Set<Integer> current = new HashSet<Integer>();
            current.add(value);
            
            // Keep following the links and add each node to the community until...
            while(true){
                Integer nextValue = chrom.getGene(value).intValue();
                
                // ... either we find that we are linked to another community
                if(communityMap.containsKey(nextValue)){
                    Set<Integer> community = communityMap.get(nextValue);
                    
                    // In that case: Add all members from the current community to the one that already exists
                    community.addAll(current);
                    
                    // Link all the members from the current community to the one that already exists
                    current.stream().forEach(val -> communityMap.put(val, community));
                    break;
                }
                
                // ... or we have found a loop and got back to a node that's already in the current community
                if(current.contains(nextValue)){
                    // In this case: We're done, link all the members to the community
                    current.stream().forEach(val -> communityMap.put(val, current));
                    break;
                }
                
                // The two cases above didn't happen? Add the node we found to the current community and follow the link
                current.add(nextValue);
                value = nextValue;
            }
            
            
        }
        return communityMap;
    }
    
    public static List<Set<Integer>> decodePartitions(Genotype<IntegerGene> individual){
        return decodePartitionMap(individual).values().stream().distinct().collect(Collectors.toList());
    }
    
    public Set<Vertex> intSetToVertexSet(Set<Integer> input){
        return input.stream().map(i -> graph.getVertices().get(i)).collect(Collectors.toSet());
    }
    
    
    // The fitness function is the modularity measure
    public double fitness(final Genotype<IntegerGene> gt){
        List<Set<Integer>> communities = decodePartitions(gt);
        return fitness(communities);
    }
    
    // The fitness function is the modularity measure
    public double fitness(List<Set<Integer>> communities){
        double result = 0;
        for(Set<Integer> community : communities){
            Set<Vertex> vertexCommunity = intSetToVertexSet(community);
            
            double edgesInCommunity = vertexCommunity.stream()
                    .map(v -> v.amountOfConnectionsTo(vertexCommunity))
                    .reduce(0, (count, current) -> count + current) / 2;
            
            double degreeInCommunity = vertexCommunity.stream()
                    .map(v -> v.degree())
                    .reduce(0, (count, current) -> count + current);
            
            double currentResult = (edgesInCommunity / numEdges) - Math.pow( 0.5 * degreeInCommunity / numEdges, 2);
            result += currentResult;
        }
        return result;
    }
    
    public List<Set<Vertex>> solve(int latticeSize, int generations) {
        final LatticeEngine<IntegerGene, Double> engine = LatticeEngine
            .builder(this::fitness, CommunityAlgorithm::decodePartitionMap, IntegerChromosome.of(0, graph.getVertices().size() - 1, graph.getVertices().size()))
            .populationSize(latticeSize * latticeSize)
            .alterers(new SplitMergeOperator(0.5, latticeSize, graph), 
                    new HybridNeighborhoodCrossover(PROB_CROSSOVER, PROB_HYBRID_STRAT, latticeSize, graph),
                    new AdaptiveMutator(PROB_MUTATE, latticeSize, graph),
                    new SelfLearnOperator(SL_SIZE, latticeSize, graph))
            .build();
        
        EvolutionStatistics<Double, DoubleMomentStatistics> statistics =
                EvolutionStatistics.ofNumber();
        
        EvolutionResult<IntegerGene, Double> result = engine.stream()
            .limit(generations)
            .peek(statistics)
            .collect(EvolutionResult.toBestEvolutionResult());

        System.out.println("Best Modularity:" + (result.getBestFitness()));
        return decodePartitions(result.getBestPhenotype().getGenotype())
                .stream()
                .map(s -> intSetToVertexSet(s))
                .collect(Collectors.toList());

    }
}
