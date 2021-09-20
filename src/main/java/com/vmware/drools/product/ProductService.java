package com.vmware.drools.product;

import com.vmware.drools.config.DroolsBeanFactory;
import org.kie.api.runtime.KieSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ProductService {

    private KieSession kieSession;

    public ProductService() throws IOException {
        List<String> ruleFiles = Arrays.asList("rules/SuggestApplicant.drl", "rules/Product_rules.xls");
        kieSession = new DroolsBeanFactory(ruleFiles).getKieSession();
    }

    public ProductService(List<String> ruleFiles) throws IOException {
        kieSession = new DroolsBeanFactory(ruleFiles).getKieSession();
    }

    public Product applyLabelToProduct(Product product){
        kieSession.insert(product);
        kieSession.fireAllRules();
        System.out.println(product.getLabel());
        return  product;

    }
}
