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

package com.rodan.intruder.ss7.entities.payload.oam;

import com.rodan.intruder.ss7.entities.payload.Ss7Payload;
import com.rodan.library.model.Constants;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter @ToString(callSuper = true)
public class SendImsiPayload extends Ss7Payload {
    private String msisdn;
    private String targetHlrGt;
    private String abuseOpcodeTag;
    private String malformedAcn;
    private String mapVersion;

    @Builder
    public SendImsiPayload(String localGt, String msisdn, String targetHlrGt,
                           String abuseOpcodeTag, String malformedAcn, String mapVersion) {
        super(localGt, Constants.SCCP_VLR_SSN, Constants.SCCP_HLR_SSN);
        this.msisdn = msisdn;
        this.targetHlrGt = targetHlrGt;
        this.abuseOpcodeTag = abuseOpcodeTag;
        this.malformedAcn = malformedAcn;
        this.mapVersion = mapVersion;
    }

    @Override
    public boolean isAbuseOpcodeTagForBypass() {
        return "Yes".equalsIgnoreCase(abuseOpcodeTag);
    }

    @Override
    public boolean isMalformedAcnForBypass() {
        return "Yes".equalsIgnoreCase(malformedAcn);
    }

    @Override
    public String getPayloadName() {
        return Constants.SIMSI_PAYLOAD_NAME;
    }
}
