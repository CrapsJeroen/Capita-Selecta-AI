package genetic;


import genetic.modded.LatticeAlterer;
import genetic.modded.LatticeEngine;
import genetic.modded.LatticeHelper;

import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.jenetics.Gene;
import org.jenetics.Genotype;
import org.jenetics.IntegerChromosome;
import org.jenetics.IntegerGene;
import org.jenetics.Phenotype;
import org.jenetics.Population;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.engine.EvolutionStatistics;
import org.jenetics.engine.limit;
import org.jenetics.internal.util.IntRef;
import org.jenetics.stat.DoubleMomentStatistics;
import org.jenetics.util.Factory;


public class SelfLearnOperator<G extends Gene<Integer, G>, C extends Comparable<? super C>> extends LatticeAlterer<G,C> {

    private final int          size;
    public static final int    MAX_STEADY_GENS = 50;
    public static final double PROB_MUTATE     = 0.02;
    private final double PROB_SPLIT_MERGE_STRAT = 0.5;
    

    protected SelfLearnOperator(int size, int latticeWidth, int latticeHeight,
            LatticeHelper<G,C> helper) {
        super(0, latticeWidth, latticeHeight, helper);
        this.size = size;
    }

    protected SelfLearnOperator(int size, int latticeSize,
            LatticeHelper<G,C> helper) {
        this(size, latticeSize, latticeSize, helper);
    }

    @Override
    public int alter(final Population<G, C> population,
            final long generation) {
        final Population<G, C> newPop = helper.evaluate(population);
        PriorityQueue<Phenotype<G, C>> queue = new PriorityQueue<Phenotype<G,C>>(newPop);
        IntStream.range(0, size*size).mapToObj(i -> queue.poll()).forEach(pt -> {
//            System.out.println(pt.getFitness());
            int index = population.indexOf(pt);
            if(!helper.updated.contains(index)) return;
            Genotype<G> mgt = selfLearn(population, index);
            population.set(index, pt.newInstance(mgt));
        });
        
        return size * size;
        
    }
    

    private Genotype<G> selfLearn(final Population<G, C> population, final int index) {
        final IntRef initCounter = new IntRef();
        final SelfLearnInitialMutator<G, C> mutator = new SelfLearnInitialMutator<G, C>(PROB_MUTATE, size, this.helper);
        
        final Factory<Genotype<G>> ENCODING = () -> {
            int i;
            synchronized(initCounter){
                i = initCounter.value;
                initCounter.value++;
            }
            if(i == 0) return population.get(index).getGenotype();
            Genotype<G> mutated = mutator.getSingleMutated(population, index);
            return mutated;
        };

        final LatticeHelper<G,C> newHelper = new LatticeHelper<G,C>(helper.initialFitness,
                helper.decodeFunction,
                helper.graph,
                MAX_STEADY_GENS,
                helper.fitnessFunction);
        final LatticeEngine<G, C> engine = LatticeEngine
                .builder(
                        helper.fitnessFunction,
                        newHelper,
                        ENCODING
                        )
                .populationSize(size * size)
                .alterers(
                        new SplitMergeOperator<G, C>(PROB_SPLIT_MERGE_STRAT,
                                size, newHelper)
                        )
                .build();

        EvolutionResult<G, C> result = engine.stream()
                .limit(limit.bySteadyFitness(MAX_STEADY_GENS))
                .collect(EvolutionResult.toBestEvolutionResult());
        return result.getBestPhenotype().getGenotype();

    }

}
