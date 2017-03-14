package genetic;

import genetic.modded.LatticeAlterer;
import genetic.modded.LatticeHelper;

import org.jenetics.IntegerGene;
import org.jenetics.Population;


public class InertLatticeAlterer extends LatticeAlterer{

    public InertLatticeAlterer(final int size, LatticeHelper<Double> helper) {
        super(size, helper);
    }

    @Override
    public int alter(Population<IntegerGene, Double> population, final long generation) {
        return 0;
    }


}
