package com.vmware.drools.misc;

import com.vmware.drools.misc.template.Loan;
import org.drools.decisiontable.ExternalSpreadsheetCompiler;
import org.drools.template.ObjectDataCompiler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DroolTemplateTest {

    private final static Logger LOG = LoggerFactory.getLogger(DroolTemplateTest.class);

    private KieContainer kieContainer;
    private KieBase kieBase;
    private static final String KIE_BASE = "loanKieBase";

    @BeforeEach
    public void setup() {
        kieContainer = KieServices.Factory.get().getKieClasspathContainer();
        kieBase = kieContainer.getKieBase(KIE_BASE);
    }

    @Test
    public void testSimpleLoan() {
        Loan loan = new Loan();
        loan.setIdentifier("simple");
        KieSession kieSession = kieBase.newKieSession();

        kieSession.insert(loan);
        kieSession.fireAllRules();

        assertEquals((Integer) 1000, loan.getMaxAmount());
    }

    @Test
    public void testComplicatedLoan() {
        Loan loan = new Loan();
        loan.setIdentifier("complex");
        KieSession kieSession = kieBase.newKieSession();

        kieSession.insert(loan);
        kieSession.fireAllRules();

        assertEquals((Integer) 5000, loan.getMaxAmount());
    }

    //Tests using programmatic load
    @Test
    public void testComplicatedLoan_manualLoad() {
        Loan loan = new Loan();
        loan.setIdentifier("complex");
        KieSession kieSession = getKieBaseFromTemplate().newKieSession();

        kieSession.insert(loan);
        kieSession.fireAllRules();

        assertEquals((Integer) 5000, loan.getMaxAmount());
    }

    //Load template through code
    private KieBase getKieBaseFromTemplate() {
        InputStream template = DroolTemplateTest.class.getResourceAsStream("/rules/misc/template/loan-template.drt");
        InputStream data = DroolTemplateTest.class.getResourceAsStream("/rules/misc/template/LoanData.xls");

        //To get data from database
        // ObjectDataCompiler converter = new ObjectDataCompiler();

        ExternalSpreadsheetCompiler converter = new ExternalSpreadsheetCompiler();
        String drl = converter.compile(data, template, 2, 1);

        System.out.println(drl);
        KieBase kieBase = createKieSessionFromDRL(drl);

        return kieBase;
    }

    private KieBase createKieSessionFromDRL(String drl) {
        KieHelper kieHelper = new KieHelper();
        kieHelper.addContent(drl, ResourceType.DRL);

        Results results = kieHelper.verify();

        if(results.hasMessages(Message.Level.WARNING, Message.Level.ERROR)) {
            List<Message> messages = results.getMessages(Message.Level.WARNING, Message.Level.ERROR);

            messages.forEach(message -> {
                LOG.error("Error: " + message.getText());
            });

            throw new IllegalStateException("Compilation errors were found. Check the logs.");
        }

        return kieHelper.build();
    }
}
