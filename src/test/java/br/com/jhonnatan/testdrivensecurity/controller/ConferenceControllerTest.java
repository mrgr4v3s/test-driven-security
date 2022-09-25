package br.com.jhonnatan.testdrivensecurity.controller;

import br.com.jhonnatan.testdrivensecurity.model.ConferenceUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ConferenceControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void aboutReturnsConferenceInfo() throws Exception {
        mockMvc.perform(get("/about"))
                .andExpect(status().isOk())
                .andExpect(content().string("Join us online September 1-2!"));
    }

    @Test
    void greetingReturnsHelloAndUsername() throws Exception {
        this.mockMvc.perform(get("/greeting")
                        .with(user("Jhonnatan")))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, Jhonnatan"));
    }

    @Test
    void greetingWhenUnauthorized() throws Exception {
        this.mockMvc.perform(get("/greeting"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void submissionsWhenUserIsSpeakerReturnsListOfSubmission() throws Exception {
        ConferenceUser joe = new ConferenceUser(
                "Joe",
                "",
                List.of("Getting Started with Spring Authorization Server"),
                true,
                false);

        this.mockMvc.perform(get("/submissions").with(user(joe)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]", is("Getting Started with Spring Authorization Server")));
    }

    @Test
    void submissionsWhenUserIsNotSpeakerReturns403() throws Exception {
        this.mockMvc.perform(get("/submissions").with(user("user").roles("ATENDEE")))
                .andExpect(status().isForbidden());
    }

    @Test
    void submissionsWhenUnauthenticatedUserReturns401() throws Exception {
        this.mockMvc.perform(get("/submissions"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void postAboutWhenUserIsAdminThenUpdatesConferenceInfo() throws Exception {
        final String testString = "Join us online September 11-12";

        this.mockMvc.perform(post("/about")
                .content(testString)
                .with(user("admin").roles("ADMIN"))
                .with(csrf()))
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/about"))
                .andExpect(status().isOk())
                .andExpect(content().string(testString));
    }

    @Test
    void postAboutWithNoCsrfTokenThen403() throws Exception {
        this.mockMvc.perform(post("/about")
                .content("Join us online September 11-12")
                .with(user("admin").roles("ADMIN")))
                .andExpect(status().isForbidden());
    }

    @Test
    void postAboutWhenUserIsNotAdminThenReturns403() throws Exception {
        this.mockMvc.perform(post("/about")
                        .content("Join us online September 11-12")
                        .with(csrf())
                        .with(user("admin").roles("SPEAKER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void postAboutWhenUnauthenticatedUserReturns401() throws Exception {
        this.mockMvc.perform(post("/about")
                .content("Join us online September 11-12")
                .with(csrf()))
                .andExpect(status().isUnauthorized());
    }
}
