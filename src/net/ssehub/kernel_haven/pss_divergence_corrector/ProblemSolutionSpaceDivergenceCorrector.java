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
package net.ssehub.kernel_haven.pss_divergence_corrector;

import net.ssehub.kernel_haven.analysis.AnalysisComponent;
import net.ssehub.kernel_haven.config.Configuration;
import net.ssehub.kernel_haven.pss_divergence_corrector.corrections.Correction;
import net.ssehub.kernel_haven.pss_divergence_corrector.corrections.CorrectionProvider;
import net.ssehub.kernel_haven.pss_divergence_detector.divergences.Divergence;
import net.ssehub.kernel_haven.util.null_checks.NonNull;

/**
 * This class provides {@link Correction}s for unintended {@link Divergence}s between problem and solution space
 * artifacts.
 * 
 * @author Christian Kröher
 *
 */
public class ProblemSolutionSpaceDivergenceCorrector extends AnalysisComponent<Correction> {
    
    /**
     * The {@link AnalysisComponent} providing the set of detected {@link Divergence}s.
     */
    private @NonNull AnalysisComponent<Divergence> pssDivergenceDetector;
    
    /**
     * The number of received {@link Divergence} for which possible {@link Correction}s shall be provided. The purpose
     * of this attributed is to log the number of received divergences.
     */
    private int receivedDivergencesCounter;
    
    /**
     * The number of provided {@link Correction}s for the received {@link Divergence}s. The purpose of this attribute
     * is to log the number of received divergences.
     */
    private int providedCorrectionsCounter;

    /**
     * Creates an {@link ProblemSolutionSpaceDivergenceCorrector} instance.
     * 
     * @param config the globale {@link Configuration}
     * @param pssDivergenceDetector the {@link AnalysisComponent} providing the {@link Divergence}s
     */
    public ProblemSolutionSpaceDivergenceCorrector(@NonNull Configuration config,
            @NonNull AnalysisComponent<Divergence> pssDivergenceDetector) {
        super(config);
        this.pssDivergenceDetector = pssDivergenceDetector;
        receivedDivergencesCounter = 0;
        providedCorrectionsCounter = 0;
    }

    @Override
    protected void execute() {
        if (pssDivergenceDetector != null) {
            Divergence receivedDivergence;
            Correction divergenceCorrection;
            while ((receivedDivergence = pssDivergenceDetector.getNextResult()) != null) {
                divergenceCorrection = CorrectionProvider.getCorrection(receivedDivergence);
                if (divergenceCorrection != null) {
                    addResult(divergenceCorrection);
                    providedCorrectionsCounter++;
                }
                receivedDivergencesCounter++;
            }
        } else {
            LOGGER.logWarning2("No divergence detector specified - no divergence correction possible");
        }
        LOGGER.logInfo2(providedCorrectionsCounter + " corrections for " + receivedDivergencesCounter 
                + " received divergences provided");
    }

    @Override
    public @NonNull String getResultName() {
        return "PSS_Corrections";
    }

}
