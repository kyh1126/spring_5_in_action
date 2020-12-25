package spring;

import org.junit.jupiter.api.*;

class SampleTest {

    @BeforeAll
    static void beforeAll() {
        System.out.println("BeforeAll method call");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("");
        System.out.println("BeforeEach method call");
    }

    @Test
    void test() {
        System.out.println("test method call");
    }

    @Test
    @DisplayName(value = "DisplayName 으로 테스트 메서드 이름 줄 수 있다.")
    void test2() {
        System.out.println("test2 method call");
    }

    @AfterEach
    void afterEach() {
        System.out.println("afterEach method call");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("afterAll method call");
    }
}
