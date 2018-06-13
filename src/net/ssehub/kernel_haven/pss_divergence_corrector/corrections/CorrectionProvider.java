/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE
 * file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package net.ssehub.kernel_haven.pss_divergence_corrector.corrections;

import net.ssehub.kernel_haven.pss_divergence_detector.divergences.Divergence;
import net.ssehub.kernel_haven.pss_divergence_detector.divergences.UndefinedVariableDivergence;
import net.ssehub.kernel_haven.pss_divergence_detector.divergences.UnusedVariableDivergence;
import net.ssehub.kernel_haven.util.null_checks.NonNull;

/**
 * This class provides the specific {@link Correction}s for identified {@link Divergence}s.
 *
 * @author Christian Kröher
 *
 */
public class CorrectionProvider {

    /**
     * The singleton instance of the {@link CorrectionProvider}.
     */
    private static @NonNull CorrectionProvider instance = new CorrectionProvider();
    
    /**
     * Creates a {@link CorrectionProvider} instance.
     */
    private CorrectionProvider() {}
    
    /**
     * Returns a {@link Correction} for the given {@link Divergence}.
     * 
     * @param divergence the {@link Divergence} to correct
     * @return a {@link Correction} or <code>null</code> if no correction for the given divergence is known
     */
    public static Correction getCorrection(Divergence divergence) {
        Correction correction = null;
        // TODO their should be a better and more flexible way of detecting and providing corrections
        // TODO their could also be more than one corrections for a single divergence and vice-versa
        if (divergence instanceof UnusedVariableDivergence) {
            correction = new UnusedVariableCorrection(divergence);
        } else if (divergence instanceof UndefinedVariableDivergence) {
            correction = new UndefinedVariableCorrection(divergence);
        }
        return correction;
    }

}
