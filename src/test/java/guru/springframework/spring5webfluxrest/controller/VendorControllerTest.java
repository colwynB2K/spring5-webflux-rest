package guru.springframework.spring5webfluxrest.controller;

import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repository.VendorRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class VendorControllerTest {

    private WebTestClient webTestClient;

    @Mock
    private VendorRepository mockVendorRepository;

    @InjectMocks
    private VendorController vendorController;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    void findAll() {
        // given
        given(mockVendorRepository.findAll())
                .willReturn(Flux.just(Vendor.builder().firstName("Vendor").lastName("One").build(),
                                        Vendor.builder().firstName("Vendor").lastName("Two").build()));

        // when
        webTestClient.get().uri(VendorController.URI)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBodyList(Vendor.class)
                            .hasSize(2);

        // then
    }

    @Test
    void findById() {
        // given
        given(mockVendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder()
                                            .firstName("Vendor")
                                            .lastName("One")
                                            .build()));

        // when
        webTestClient.get().uri(VendorController.URI + "/1")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Vendor.class);

        // then
    }

    @Test
    void create() {
        // given
        Mono<Vendor> vendorToSaveMono = Mono.just(Vendor.builder().firstName("Vendor").lastName("One").build());
        Flux<Vendor> savedVendors = Flux.just(Vendor.builder().build());
        given(mockVendorRepository.saveAll(any(Publisher.class))).willReturn(savedVendors);

        // when
        webTestClient.post().uri(VendorController.URI)
                .body(vendorToSaveMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();

        // then
    }

    @Test
    void update() {
        //given
        Mono<Vendor> vendorToSaveMono = Mono.just(Vendor.builder().firstName("Vendor").lastName("One").build());
        given(mockVendorRepository.save(any(Vendor.class))).willReturn(Mono.just(Vendor.builder().build()));

        // when
        webTestClient.put().uri(VendorController.URI + "/1")
                .body(vendorToSaveMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        // then
    }
}