package com.vmware.drools.misc;

import com.vmware.drools.config.DroolsBeanFactory;
import com.vmware.drools.misc.agendagroup.Account;
import org.junit.jupiter.api.Test;
import org.kie.api.runtime.KieSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MiscTest {

    @Test
    public void fireRules_Contains() throws IOException {
        List<String> ruleFiles = Arrays.asList("rules/misc/contains.drl");
        KieSession kieSession = new DroolsBeanFactory(ruleFiles).getKieSession();;

        Account account = new Account();
        account.setAccountNo("123");
        account.setBalance(50);
        account.setTypes(Arrays.asList("personal", "saving", "sgd"));

        kieSession.insert(account);

        kieSession.fireAllRules();

        kieSession.dispose();
    }

    @Test
    public void fireRules_In() throws IOException {
        List<String> ruleFiles = Arrays.asList("rules/misc/contains.drl");
        KieSession kieSession = new DroolsBeanFactory(ruleFiles).getKieSession();

//        List<String> validStatus = Arrays.asList("ACTIVE", "DRAFT");

        Account account = new Account();
        account.setAccountNo("123");
        account.setBalance(50);
        account.setTypes(Arrays.asList("personal", "saving", "sgd"));
        account.setStatus("Active");

//        kieSession.insert(validStatus);
        kieSession.insert(new Lookup());
        kieSession.insert(account);

        kieSession.fireAllRules();

        kieSession.dispose();
    }
}
