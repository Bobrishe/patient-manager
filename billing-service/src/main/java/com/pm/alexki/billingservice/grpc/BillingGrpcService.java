package com.pm.alexki.billingservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import com.pm.alexki.billingservice.grpc.enums.BillingStatusEnum;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class BillingGrpcService extends BillingServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(BillingGrpcService.class);

    @Override
    public void createBillingAccount(BillingRequest request, StreamObserver<BillingResponse> responseObserver) {

        log.info("createBillingAccount request received {}", request.toString());

        BillingResponse response = BillingResponse.newBuilder()
                .setAccountId(request.getPatientId())
                .setStatus(BillingStatusEnum.ACTIVE.name())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
                