package com.sprinthive.evidence.mgmt.id.demo.service;

import com.sprinthive.evidence.mgmt.id.demo.model.IdDocData;
import com.sprinthive.evidence.mgmt.id.demo.model.IdentityEvidenceRequest;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.net.URISyntaxException;
import java.util.Date;
import java.util.logging.Level;

@EnableBinding({ConsumerChannels.class})
@Log
@Service
public class EvidenceRequestListener {

    @Autowired
    private IdentityUpdater identityUpdater;

    @StreamListener("evidenceRequestCreated")
    public void process(IdentityEvidenceRequest evidenceRequest) {
        log.info("Handling work: " + evidenceRequest);
        IdDocData idDocData = new IdDocData();
        idDocData.setFullName("Dirk Horn");
        idDocData.setLastName("le Roux");
        idDocData.setIdentifyingNumber("7507305183086");
        idDocData.setNationality("RSA");
        idDocData.setDateOfBirth(new Date(Date.UTC(1975, 7, 30, 0, 0, 0)));
        try {
            identityUpdater.addProof(evidenceRequest, idDocData);
        } catch (RestClientException | URISyntaxException e) {
            log.log(Level.SEVERE, "Unexpected error adding proof to an evidence request (" + evidenceRequest.getIdentifyingNumber() + ").", e);
        }
    }
}

interface ConsumerChannels {

    @Input
    SubscribableChannel evidenceRequestCreated();
}