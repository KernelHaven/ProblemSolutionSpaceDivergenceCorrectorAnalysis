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
import net.ssehub.kernel_haven.util.null_checks.NonNull;

/**
 * This class provides a specific {@link Correction} for {@link UndefinedVariableDivergence}s.
 *  
 * @author Christian Kröher
 *
 */
public class UndefinedVariableCorrection extends Correction {

    /**
     * Creates an {@link UndefinedVariableCorrection} instance.
     * 
     * @param divergence the {@link Divergence} to be corrected by this correction
     */
    public UndefinedVariableCorrection(@NonNull Divergence divergence) {
        super(divergence);
    }

    @Override
    public @NonNull String getDescription() {
        StringBuilder descriptionBuilder = new StringBuilder();
        String involvedVariablesDescription = toDescriptionString(divergence.getInvolvedVariablesString());
        String involvedSourceFilesDescription = toDescriptionString(divergence.getInvolvedSourceFilesString());
        String involvedCodeElementsDescription = toDescriptionString(divergence.getInvolvedCodeElementsString());
        
        // Correction in the variability model
        descriptionBuilder.append("Define ");
        descriptionBuilder.append(involvedVariablesDescription);
        descriptionBuilder.append(" in the variability model");
        // Correction in the build artifacts (if involved)
        if (!involvedSourceFilesDescription.isEmpty()) {
            descriptionBuilder.append("\n");
            descriptionBuilder.append("OR");
            descriptionBuilder.append("\n");
            descriptionBuilder.append(getBuildCorrectionDescription(involvedVariablesDescription,
                    involvedSourceFilesDescription));
        }
        // Correction in the code artifacts (if involved)
        if (!involvedCodeElementsDescription.isEmpty()) {
            descriptionBuilder.append("\n");
            descriptionBuilder.append("OR");
            descriptionBuilder.append("\n");
            descriptionBuilder.append(getCodeCorrectionDescription(involvedVariablesDescription,
                    involvedCodeElementsDescription));
        }
        
        return descriptionBuilder.toString();
    }
    
    /**
     * Returns a textual description of how to correct the build artifacts, if they reference the undefined variable.
     * 
     * @param involvedVariablesDescription the string containing the variables description as provided by
     *        {@link #toDescriptionString(String)} called with {@link Divergence#getInvolvedVariablesString()}
     * @param involvedSourceFilesDescription the string containing the source files description as provided by
     *        {@link #toDescriptionString(String)} called with {@link Divergence#getInvolvedSourceFilesString()}
     * @return a string containing a textual description of a build artifacts correction; can be <i>empty</i> if no
     *         build artifacts are involved in the divergence.
     */
    private @NonNull String getBuildCorrectionDescription(String involvedVariablesDescription,
            String involvedSourceFilesDescription) {
        /*
         * TODO presence conditions in build artifacts are not represented in divergences due to missing information
         * provided by the extractor. It would be nice, if we could point exactly to the location in the build artifacts
         * where the references to the undefined variable exist.
         */
        StringBuilder buildCorrectionStringBuilder = new StringBuilder();
        buildCorrectionStringBuilder.append("Remove references to ");
        buildCorrectionStringBuilder.append(involvedVariablesDescription);
        buildCorrectionStringBuilder.append(" in the build artifacts controlling the presence or absence of ");
        buildCorrectionStringBuilder.append(involvedSourceFilesDescription);               
        return buildCorrectionStringBuilder.toString();
    }
    
    /**
     * Returns a textual description of how to correct the code artifacts, if they reference the undefined variable.
     * 
     * @param involvedVariablesDescription the string containing the variables description as provided by
     *        {@link #toDescriptionString(String)} called with {@link Divergence#getInvolvedVariablesString()}
     * @param involvedCodeElementsDescription the string containing the source files description as provided by
     *        {@link #toDescriptionString(String)} called with {@link Divergence#getInvolvedSourceFilesString()}
     * @return a string containing a textual description of a code elements correction; can be <i>empty</i> if no code
     *         elements are involved in the divergence.
     */
    private @NonNull String getCodeCorrectionDescription(String involvedVariablesDescription,
            String involvedCodeElementsDescription) {
        StringBuilder codeCorrectionStringBuilder = new StringBuilder();
        codeCorrectionStringBuilder.append("Remove references to ");
        codeCorrectionStringBuilder.append(involvedVariablesDescription);
        codeCorrectionStringBuilder.append(" in the conditions controlling the presence or absence of the following"
                + " code elements: ");
        codeCorrectionStringBuilder.append(involvedCodeElementsDescription);               
        return codeCorrectionStringBuilder.toString();
    }

}
