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

import java.util.ArrayList;
import java.util.List;

import net.ssehub.kernel_haven.pss_divergence_detector.divergences.Divergence;
import net.ssehub.kernel_haven.util.io.ITableRow;
import net.ssehub.kernel_haven.util.null_checks.NonNull;
import net.ssehub.kernel_haven.util.null_checks.Nullable;

/**
 * This class represents an abstract correction for unintended {@link Divergence}. It provides the common attributes and
 * methods for specific corrections (subclasses) as well as the required methods for saving corrections as a KernelHaven
 * result.
 * 
 * @author Christian Kröher
 *
 */
public abstract class Correction implements ITableRow {
    
    /**
     * The {@link Divergence} to be corrected by this correction.
     */
    protected @NonNull Divergence divergence;
    
    /**
     * Creates an {@link Correction} instance.
     * 
     * @param divergence the {@link Divergence} to be corrected by this correction
     */
    public Correction(@NonNull Divergence divergence) {
        this.divergence = divergence;
    }
    
    /**
     * Returns a textual description of this correction, e.g., how to correct the targeted divergence.
     * 
     * @return the description of this correction
     */
    public abstract @NonNull String getDescription();
    
    @Override
    public @Nullable Object @NonNull [] getHeader() {
        // For writing the Excel-sheet headers: extend divergence table by "Correction" column
        String correctionHeader = "Correction";
        Object[] headers = {correctionHeader};
        if (divergence != null) {
            headers = extend(divergence.getHeader(), correctionHeader);
        }
        return headers;
    }

    @Override
    public @Nullable Object @NonNull [] getContent() {
        // For writing each correction as an extension to the divergence row to the Excel-sheet
        String correctionContent = getDescription();
        Object[] content = {correctionContent};
        if (divergence != null) {
            content = extend(divergence.getContent(), correctionContent);
        }
        return content;
    }
    
    /**
     * Converts the given string containing whitespace-separated elements involved in a {@link Divergence} into a string
     * appropriate for including it into a correction description. The result will be a formatted listing, e.g.:
     * <li>
     * <ul>Input parameter value = "VariableA VariableB VariableC"</ul>
     * <ul>Return value = ""VariableA", "VariableB", and "VariableC""</ul>
     * <li>
     * 
     * @param involvedElements the string containing whitespace-separated elements involved in a {@link Divergence}
     * @return a string containing the involved elements properly formatted for a correction description; can be
     *         <i>empty</i> if the given string does not contain any characters
     */
    protected @NonNull String toDescriptionString(@NonNull String involvedElements) {
        StringBuilder descriptionStringBuilder = new StringBuilder("");
        if (!involvedElements.isEmpty()) {            
            String[] splittedInvolvedElements = involvedElements.split("\\s+");
            int involvedElementsNumber = splittedInvolvedElements.length;
            if (involvedElementsNumber > 0) {
                descriptionStringBuilder.append("\"" + splittedInvolvedElements[0] + "\"");
                if (involvedElementsNumber == 2) {
                    // Exactly 2 elements: return ""Elem1" and "Elem2""
                    descriptionStringBuilder.append(" and ");
                    descriptionStringBuilder.append("\"" + splittedInvolvedElements[1] + "\"");
                } else if (involvedElementsNumber > 2) {
                    // More than 2 elements: return ""Elem1, Elem2, [...], and "ElemX""
                    for (int i = 1; i < involvedElementsNumber - 1; i++) {
                        descriptionStringBuilder.append(", ");
                        descriptionStringBuilder.append(splittedInvolvedElements[i]);
                    }
                    descriptionStringBuilder.append(", and ");
                    descriptionStringBuilder.append("\"" + splittedInvolvedElements[involvedElementsNumber - 1] + "\"");
                }
            }
        }
        return descriptionStringBuilder.toString();
    }
    
    /**
     * Extends the given array by the given object.
     * 
     * @param extensible the array to be extended by the extension
     * @param extension the extension to be added to the extensible
     * @return an array, which contains all elements of the given array and the extension
     */
    private @NonNull Object[] extend(Object[] extensible, Object extension) {
        List<Object> extended = new ArrayList<Object>();
        if (extensible != null) {
            for (int i = 0; i < extensible.length; i++) {
                extended.add(extensible[i]);
            }
        }
        extended.add(extension);
        return extended.toArray();
    }
}
