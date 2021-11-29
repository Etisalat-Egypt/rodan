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

package com.rodan.library.model.config.node.config;

import com.rodan.library.model.config.FakeSubscriberInfo;
import com.rodan.library.model.config.TargetNetworkInfo;
import com.rodan.library.model.config.TargetSubscriberInfo;
import com.rodan.library.model.config.association.DiameterAssociationInfo;
import com.rodan.library.model.config.association.SepAssociationInfo;
import com.rodan.library.model.config.node.SctpMode;
import com.rodan.library.model.error.ValidationException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter @ToString @NoArgsConstructor
public class IntruderNodeConfig extends SepNodeConfig {
    private FakeSubscriberInfo fakeSubscriberInfo;
    private DiameterAssociationInfo diameterAssociationInfo;

    @Builder
    public IntruderNodeConfig(SctpMode sctpMode, SepAssociationInfo associationInfo, TargetNetworkInfo targetNetworkInfo,
                              TargetSubscriberInfo targetSubscriberInfo, FakeSubscriberInfo fakeSubscriberInfo,
                              DiameterAssociationInfo diameterAssociationInfo) {
        super(sctpMode, associationInfo, targetNetworkInfo, targetSubscriberInfo);
        this.fakeSubscriberInfo = fakeSubscriberInfo;
        this.diameterAssociationInfo = diameterAssociationInfo;
    }

    @Override
    public void validate() throws ValidationException {
        super.validate();
        if (fakeSubscriberInfo != null)
            fakeSubscriberInfo.validate();
        if (diameterAssociationInfo != null) {
            diameterAssociationInfo.validate();
        }
    }
}
