package com.vmware.drools;

import com.vmware.drools.application.Applicant;
import com.vmware.drools.application.ApplicantService;
import com.vmware.drools.application.SuggestedRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApplicantServiceTest {

    ApplicantService applicantService;

    @BeforeEach
    public void setup() throws IOException {
        applicantService = new ApplicantService();
    }

    @Test
    public void whenCriteriaMatching_ThenSuggestManagerRole() throws IOException {
        Applicant applicant = new Applicant("David", 37, 1600000.0,11);
        SuggestedRole suggestedRole = new SuggestedRole();

        applicantService.suggestARoleForApplicant(applicant, suggestedRole);

        assertEquals("Manager", suggestedRole.getRole());
    }

    @Test
    public void whenCriteriaMatching_ThenSuggestSeniorDevRole() throws IOException {
        Applicant applicant = new Applicant("David", 37, 700000.0,7);
        SuggestedRole suggestedRole = new SuggestedRole();

        applicantService.suggestARoleForApplicant(applicant, suggestedRole);

        assertEquals("Senior developer", suggestedRole.getRole());
    }
}
