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

package com.rodan.connectivity.ss7.payloadwrapper.location;

import com.rodan.connectivity.ss7.adapter.MapAdapter;
import com.rodan.connectivity.ss7.adapter.SccpAdapter;
import com.rodan.connectivity.ss7.payloadwrapper.JSs7PayloadWrapper;
import com.rodan.connectivity.ss7.service.MapDialogGenerator;
import com.rodan.connectivity.ss7.service.MapLcsService;
import com.rodan.library.model.Constants;
import com.rodan.library.model.annotation.Payload;
import com.rodan.library.model.config.node.config.IntruderNodeConfig;
import com.rodan.library.model.config.node.config.LabNodeConfig;
import com.rodan.library.model.config.node.config.NodeConfig;
import com.rodan.library.model.error.ErrorCode;
import com.rodan.library.model.error.SystemException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.lsm.MAPDialogLsm;

@Payload(name = Constants.SRI_LCS_PAYLOAD_NAME)
@ToString(callSuper = true)
public class SriLcsPayloadWrapper extends JSs7PayloadWrapper<MapLcsService, MAPDialogLsm> {
    @Getter(AccessLevel.PRIVATE) private String msisdn;
    @Getter(AccessLevel.PRIVATE) private String imsi;
    @Getter(AccessLevel.PRIVATE) private String targetHlrGt;
    @Getter(AccessLevel.PRIVATE) private String gmlcNumber; // GMLC: Gateway Mobile Location Center
    @Getter(AccessLevel.PRIVATE) private String mapVersion;

    @Builder
    public SriLcsPayloadWrapper(String localGt, int localSsn, int remoteSsn, NodeConfig nodeConfig, 
                                SccpAdapter sccpAdapter, MapAdapter mapAdapter, 
                                MapDialogGenerator<MAPDialogLsm> dialogGenerator, String msisdn, String imsi, 
                                String targetHlrGt, String gmlcNumber, String mapVersion) {
        super(localGt, localSsn, remoteSsn, nodeConfig, sccpAdapter, mapAdapter, dialogGenerator);
        this.msisdn = msisdn;
        this.imsi = imsi;
        this.targetHlrGt = targetHlrGt;
        this.gmlcNumber = gmlcNumber;
        this.mapVersion = mapVersion;
    }

    @Override
    public MAPDialogLsm generateCarrier() throws SystemException {
        validate();

        var sccpParamFactory = getSccpAdapter().getParamFactory();
        var callingGt = sccpParamFactory.createGlobalTitle(getLocalGt(), TRANSLATION_TYPE,
                ISDN_TELEPHONY_INDICATOR, ENCODING_SCHEME, NATURE_OF_ADDRESS);
        var callingPc = Integer.valueOf(getNodeConfig().getSs7Association().getLocalNode().getPointCode());
        var callingParty = sccpParamFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                callingGt, callingPc, getLocalSsn());
        var calledGtStr = StringUtils.isBlank(getTargetHlrGt()) ? getMsisdn() :
                getTargetHlrGt();
        var calledGt = sccpParamFactory.createGlobalTitle(calledGtStr, TRANSLATION_TYPE, ISDN_TELEPHONY_INDICATOR,
                ENCODING_SCHEME, NATURE_OF_ADDRESS);
        var peerNode = (getNodeConfig() instanceof IntruderNodeConfig) ?
                ((IntruderNodeConfig) getNodeConfig()).getSs7Association().getPeerNode() :
                ((LabNodeConfig) getNodeConfig()).getSs7Association().getPeerNode();
        var calledPc = Integer.valueOf(peerNode.getPointCode());
        var calledParty = sccpParamFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, calledGt,
                calledPc, getRemoteSsn());

        return dialogGenerator.generate(callingParty, calledParty, getApplicationContext());
    }

    @Override
    public void addToCarrier(MAPDialogLsm dialog) throws SystemException {
        try {
            validate();

            var mapParamFactory = getMapAdapter().getParamFactory();
            ISDNAddressString msisdn = null;
            IMSI imsi = null;
            if (StringUtils.isNotBlank(getMsisdn())) {
                msisdn = mapParamFactory.createISDNAddressString(ADDRESS_NATURE, NumberingPlan.ISDN,
                        getMsisdn());
            } else {
                imsi = mapParamFactory.createIMSI(getImsi());
            }

            var gmlcStr = StringUtils.isBlank(getGmlcNumber()) ? getLocalGt() : getGmlcNumber();
            var gmlcNumber = mapParamFactory.createISDNAddressString(ADDRESS_NATURE, NumberingPlan.ISDN, gmlcStr);
            var subscriberIdentity = (msisdn != null) ? mapParamFactory.createSubscriberIdentity(msisdn) :
                    mapParamFactory.createSubscriberIdentity(imsi);
            dialog.addSendRoutingInfoForLCSRequest(gmlcNumber, subscriberIdentity, null);

        } catch (MAPException e) {
            var msg = "Failed to add SRI_GPRS to dialog";
            logger.error(msg, e);
            throw SystemException.builder().code(ErrorCode.MAP_INITIALIZATION).message(msg).parent(e).build();
        }
    }

    @Override
    protected MAPApplicationContext getApplicationContext() throws SystemException {
        Integer[] supportedVersions = {3};
        var mapContextVersion = getAcVersion(Integer.parseInt(getMapVersion()), supportedVersions);
        return MAPApplicationContext.getInstance(MAPApplicationContextName.locationSvcGatewayContext, mapContextVersion);
    }
}
