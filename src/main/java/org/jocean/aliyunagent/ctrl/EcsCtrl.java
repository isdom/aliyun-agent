package org.jocean.aliyunagent.ctrl;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.jocean.aliyun.ecs.EcsAPI;
import org.jocean.aliyun.sts.STSCredentials;
import org.jocean.svr.annotation.RpcFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import rx.Observable;

@Controller
@Path("/aliagent/")
@Scope("prototype")
public class EcsCtrl {
    private static final Logger LOG = LoggerFactory.getLogger(EcsCtrl.class);

    @RpcFacade
    EcsAPI ecs;

    @Inject
    @Named("${ecs.id}-stsc")
    STSCredentials _stsc;

    // https://help.aliyun.com/document_detail/25499.html?spm=a2c4g.11186623.6.1083.73643ff5dezPxV
    @Path("ecs/buy1")
    public Observable<? extends Object> buyPostPaid(
            @QueryParam("dryRun") final boolean dryRun,
            @QueryParam("region") final String regionId,
            @QueryParam("zone") final String zoneId,
            @QueryParam("instanceType") final String instanceType,
            @QueryParam("imageId") final String imageId,
            @QueryParam("securityGroupId") final String securityGroupId,
//            @QueryParam("instanceName") final String instanceName,
//            @QueryParam("hostName") final String hostName,
//            @QueryParam("description") final String description,
            @QueryParam("vSwitchId") final String vSwitchId,
            @QueryParam("keyPairName") final String keyPairName,
            @QueryParam("ramRoleName") final String ramRoleName) {
        return ecs.createInstance()
                .signer(_stsc.aliSigner())
                .dryRun(dryRun)
                .imageId(imageId)
                .instanceType(instanceType)
                .regionId(regionId)
                .zoneId(zoneId)
                .securityGroupId(securityGroupId)
                .internetMaxBandwidthOut(0)
//                .internetChargeType("PayByTraffic")
//                .instanceName(instanceName)
//                .hostName(hostName)
                .systemDiskSize(20)
                .systemDiskCategory("cloud_efficiency")
                .ioOptimized("optimized")
//                .description(description)
                .vSwitchId(vSwitchId)
                .useAdditionalService(true)
                .instanceChargeType("PostPaid")
                .spotStrategy("NoSpot")
                .keyPairName(keyPairName)
                .ramRoleName(ramRoleName)
                .securityEnhancementStrategy("Active")
                .call();
    }

}
