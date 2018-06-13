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
import net.ssehub.kernel_haven.pss_divergence_detector.divergences.UnusedVariableDivergence;
import net.ssehub.kernel_haven.util.null_checks.NonNull;

/**
 * This class provides a specific {@link Correction} for {@link UnusedVariableDivergence}s.
 *  
 * @author Christian Kröher
 *
 */
public class UnusedVariableCorrection extends Correction {

    /**
     * Creates an {@link UnusedVariableCorrection} instance.
     * 
     * @param divergence the {@link Divergence} to be corrected by this correction
     */
    public UnusedVariableCorrection(@NonNull Divergence divergence) {
        super(divergence);
    }

    @Override
    public @NonNull String getDescription() {
        StringBuilder descriptionBuilder = new StringBuilder();
        String involvedVariablesDescription = toDescriptionString(divergence.getInvolvedVariablesString());
        
        // Correction in the variability model
        descriptionBuilder.append("Remove ");
        descriptionBuilder.append(involvedVariablesDescription);
        descriptionBuilder.append(" from the variability model");
        descriptionBuilder.append("\n");
        descriptionBuilder.append("OR");
        descriptionBuilder.append("\n");
        descriptionBuilder.append("Define at least one reference to ");
        descriptionBuilder.append(involvedVariablesDescription);
        descriptionBuilder.append(" as part of another variables constraint in the variability model");
        // Correction in the build or code artifacts
        descriptionBuilder.append("\n");
        descriptionBuilder.append("OR");
        descriptionBuilder.append("\n");
        descriptionBuilder.append("Define at least one reference to ");
        descriptionBuilder.append(involvedVariablesDescription);
        descriptionBuilder.append(" in a build or code artifact");
        
        return descriptionBuilder.toString();
    }

}
