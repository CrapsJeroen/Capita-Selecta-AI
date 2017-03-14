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

import org.jenetics.Gene;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.engine.EvolutionStart;


public final class CustomEvolutionResult
{
    /**
     * Return the next evolution start object with the current population and
     * the incremented generation.
     *
     * @return the next evolution start object
     */
    static <
    G extends Gene<?, G>,
    C extends Comparable<? super C>
> EvolutionStart<G, C> next(EvolutionResult<G, C> result) {
        return EvolutionStart.of(result.getPopulation(), result.getGeneration() + 1);
    }

}