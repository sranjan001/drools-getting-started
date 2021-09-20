package com.vmware.drools.application;

import com.vmware.drools.config.DroolsBeanFactory;
import org.kie.api.runtime.StatelessKieSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ApplicantService {

    private StatelessKieSession kieSession;

    public ApplicantService() throws IOException {
        List<String> ruleFiles = Arrays.asList("rules/SuggestApplicant.drl");
        kieSession = new DroolsBeanFactory(ruleFiles).getStatelessKieSession();
    }

    public ApplicantService(List<String> ruleFiles) throws IOException {
        kieSession = new DroolsBeanFactory(ruleFiles).getStatelessKieSession();
    }

    public SuggestedRole suggestARoleForApplicant(Applicant applicant, SuggestedRole suggestedRole) throws IOException {
//        kieSession.insert(applicant);
        kieSession.setGlobal("suggestedRole",suggestedRole);
        kieSession.execute(applicant);
        System.out.println(suggestedRole.getRole());
        return  suggestedRole;
    }
}
