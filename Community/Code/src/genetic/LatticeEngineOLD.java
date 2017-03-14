package genetic;
//package genetic;
//
//import genetic.modded.AlterResult;
//import genetic.modded.CustomExecutor;
//import genetic.modded.FilterResult;
//import genetic.modded.CustomResult;
//import genetic.modded.Timer;
//
//import java.util.List;
//import java.util.Set;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.Executor;
//import java.util.concurrent.ForkJoinPool;
//import java.util.function.Function;
//
//import org.jenetics.Genotype;
//import org.jenetics.IntegerGene;
//import org.jenetics.Phenotype;
//import org.jenetics.Population;
//import org.jenetics.engine.EvolutionDurations;
//import org.jenetics.engine.EvolutionResult;
//import org.jenetics.engine.EvolutionStart;
//import org.jenetics.engine.EvolutionStream;
//import org.jenetics.engine.limit;
//import org.jenetics.util.Factory;
//
//
//public final class LatticeEngine {    
//    private final Factory<Genotype<IntegerGene>> GTF;
//    private final Function<Genotype<IntegerGene>, Double> fitness;
//    private final double PROB_CROSSOVER = 0.5;
//    private final double PROB_MUTATE = 0.5;
//    private final double PROB_HYBRID_STRAT = 0.5;
//    private final int MAX_STEADY_GENS = 10;
//    private final int SL_SIZE = 3;
//    private final int SL = SL_SIZE * SL_SIZE;
//    
//    private final SplitMergeOperator split = new SplitMergeOperator(0.5);
//    
//    
//    private CustomExecutor _executor = new CustomExecutor(ForkJoinPool.commonPool());
//    
//    
//    public LatticeEngine(Factory<Genotype<IntegerGene>> GTF, Function<Genotype<IntegerGene>, Double> fitness){
//        this.GTF = GTF;
//        this.fitness = fitness;
//    }
//    
//    
//    private EvolutionStart<IntegerGene, Double> start(final int populationSize, final long generation){
//        final Population<IntegerGene, Double> population = GTF
//                .instances()
//                .map(gt -> Phenotype
//                        .of(gt, generation, fitness))
//                .limit(populationSize)
//                .collect(Population.toPopulation());
//        
//        return EvolutionStart.of(population, generation);
//    }
//    
//    private EvolutionResult<IntegerGene, Double> evolve(final EvolutionStart<IntegerGene, Double> start){
//        
//        final Population<IntegerGene, Double> startPopulation = start.getPopulation();
//        
//      final CompletableFuture<Population<IntegerGene, Double>> offspring =
//      _executor.async(() ->
//          split.alter(startPopulation, start.getGeneration())
//      );
//        // split/merge operator + update learning labels
//        
//        // hybrid neighborhood crossover with PROB_CROSSOVER and PROB_HYBRID_STRAT + update learning labels
//        
//        // ADAPTIVE MUTATION with PROB_MUTATE + update learning labels
//        
//        
//        // for SL best agents A:
//            // if learning(A)
//                // self learning operator on A
//        
//        // evaluate agents
//    }
//    
////    public EvolutionResult<G, C> evolve(final EvolutionStart<G, C> start) {
////        final Timer timer = Timer.of().start();
////
////        
////
////        // Initial evaluation of the population.
////        final Timer evaluateTimer = Timer.of(_clock).start();
////        evaluate(startPopulation);
////        evaluateTimer.stop();
////
////        // Select the offspring population.
////        final CompletableFuture<TimedResult<Population<G, C>>> offspring =
////            _executor.async(() ->
////                selectOffspring(startPopulation),
////                _clock
////            );
////
////        // Select the survivor population.
////        final CompletableFuture<TimedResult<Population<G, C>>> survivors =
////            _executor.async(() ->
////                selectSurvivors(startPopulation),
////                _clock
////            );
////
////        // Altering the offspring population.
////        final CompletableFuture<TimedResult<AlterResult<G, C>>> alteredOffspring =
////            _executor.thenApply(offspring, p ->
////                alter(p.result, start.getGeneration()),
////                _clock
////            );
////
////        // Filter and replace invalid and to old survivor individuals.
////        final CompletableFuture<TimedResult<FilterResult<G, C>>> filteredSurvivors =
////            _executor.thenApply(survivors, pop ->
////                filter(pop.result, start.getGeneration()),
////                _clock
////            );
////
////        // Filter and replace invalid and to old offspring individuals.
////        final CompletableFuture<TimedResult<FilterResult<G, C>>> filteredOffspring =
////            _executor.thenApply(alteredOffspring, pop ->
////                filter(pop.result.population, start.getGeneration()),
////                _clock
////            );
////
////        // Combining survivors and offspring to the new population.
////        final CompletableFuture<Population<G, C>> population =
////            filteredSurvivors.thenCombineAsync(filteredOffspring, (s, o) -> {
////                    final Population<G, C> pop = s.result.population;
////                    pop.addAll(o.result.population);
////                    return pop;
////                },
////                _executor.get()
////            );
////
////        // Evaluate the fitness-function and wait for result.
////        final Population<G, C> pop = population.join();
////        final TimedResult<Population<G, C>> result = TimedResult
////            .of(() -> evaluate(pop), _clock)
////            .get();
////
////
////        final EvolutionDurations durations = EvolutionDurations.of(
////            offspring.join().duration,
////            survivors.join().duration,
////            alteredOffspring.join().duration,
////            filteredOffspring.join().duration,
////            filteredSurvivors.join().duration,
////            result.duration.plus(evaluateTimer.getTime()),
////            timer.stop().getTime()
////        );
////
////        final int killCount =
////            filteredOffspring.join().result.killCount +
////            filteredSurvivors.join().result.killCount;
////
////        final int invalidCount =
////            filteredOffspring.join().result.invalidCount +
////            filteredSurvivors.join().result.invalidCount;
////
////        return EvolutionResult.of(
////            _optimize,
////            result.result,
////            start.getGeneration(),
////            durations,
////            killCount,
////            invalidCount,
////            alteredOffspring.join().result.alterCount
////        );
////    }
//    
//    public Genotype<IntegerGene> solve(int populationSize, int maxIterations){
//        final Genotype<IntegerGene> best = EvolutionStream
//                .of(() -> start(populationSize, 0), this::evolve)
//                .limit(limit.bySteadyFitness(MAX_STEADY_GENS))
//                .limit(maxIterations)
//                .collect(EvolutionResult.toBestGenotype());
//        return best;
//    }
//}
