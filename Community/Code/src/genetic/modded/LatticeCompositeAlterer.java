package genetic.modded;


/*
 * Java Genetic Algorithm Library (@__identifier__@).
 * Copyright (c) @__year__@ Franz Wilhelmstötter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author:
 *    Franz Wilhelmstötter (franz.wilhelmstoetter@gmx.at)
 */

import static java.lang.String.format;
import static org.jenetics.internal.util.Equality.eq;
import static org.jenetics.util.ISeq.toISeq;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jenetics.AbstractAlterer;
import org.jenetics.Gene;
import org.jenetics.Genotype;
import org.jenetics.Population;
import org.jenetics.internal.util.Hash;
import org.jenetics.util.ISeq;
import org.jenetics.util.Seq;

/**
 * Combines several alterers to one.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @since 1.0
 * @version 3.0
 */
public final class LatticeCompositeAlterer<
    G extends Gene<?, G>,
    C extends Comparable<? super C>
>
    extends LatticeAlterer<G, C>
{

    private final ISeq<LatticeAlterer<G, C>> _alterers;

    /**
     * Combine the given alterers.
     *
     * @param alterers the alterers to combine.
     * @throws NullPointerException if one of the alterers is {@code null}.
     */
    public LatticeCompositeAlterer(final Seq<LatticeAlterer<G, C>> alterers) {
        super(1.0, 0, null);
        _alterers = normalize(alterers);
    }

    private static <G extends Gene<?, G>, C extends Comparable<? super C>>
    ISeq<LatticeAlterer<G, C>> normalize(final Seq<LatticeAlterer<G, C>> alterers) {
        final Function<LatticeAlterer<G, C>, Stream<LatticeAlterer<G, C>>> mapper =
            a -> a instanceof LatticeCompositeAlterer<?, ?>
                ? ((LatticeCompositeAlterer<G, C>)a).getAlterers().stream()
                : Stream.of(a);

        return alterers.stream()
            .flatMap(mapper)
            .collect(toISeq());
    }

    @Override
    public int alter(final Population<G, C> population, final long generation,
            Function<Genotype<G>, Map<Integer, Set<Integer>>> communityCache) {
        return _alterers.stream()
            .mapToInt(a -> a.alter(population, generation, communityCache))
            .sum();
    }

    /**
     * Return the alterers this alterer consists of. The returned array is sealed
     * and cannot be changed.
     *
     * @return the alterers this alterer consists of.
     */
    public ISeq<LatticeAlterer<G, C>> getAlterers() {
        return _alterers;
    }

    @Override
    public int hashCode() {
        return Hash.of(getClass()).and(_alterers).value();
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof LatticeCompositeAlterer &&
            eq(((LatticeCompositeAlterer)obj)._alterers, _alterers);
    }

    @Override
    public String toString() {
        return format(
            "%s:\n%s", getClass().getSimpleName(),
            _alterers.stream()
                .map(a -> "   - " + a)
                .collect(Collectors.joining("\n"))
        );
    }

    /**
     * Combine the given alterers.
     *
     * @param <G> the gene type
     * @param <C> the fitness function result type
     * @param alterers the alterers to combine.
     * @return a new alterer which consists of the given one
     * @throws NullPointerException if one of the alterers is {@code null}.
     */
    @SafeVarargs
    public static <G extends Gene<?, G>, C extends Comparable<? super C>>
    LatticeCompositeAlterer<G, C> of(final LatticeAlterer<G, C>... alterers) {
        return new LatticeCompositeAlterer<>(ISeq.of(alterers));
    }

    /**
     * Joins the given alterer and returns a new CompositeAlterer object. If one
     * of the given alterers is a CompositeAlterer the sub alterers of it are
     * unpacked and appended to the newly created CompositeAlterer.
     *
     * @param <T> the gene type of the alterers.
     *
     * @param <C> the fitness function result type
     * @param a1 the first alterer.
     * @param a2 the second alterer.
     * @return a new CompositeAlterer object.
     * @throws NullPointerException if one of the given alterer is {@code null}.
     */
    public static <T extends Gene<?, T>, C extends Comparable<? super C>>
    LatticeCompositeAlterer<T, C> join(
        final LatticeAlterer<T, C> a1,
        final LatticeAlterer<T, C> a2
    ) {
        return LatticeCompositeAlterer.of(a1, a2);
    }

}