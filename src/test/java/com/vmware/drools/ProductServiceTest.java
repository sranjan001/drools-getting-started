package com.vmware.drools;

import com.vmware.drools.product.Product;
import com.vmware.drools.product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductServiceTest {

    private ProductService productService;

    @BeforeEach
    public void setup() throws IOException {
        productService = new ProductService();
    }


    @Test
    public void whenProductTypeElectronic_ThenLabelBarcode() {
        Product product = new Product("Microwave", "Electronic");
        product = productService.applyLabelToProduct(product);
        assertEquals("BarCode", product.getLabel());
    }

    @Test
    public void whenProductTypeBook_ThenLabelIsbn() {
        Product product = new Product("AutoBiography", "Book");
        product = productService.applyLabelToProduct(product);
        assertEquals("ISBN", product.getLabel());
    }
}