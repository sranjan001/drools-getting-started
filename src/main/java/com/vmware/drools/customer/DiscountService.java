package com.vmware.drools.customer;

import com.vmware.drools.config.DroolsBeanFactory;
import org.kie.api.runtime.KieSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class DiscountService {

    private KieSession kieSession;

    public DiscountService() throws IOException {
        List<String> ruleFiles = Arrays.asList("rules/customer/Discount.xls");
        kieSession = new DroolsBeanFactory(ruleFiles).getKieSession();
    }

    public DiscountService(List<String> ruleFiles) throws IOException {
        kieSession = new DroolsBeanFactory(ruleFiles).getKieSession();
    }

    public Customer getDiscountForTheCustomer(Customer customer){
        kieSession.insert(customer);
        kieSession.fireAllRules();
        System.out.println(customer.getDiscount());
        return  customer;

    }
}
