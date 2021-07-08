package guru.springframework.spring5webfluxrest.controller;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.repository.CategoryRepository;

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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    private WebTestClient webTestClient;

    @Mock
    private CategoryRepository mockCategoryRepository;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    void findAll() {
        // given
        when(mockCategoryRepository.findAll()).thenReturn(Flux.just(Category.builder().name("Cat1").build(),
                                                                    Category.builder().name("Cat2").build()));

        // when
        webTestClient.get().uri(CategoryController.URI)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBodyList(Category.class)
                            .hasSize(2);

        // then
    }

    @Test
    void findById() {
        // given
        when(mockCategoryRepository.findById(anyString())).thenReturn(Mono.just(Category.builder().name("Cat1").build()));

        // when
        webTestClient.get().uri(CategoryController.URI + "/1")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Category.class);

        // then
    }

    @Test
    void create() {
        // given
        Flux<Category> savedCategories = Flux.just(Category.builder().build());
        Mono<Category> categoryToSave = Mono.just(Category.builder().name("Cat1").build());
        when(mockCategoryRepository.saveAll(any(Publisher.class))).thenReturn(savedCategories);

        // when
        webTestClient.post().uri(CategoryController.URI)
                .body(categoryToSave, Category.class)
                .exchange()
                .expectStatus()
                .isCreated();

        // then
    }
}