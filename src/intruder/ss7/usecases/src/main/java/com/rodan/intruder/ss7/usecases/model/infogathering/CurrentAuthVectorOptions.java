/*
 * Etisalat Egypt, Open Source
 * Copyright 2021, Etisalat Egypt and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

/**
 * @author Ayman ElSherif
 */

package com.rodan.intruder.ss7.usecases.model.infogathering;

import com.rodan.intruder.ss7.usecases.model.Ss7ModuleOptions;
import com.rodan.library.model.Validator;
import com.rodan.library.model.annotation.Option;
import com.rodan.library.model.config.node.config.IntruderNodeConfig;
import com.rodan.library.model.error.ValidationException;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter @ToString
public class CurrentAuthVectorOptions extends Ss7ModuleOptions<IntruderNodeConfig> {
    @Option(name = "tmsi", description = "Target TMSI", mandatory = true)
    private String tmsi;

    @Option(name = "vlr", description = "Target VLR global title", mandatory = true)
    private String targetVlrGt;

    @Option(name = "av_num", description = "Number of required authentication vectors", mandatory = true)
    private String avNumber;

    @Option(name = "acv", description = "MAP app context version (2, 3)", mandatory = true)
    private String mapVersion;

    @Builder
    public CurrentAuthVectorOptions(IntruderNodeConfig nodeConfig, String tmsi, String targetVlrGt, String avNumber, String mapVersion) {
        super(nodeConfig);
        this.tmsi = Objects.requireNonNullElse(tmsi, "");
        this.targetVlrGt = Objects.requireNonNullElse(targetVlrGt, "");
        this.avNumber = Objects.requireNonNullElse(avNumber, "5");
        this.mapVersion = Objects.requireNonNullElse(mapVersion, "3");
    }

    @Override
    public String getSpoofSender() {
        return "No";
    }

    @Override
    public void validate() throws ValidationException {
        Validator.validateTmsi(tmsi);
        Validator.validateVlr(targetVlrGt);
        Validator.validateAuthVectorNumber(avNumber);
        Validator.validateMapVersion(mapVersion);
    }
}
