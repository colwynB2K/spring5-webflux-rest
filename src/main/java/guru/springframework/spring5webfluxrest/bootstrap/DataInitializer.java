package guru.springframework.spring5webfluxrest.bootstrap;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repository.CategoryRepository;
import guru.springframework.spring5webfluxrest.repository.VendorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
@Component
@Slf4j
// CommandLineRunner is a Spring Boot specific class that is called at startup
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public DataInitializer(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("#### LOADING DATA ON BOOTSTRAP ####");
        loadCategories();
        loadVendors();
    }

    private void loadCategories() {
        if (categoryRepository.count().block() == 0) {
            Category fruits = Category.builder()
                    .name("Fruits").build();

            Category dried = Category.builder()
                    .name("Dried").build();

            Category fresh = Category.builder()
                    .name("Fresh").build();

            Category exotic = Category.builder()
                    .name("Exotic").build();

            Category nuts = Category.builder()
                    .name("Nuts").build();

            categoryRepository.saveAll(Arrays.asList(fruits, dried, fresh, exotic, nuts)).blockLast();
            log.info("Categories loaded: " + categoryRepository.count().block());
        }
    }

    private void loadVendors() {
        if (vendorRepository.count().block() == 0) {
            Vendor vendor1 = Vendor.builder()
                                    .firstName("Apu")
                                    .lastName("Quick-E Mart")
                                    .build();

            Vendor vendor2 = Vendor.builder()
                                    .firstName("Wattoo")
                                    .lastName("Hutt")
                                    .build();

            vendorRepository.saveAll(Arrays.asList(vendor1, vendor2)).blockLast();
            log.info("Vendors loaded: " + vendorRepository.count().block());
        }
    }
}
