package genetic;

import genetic.modded.LatticeAlterer;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.jenetics.Gene;
import org.jenetics.Genotype;
import org.jenetics.Population;

import common.Graph;


public class InertLatticeAlterer<G extends Gene<?,G>,C extends Comparable<? super C>> extends LatticeAlterer<G, C>{

    public InertLatticeAlterer(final int size, Graph graph) {
        super(size, graph);
    }

    @Override
    public int alter(Population<G, C> population, final long generation, final Function<Genotype<G>, Map<Integer, Set<Integer>>> communityCache) {
        return 0;
    }


}
