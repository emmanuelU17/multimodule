package dev.fullstack;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfig.class)
@Transactional
class CarControllerTest {

    @Value("/${api.base.path}/car")
    private String path;

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    private ICar repository;

    @Test
    void carById() throws Exception {
        // when
        repository.save(new Car("Jeep Wrangler", new Date()));

        // then
        mockMvc.perform(get(path + "/1")).andExpect(status().isOk());
    }

    @Test
    void shouldSuccessfullyCreateACar() throws Exception {
        // given
        record Payload(String brand) {
        }

        // when
        mockMvc.perform(post(path).contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper()
                                .writeValueAsString(new Payload("BMW")))
                )
                .andExpect(status().isCreated());

        // when
        var optional = repository.carById(1);

        // then
        assertTrue(optional.isPresent());
        assertEquals("BMW", optional.get().brand());
    }

}