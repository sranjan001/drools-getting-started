package com.vmware.drools.misc;

import com.vmware.drools.config.DroolsBeanFactory;
import com.vmware.drools.misc.agendagroup.Account;
import com.vmware.drools.misc.agendagroup.AccountPeriod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.api.runtime.KieSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AgendaGroupTest {

    @BeforeEach
    public void setup() throws IOException {
    }

    @Test
    public void fireRulesForAgendaGroup() throws IOException {
        List<String> ruleFiles = Arrays.asList("rules/misc/agendagroup/account-agendagroup.drl");
        KieSession kieSession = new DroolsBeanFactory(ruleFiles).getKieSession();;

        Account account = new Account();
        account.setAccountNo("123");
        account.setBalance(50);

        AccountPeriod accountPeriod = new AccountPeriod();

        kieSession.insert(account);
        kieSession.insert(accountPeriod);

        kieSession.getAgenda().getAgendaGroup("report").setFocus();
        kieSession.getAgenda().getAgendaGroup("calculation").setFocus();

        kieSession.fireAllRules();

        kieSession.dispose();
    }

}
