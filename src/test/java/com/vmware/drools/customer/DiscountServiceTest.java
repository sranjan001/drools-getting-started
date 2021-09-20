package com.vmware.drools.customer;

import org.drools.core.builder.conf.impl.DecisionTableConfigurationImpl;
import org.drools.decisiontable.DecisionTableProviderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.api.io.Resource;
import org.kie.internal.io.ResourceFactory;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class DiscountServiceTest {

    DiscountService discountService;
    @BeforeEach
    void setUp() throws IOException {
        discountService = new DiscountService();
    }

    @Test
    public void giveIndvidualLongStanding_whenFireRule_thenCorrectDiscount() throws Exception {
        Customer customer = new Customer(Customer.CustomerType.INDIVIDUAL, 5);

        discountService.getDiscountForTheCustomer(customer);

        assertEquals(customer.getDiscount(), 15);
    }

    @Test
    public void giveIndvidualRecent_whenFireRule_thenCorrectDiscount() throws Exception {

        Customer customer = new Customer(Customer.CustomerType.INDIVIDUAL, 1);
        discountService.getDiscountForTheCustomer(customer);

        assertEquals(customer.getDiscount(), 5);
    }

    @Test
    public void giveBusinessAny_whenFireRule_thenCorrectDiscount() throws Exception {
        Customer customer = new Customer(Customer.CustomerType.BUSINESS, 0);
        discountService.getDiscountForTheCustomer(customer);

        assertEquals(customer.getDiscount(), 20);
    }

    @Test
    public void printDecisionTableToDRL() {
        Resource dt
                = ResourceFactory
                .newClassPathResource("rules/customer/Discount.xls",
                        getClass());

        DecisionTableProviderImpl decisionTableProvider
                = new DecisionTableProviderImpl();

        String drl = decisionTableProvider.loadFromResource(dt, new DecisionTableConfigurationImpl());
        System.out.println(drl);
    }
}