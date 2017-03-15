package genetic;

import genetic.modded.LatticeAlterer;
import genetic.modded.LatticeHelper;

import org.jenetics.Gene;
import org.jenetics.Population;


public class InertLatticeAlterer<G extends Gene<Integer, G>, C extends Comparable<? super C>> extends LatticeAlterer<G,C>{

    public InertLatticeAlterer(final int size, LatticeHelper<G,C> helper) {
        super(size, helper);
    }

    @Override
    public int alter(Population<G, C> population, final long generation) {
        return 0;
    }


}
