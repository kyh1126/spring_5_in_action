package spring;

import org.junit.jupiter.api.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.*;
import java.util.stream.Stream;

class FluxTest {

    @Test
    void createAFluxJust() {
        Flux<String> fruitFlux = Flux.just("Apple", "Orange", "Grape", "Banana", "Strawberry");

        fruitFlux.subscribe(
                f -> System.out.println("Here's some fruit:" + f)
        );

        StepVerifier.create(fruitFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
//                .expectNext("a")
                .verifyComplete();
    }

    @Test
    public void createAFlux_fromIterable() {
        List<String> fruitList = new ArrayList<>();
        fruitList.add("Apple");
        fruitList.add("Orange");
        fruitList.add("Grape");
        fruitList.add("Banana");
        fruitList.add("Strawberry");

        Flux<String> fruitFlux = Flux.fromIterable(fruitList);

        StepVerifier.create(fruitFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
//                .expectNext("a")
                .verifyComplete();
    }

    @Test
    public void createAFlux_fromArray() {
        List<String> fruitList = new ArrayList<>();
        String[] fruits = new String[]{
                "Apple", "Orange", "Grape", "Banana", "Strawberry"
        };

        Flux<String> fruitFlux = Flux.fromArray(fruits);

        StepVerifier.create(fruitFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
//                .expectNext("a")
                .verifyComplete();
    }

    @Test
    public void createAFlux_fromStream() {
        List<String> fruitList = new ArrayList<>();
        String[] fruits = new String[]{
                "Apple", "Orange", "Grape", "Banana", "Strawberry"
        };

        Stream<String> fruitStream = Stream.of(fruits);

        Flux<String> fruitFlux = Flux.fromStream(fruitStream);

        StepVerifier.create(fruitFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
//                .expectNext("a")
                .verifyComplete();
    }

    @Test
    public void createAFlux_range() {
        Flux<Integer> intervalFlux = Flux.range(1, 5);

        StepVerifier.create(intervalFlux)
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                .expectNext(5)
                .verifyComplete();
    }

    @Test
    public void createAFlux_interval() {
        Flux<Long> intervalFlux = Flux.interval(Duration.ofSeconds(1))
                .take(5);

        StepVerifier.create(intervalFlux)
                .expectNext(0L)
                .expectNext(1L)
                .expectNext(2L)
                .expectNext(3L)
                .expectNext(4L)
                .verifyComplete();
    }

    @Test
    public void mergeFluxes() {
        Flux<String> characterFlux = Flux.just("Garfield", "Kojak", "Barbosa")
                .delayElements(Duration.ofMillis(500));

        Flux<String> foodFlux = Flux.just("Lasagna", "Lollipops", "Pasta")
                .delaySubscription(Duration.ofMillis(250))
                .delayElements(Duration.ofMillis(500));

        Flux<String> mergedFlux = characterFlux.mergeWith(foodFlux);

        StepVerifier.create(mergedFlux)
                .expectNext("Garfield")
                .expectNext("Lasagna")
                .expectNext("Kojak")
                .expectNext("Lollipops")
                .expectNext("Barbosa")
                .expectNext("Pasta")
                .verifyComplete();
    }

    @Test
    public void zipFluxes() {
        Flux<String> characterFlux = Flux.just("Garfield", "Kojak", "Barbosa");

        Flux<String> foodFlux = Flux.just("Lasagna", "Lollipops", "Pasta");

        Flux<Tuple2<String, String>> zippedFlux = Flux.zip(characterFlux, foodFlux);

        StepVerifier.create(zippedFlux)
                .expectNextMatches(
                        p -> p.getT1().equals("Garfield") && p.getT2().equals("Lasagna")
                )
                .expectNextMatches(
                        p -> p.getT1().equals("Kojak") && p.getT2().equals("Lollipops")
                )
                .expectNextMatches(
                        p -> p.getT1().equals("Barbosa") && p.getT2().equals("Pasta")
                )
                .verifyComplete();
    }

    @Test
    public void zipFluxesToObjects() {
        Flux<String> characterFlux = Flux.just("Garfield", "Kojak", "Barbosa");

        Flux<String> foodFlux = Flux.just("Lasagna", "Lollipops", "Pasta");

        Flux<String> zippedFlux = Flux.zip(characterFlux, foodFlux, (c, f) -> c + " eats " + f);

        StepVerifier.create(zippedFlux)
                .expectNext("Garfield eats Lasagna")
                .expectNext("Kojak eats Lollipops")
                .expectNext("Barbosa eats Pasta")
                .verifyComplete();
    }

    @Test
    public void firstFlux() {
        Flux<String> slowFlux = Flux.just("tortoise", "snail", "sloth")
                .delaySubscription(Duration.ofMillis(200));

        Flux<String> fastFlux = Flux.just("hare", "cheetah", "squirrel");

        Flux<String> firstFlux = Flux.first(slowFlux, fastFlux);

        StepVerifier.create(firstFlux)
                .expectNext("hare")
                .expectNext("cheetah")
                .expectNext("squirrel")
                .verifyComplete();

        // Deprecated
        //use firstWithSignal(Publisher[]). To be removed in reactor 3.5.
        Flux<String> firstWithSignal = Flux.firstWithSignal(fastFlux, slowFlux);

        StepVerifier.create(firstWithSignal)
                .expectNext("hare")
                .expectNext("cheetah")
                .expectNext("squirrel")
                .verifyComplete();
    }

    @Test
    public void skipAFew() {
        Flux<String> skipFLux = Flux.just("one", "two", "skip a few", "99", "100")
                .delayElements(Duration.ofSeconds(1))
                .skip(Duration.ofSeconds(4));
        StepVerifier.create(skipFLux)
                .expectNext("99")
                .expectNext("100")
                .verifyComplete();

        Flux<String> skipFlux = Flux.just("one", "two", "skip a few", "99", "100")
                .skip(3);

        StepVerifier.create(skipFlux)
                .expectNext("99")
                .expectNext("100")
                .verifyComplete();
    }

    @Test
    public void take() {
        Flux<String> takeFlux = Flux.just("one", "two", "skip a few", "99", "100")
                .delayElements(Duration.ofSeconds(1))
                .take(Duration.ofSeconds(3));
        StepVerifier.create(takeFlux)
                .expectNext("one")
                .expectNext("two")
                .verifyComplete();

        Flux<String> takeFluxNumber = Flux.just("one", "two", "skip a few", "99", "100")
//                .delayElements(Duration.ofSeconds(1))
                .take(2);

        StepVerifier.create(takeFluxNumber)
                .expectNext("one", "two")
                .verifyComplete();
    }

    @Test
    public void filter() {
        Flux<String> filteredFlux = Flux.just("1", "2", "32", "4", "5")
                .filter(num -> !num.contains("2"));

        StepVerifier.create(filteredFlux)
                .expectNext("1", "4", "5")
                .verifyComplete();
    }

    @Test
    public void distinct() {
        Flux<String> filteredFlux = Flux.just("1", "2", "2", "1", "3")
                .distinct();

        StepVerifier.create(filteredFlux)
                .expectNext("1", "2", "3")
                .verifyComplete();
    }

    @Test
    public void map() {
        Flux<Player> playerFlux = Flux.just("Michael Jordan")
                .map(n -> {
                    String[] split = n.split("\\s");
                    return new Player(split[0], split[1]);
                });

        Flux<String> playNameFlux = Flux.just("Michael Jordan")
                .map(n -> {
                    String[] split = n.split("\\s");
                    return new Player(split[0], split[1]).name;
                }).log();

        StepVerifier.create(playNameFlux)
                .expectNext(new Player("Michael", "Jordan").name)
                .verifyComplete();
    }

    @Test
    public void flatMap() {
        Flux<Player> playerFlux = Flux.just("Michael Jordan", "Super Sonny", "Lebron James")
                .flatMap(n -> Mono.just(n)
                        .map(p -> {
                            String[] split = p.split("\\s");
                            return new Player(split[0], split[1]);
                        }).subscribeOn(Schedulers.parallel()));

        Flux<String> playNameFlux = Flux.just("Michael Jordan", "Super Sonny", "Lebron James")
                .flatMap(n -> Mono.just(n)
                        .map(p -> {
                            String[] split = p.split("\\s");
                            return new Player(split[0], split[1]).name;
                        }).subscribeOn(Schedulers.parallel()));

        List<Player> playerList = Arrays.asList(
                new Player("Michael", "Jordan"),
                new Player("Super", "Sonny"),
                new Player("Lebron", "James")
        );

        List<String> players = Arrays.asList(
                new Player("Michael", "Jordan").name,
                new Player("Super", "Sonny").name,
                new Player("Lebron", "James").name
        );
        StepVerifier.create(playNameFlux)
                .expectNextMatches(p -> players.contains(p))
                .expectNextMatches(p -> players.contains(p))
                .expectNextMatches(p -> players.contains(p))
                .verifyComplete();
    }

    @Test
    public void buffer() {
        Flux<String> numFlux = Flux.just("1", "2", "3", "4", "5");
        Flux<List<String>> bufferedFlux2 = Flux.just("1", "2", "3", "4", "5")
                .buffer(3);

        Flux<List<String>> bufferedFlux = numFlux.buffer(3);

        StepVerifier.create(bufferedFlux)
                .expectNext(Arrays.asList("1", "2", "3"))
                .expectNext(Arrays.asList("4", "5"))
                .verifyComplete();
    }

    @Test
    public void bufferParallel() {
        Flux.just("ab", "cd", "ef", "gh", "tre")
                .buffer(3)
                .flatMap(x ->
                        Flux.fromIterable(x)
                                .map(y -> y.toUpperCase())
                                .subscribeOn(Schedulers.parallel())
                                .log()
                )
                .subscribe();
    }

    @Test
    public void collectList() {
        Flux<String> stringFlux = Flux.just("1", "2", "3", "4", "5");

        Mono<List<String>> fruitListMono = stringFlux.collectList();

        StepVerifier.create(fruitListMono)
                .expectNext(Arrays.asList("1", "2", "3", "4", "5"))
                .verifyComplete();
    }

    @Test
    public void collectMap() {
        Flux<String> stringFlux = Flux.just("eagle", "cat", "dog", "elephant", "coyote");

        Mono<Map<Character, String>> fruitListMono = stringFlux.collectMap(a -> a.charAt(0));

        StepVerifier.create(fruitListMono)
            .expectNextMatches(map -> {
                System.out.println(map.toString());
                return
                    map.size() == 3 &&
                    map.get('a').equals("elephant") &&
                    map.get('c').equals("coyote");
                }
            )
        .verifyComplete();
    }

    @Test
    public void all() {
        Flux<String> animalFlux = Flux.just("eagle", "cat", "dog", "elephant", "coyote");

        Mono<Boolean> hasMono = animalFlux.all(a -> a.contains("a"));

        StepVerifier.create(hasMono)
            .expectNext(false)
            .verifyComplete();
    }

    @Test
    public void any() {
        Flux<String> animalFlux = Flux.just("eagle", "cat", "dog", "elephant", "coyote");

        Mono<Boolean> hasMono = animalFlux.any(a -> a.contains("a"));

        StepVerifier.create(hasMono)
                .expectNext(true)
                .verifyComplete();
    }

    static class Player {
        String name;

        Player(String firstName, String lastName) {
            name = firstName + " " + lastName;
        }
    }
}
