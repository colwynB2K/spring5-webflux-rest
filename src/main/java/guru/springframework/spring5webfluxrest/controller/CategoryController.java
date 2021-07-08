package guru.springframework.spring5webfluxrest.controller;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.repository.CategoryRepository;

import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static guru.springframework.spring5webfluxrest.controller.CategoryController.URI;

@RestController
@RequestMapping(URI)
public class CategoryController {

    public static final String URI = "/api/v1/categories";

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public Flux<Category> findAll() {
        return categoryRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Category> findById(@PathVariable String id) {
        return categoryRepository.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)                                           // Make sure to return a 201 HTTP STATUS for object creations
    public Mono<Void> create(@RequestBody Publisher<Category> categoryStream) {  // called categoryStream as it can be a stream of one or more objects: Publisher is part of Reactive Streams API. Mono and Flux are implementations of a Publisher, so this method will be able to take in both.
        return categoryRepository.saveAll(categoryStream).then();   // .then() makes sure to return a Mono<Void>
    }
}
