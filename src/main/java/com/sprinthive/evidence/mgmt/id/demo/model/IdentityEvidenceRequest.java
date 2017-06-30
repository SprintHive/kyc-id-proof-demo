package com.sprinthive.evidence.mgmt.id.demo.model;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class IdentityEvidenceRequest {

    private String firstName;
    private String middleNames;
    private String lastName;
    private Date dateOfBirth;
    private String nationality;
    private String identifyingNumber;
    private Map<String, Map<String, Object>> proofs;

}
