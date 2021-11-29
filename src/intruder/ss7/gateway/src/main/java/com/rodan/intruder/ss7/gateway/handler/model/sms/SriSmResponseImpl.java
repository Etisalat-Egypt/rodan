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

package com.rodan.intruder.ss7.gateway.handler.model.sms;

import com.rodan.intruder.ss7.entities.dialog.Ss7MapDialog;
import com.rodan.intruder.ss7.entities.event.model.sms.SriSmResponse;
import com.rodan.intruder.ss7.gateway.dialog.Ss7MapDialogImpl;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.mobicents.protocols.ss7.map.api.MAPDialog;

@Getter @ToString(callSuper = true)
public class SriSmResponseImpl extends SriSmResponse {
    private Ss7MapDialogImpl ss7Dialog;
    private long invokeId; // TODO SS7: change to Long in all classes

    @Builder
    public SriSmResponseImpl(String imsi, String hlrGt, String vmsc, String smsHomeRouting, MAPDialog mapDialog,
                             long invokeId) {
        super(imsi, hlrGt, vmsc, smsHomeRouting);
        this.ss7Dialog = Ss7MapDialogImpl.builder().jss7Dialog(mapDialog).build();
        this.invokeId = invokeId;
    }

    @Override
    public Ss7MapDialog getDialog() {
        return ss7Dialog;
    }

    @Override
    public long getInvokeId() {
        return invokeId;
    }
}
