package com.sprinthive.evidence.mgmt.id.demo.model;

import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * Created by dirk on 2017/06/21.
 */
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
