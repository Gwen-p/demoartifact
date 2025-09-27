package com.example.demoartifac;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoartifacApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
	void contextLoads() {
	}

    @Test
    void helloEndpointReturnsDefaultMessage() {
        String body = this.restTemplate.getForObject("/", String.class);
        assertThat(body).isEqualTo("Hello World!");
    }

    @Test
    void helloEndpointReturnsCustomMessage() {
        String body = this.restTemplate.getForObject("/?name=Ana", String.class);
        assertThat(body).isEqualTo("Hello Ana!");
    }

}
