package genetic;


import genetic.modded.LatticeAlterer;
import genetic.modded.LatticeEngine;
import genetic.modded.LatticeHelper;

import org.jenetics.Genotype;
import org.jenetics.IntegerChromosome;
import org.jenetics.IntegerGene;
import org.jenetics.Population;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.engine.EvolutionStatistics;
import org.jenetics.engine.limit;
import org.jenetics.stat.DoubleMomentStatistics;


public class SelfLearnOperator extends LatticeAlterer {

    private final int          size;
    public static final int    MAX_STEADY_GENS = 50;
    public static final double PROB_MUTATE     = 0.02;
    private final double PROB_SPLIT_MERGE_STRAT = 0.5;

    protected SelfLearnOperator(int size, int latticeWidth, int latticeHeight,
            LatticeHelper<Double> helper) {
        super(0, latticeWidth, latticeHeight, helper);
        this.size = size;
    }

    protected SelfLearnOperator(int size, int latticeSize,
            LatticeHelper<Double> helper) {
        this(size, latticeSize, latticeSize, helper);
    }

    @Override
    public int alter(Population<IntegerGene, Double> population,
            final long generation) {
        // TODO HOOK THIS UP
        return 0;
    }
    

    private Genotype<IntegerGene> selfLearn(Genotype<IntegerGene> gt) {
        final LatticeHelper<Double> newHelper = new LatticeHelper<Double>(0.0,
                CommunityAlgorithm::decodePartitionMap,
                helper.graph,
                MAX_STEADY_GENS,
                helper.fitnessFunction);
        
        final LatticeEngine<IntegerGene, Double> engine = LatticeEngine
                .builder(
                        helper.fitnessFunction,
                        newHelper,
                        IntegerChromosome.of(0, helper.graph.getVertices().size() - 1,
                                helper.graph.getVertices().size())
                        )
                .populationSize(size * size)
                .alterers(
                        new SelfLearnInitialSplitMergeOperator(PROB_MUTATE, size, newHelper, gt),
                        new SplitMergeOperator(PROB_SPLIT_MERGE_STRAT,
                                size, newHelper)
                        )
                .build();

        EvolutionStatistics<Double, DoubleMomentStatistics> statistics =
                EvolutionStatistics.ofNumber();

        EvolutionResult<IntegerGene, Double> result = engine.stream()
                .limit(limit.bySteadyFitness(MAX_STEADY_GENS))
                .peek(statistics)
                .collect(EvolutionResult.toBestEvolutionResult());
        return result.getBestPhenotype().getGenotype();

    }

}
