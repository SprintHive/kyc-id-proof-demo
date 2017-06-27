package com.sprinthive.evidence.mgmt.id.demo.service;

import com.sprinthive.evidence.mgmt.id.demo.model.IdDocData;
import com.sprinthive.evidence.mgmt.id.demo.model.IdentityEvidenceRequest;
import lombok.extern.java.Log;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

/**
 * Created by dirk on 2017/06/19.
 */
@EnableBinding({ConsumerChannels.class})
@Log
@Service
@EnableIntegration
public class IdentityUpdater {

    @StreamListener("evidenceRequestCreated")
    public void process(IdentityEvidenceRequest evidenceRequest) throws URISyntaxException {
        log.info("Handling work: " + evidenceRequest);
        IdDocData idDocData = new IdDocData();
        idDocData.setFullName("Dirk Horn");
        idDocData.setLastName("le Roux");
        idDocData.setIdentifyingNumber("7507305183086");
        idDocData.setNationality("RSA");
        idDocData.setDateOfBirth(new Date(Date.UTC(1975, 7, 30, 0, 0, 0)));
        String url = "http://localhost:8080/evidence/v1/identity/id/" + evidenceRequest.getIdentifyingNumber() + "/proof/ID_DOC_SCAN";
        log.info("url: " + url);
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<IdentityEvidenceRequest> responseEntity = restTemplate.postForEntity(new URI(url), idDocData, IdentityEvidenceRequest.class);
            log.info("responseEntity: " + responseEntity);
            IdentityEvidenceRequest identityEvidenceRequest = responseEntity.getBody();
            log.info("identityEvidenceRequest: " + identityEvidenceRequest.toString());
        } catch (RestClientException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


}

interface ConsumerChannels {

    @Input
    SubscribableChannel evidenceRequestCreated();
}