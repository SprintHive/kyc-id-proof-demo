package com.sprinthive.evidence.mgmt.id.demo.model;

import lombok.Data;

import java.util.Date;

@Data
public class IdDocData {

    private String fullName;
    private String lastName;
    private Date dateOfBirth;
    private String nationality;
    private String identifyingNumber;

}
