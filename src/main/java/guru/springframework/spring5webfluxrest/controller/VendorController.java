package guru.springframework.spring5webfluxrest.controller;

import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.exception.ResourceNotFoundException;
import guru.springframework.spring5webfluxrest.repository.VendorRepository;

import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static guru.springframework.spring5webfluxrest.controller.VendorController.URI;

@RestController
@RequestMapping(URI)
public class VendorController {

    public static final String URI = "/api/v1/vendors";

    private final VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping
    public Flux<Vendor> findAll() {
        return vendorRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Vendor> findById(@PathVariable String id) {
        return vendorRepository.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> create(@RequestBody Publisher<Vendor> vendorStream) {
        return vendorRepository.saveAll(vendorStream).then();
    }

    @PutMapping("/{id}")
    public Mono<Vendor> update(@PathVariable String id, @RequestBody Vendor vendor) {
        vendor.setId(id);

        return vendorRepository.save(vendor);
    }

    @PatchMapping("/{id}")
    public Mono<Vendor> patch(@PathVariable String id, @RequestBody Vendor vendor) {
        boolean changesFound = false;

        Vendor vendorToPatch = vendorRepository.findById(id).block();
        if (vendorToPatch == null) {
            throw new ResourceNotFoundException();
        }

        if (StringUtils.hasText(vendor.getFirstName()) && !vendor.getFirstName().equals(vendorToPatch.getFirstName())) {
            vendorToPatch.setFirstName(vendor.getFirstName());
            changesFound = true;
        }

        if (StringUtils.hasText(vendor.getLastName()) && !vendor.getLastName().equals(vendorToPatch.getLastName())) {
            vendorToPatch.setLastName(vendor.getLastName());
            changesFound = true;
        }

        if (changesFound) {
            vendorRepository.save(vendorToPatch);
        }

        return Mono.just(vendorToPatch);
    }
}