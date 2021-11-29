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

package com.rodan.intruder.ss7.entities.event.model.mobility;

import com.rodan.intruder.ss7.entities.event.model.MapMessage;
import com.rodan.intruder.ss7.entities.event.model.auth.AuthQuintuplet;
import com.rodan.intruder.ss7.entities.event.model.auth.AuthTriplet;
import com.rodan.intruder.ss7.entities.event.model.auth.EpcAuthVector;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter @ToString
public abstract class SaiResponse implements MapMessage {
    private List<com.rodan.intruder.ss7.entities.event.model.auth.AuthTriplet> tripletList;
    private List<com.rodan.intruder.ss7.entities.event.model.auth.AuthQuintuplet> quintupletList;
    private List<com.rodan.intruder.ss7.entities.event.model.auth.EpcAuthVector> epcAuthVectorList;

    public SaiResponse(List<AuthTriplet> tripletList, List<AuthQuintuplet> quintupletList,
                       List<EpcAuthVector> epcAuthVectorList) {
        this.tripletList = tripletList;
        this.quintupletList = quintupletList;
        this.epcAuthVectorList = epcAuthVectorList;
    }
}
