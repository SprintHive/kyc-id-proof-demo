package com.sprinthive.evidence.mgmt.id.demo.service;

import com.sprinthive.evidence.mgmt.id.demo.model.IdDocData;
import com.sprinthive.evidence.mgmt.id.demo.model.IdentityEvidenceRequest;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;

@Log
@Service
@EnableRetry
public class IdentityUpdater {

    @Retryable(value = HttpServerErrorException.class, backoff = @Backoff(delay = 300, multiplier = 3))
    void addProof(IdentityEvidenceRequest evidenceRequest, IdDocData idDocData) throws URISyntaxException {
        String url = "http://localhost:8080/evidence/v1/identity/id/" + evidenceRequest.getIdentifyingNumber() + "/proof/ID_DOC_SCAN";
        log.info("url: " + url);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<IdentityEvidenceRequest> responseEntity = restTemplate.postForEntity(new URI(url), idDocData, IdentityEvidenceRequest.class);
        log.info("responseEntity: " + responseEntity);
        IdentityEvidenceRequest identityEvidenceRequest = responseEntity.getBody();
        log.info("identityEvidenceRequest: " + identityEvidenceRequest.toString());
    }

    @Recover
    public void recover(HttpServerErrorException e, IdentityEvidenceRequest evidenceRequest, IdDocData idDocData) {
        // ... panic
        handleException(e, evidenceRequest, idDocData);
    }

    private void handleException(Exception e, IdentityEvidenceRequest evidenceRequest, IdDocData idDocData) {
        log.log(Level.SEVERE, "Unexpected error adding proof to an evidence request (" + evidenceRequest.getIdentifyingNumber() + ").", e);
    }
}
