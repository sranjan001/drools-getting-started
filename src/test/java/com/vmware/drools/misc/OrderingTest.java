package com.vmware.drools.misc;

import com.vmware.drools.config.DroolsBeanFactory;
import com.vmware.drools.misc.ordering.MyFact;
import com.vmware.drools.misc.ordering.MyProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.api.runtime.KieContainerSessionsPool;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class OrderingTest {

    @BeforeEach
    public void setup() throws IOException {
    }

    @Test
    public void fireRulesInOrder() throws IOException {
        List<String> ruleFiles = Arrays.asList("rules/misc/ordering/salience-test.drl");
        KieSession kieSession = new DroolsBeanFactory(ruleFiles).getKieSession();;

        MyFact myFact = new MyFact();
        myFact.setField1(false);

        kieSession.insert(myFact);
        kieSession.fireAllRules();
        kieSession.dispose();
    }

    @Test
    public void fireRulesForProductInOrder() throws IOException {
        List<String> prodRuleFiles = Arrays.asList("rules/misc/ordering/product-salience.drl");
        KieSession prodKieSession = new DroolsBeanFactory(prodRuleFiles).getKieSession();

        MyProduct product = new MyProduct();
        product.setType("gold");
        product.setEvent("sale");

        prodKieSession.insert(product);
        product.setBuyer("platinum");

        prodKieSession.fireAllRules();
        prodKieSession.dispose();
    }

    @Test
    public void fireRulesForProductInOrder_KieSessionPool() throws IOException {
        List<String> prodRuleFiles = Arrays.asList("rules/misc/ordering/product-salience.drl");

        KieContainerSessionsPool pool = new DroolsBeanFactory(prodRuleFiles).getKieContainer().newKieSessionsPool(5);

        for(int i=0; i<10; i++) {
            KieSession kieSession = pool.newKieSession();
            MyProduct product = new MyProduct();
            product.setType("gold");
            product.setEvent("sale");

            kieSession.insert(product);
            product.setBuyer("platinum");

            kieSession.fireAllRules();
            kieSession.dispose();
//            kieSession.fireAllRules();

            System.out.println("Discount : " + product.getDiscount());
        }
    }

    @Test
    public void fireRulesForProductInOrder_KieStatelessSessionPool() throws IOException {
        List<String> prodRuleFiles = Arrays.asList("rules/misc/ordering/product-salience.drl");

        KieContainerSessionsPool pool = new DroolsBeanFactory(prodRuleFiles).getKieContainer().newKieSessionsPool(5);

        //for(int i=0; i<10; i++) {
        StatelessKieSession kieSession = pool.newStatelessKieSession();
        MyProduct product = new MyProduct();
        product.setType("gold");
        product.setEvent("sale");

        product.setBuyer("platinum");

        kieSession.execute(product);
        kieSession.execute(product);

        System.out.println("Discount : " + product.getDiscount());
        //}
    }

}
