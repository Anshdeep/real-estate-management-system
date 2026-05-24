package com.realestate.plan.controller;

import com.realestate.plan.entity.Builder;
import com.realestate.plan.repository.BuilderRepository;
import com.realestate.plan.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@Transactional
@Rollback
public class ProjectIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private BuilderRepository builderRepository;

    @Autowired
    private ProjectRepository projectRepository;

    private Builder savedBuilder;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
            .apply(springSecurity())
            .build();

        // ensure a builder exists
        String unique = java.util.UUID.randomUUID().toString().substring(0,8);
        Builder b = Builder.builder()
                .builderCode("TEST-BLD-" + unique)
                .companyName("Test Builders Ltd")
                .type(com.realestate.plan.enums.BuilderType.PRIVATE_LIMITED)
                .status(com.realestate.plan.enums.BuilderStatus.ACTIVE)
                .primaryPhone("999999" + (int)(Math.random()*10000))
                .emailId("testbuilders+" + unique + "@example.com")
                .build();
        savedBuilder = builderRepository.save(b);
    }

    @Test
    void createProject_shouldPersistProject() throws Exception {
        String payload = "{\n" +
                "  \"projectName\": \"Integration Project\",\n" +
                "  \"description\": \"Created by integration test\",\n" +
                "  \"status\": \"PLANNED\",\n" +
                "  \"phase\": \"EXCAVATION\",\n" +
                "  \"builderId\": " + savedBuilder.getId() + ",\n" +
                "  \"totalArea\": 10.5,\n" +
                "  \"areaUnit\": \"ACRE\",\n" +
                "  \"addressLine1\": \"42 Test Lane\",\n" +
                "  \"city\": \"TestCity\",\n" +
                "  \"state\": \"TestState\",\n" +
                "  \"pincode\": \"560001\",\n" +
                "  \"startDate\": \"2026-06-01\",\n" +
                "  \"expectedCompletionDate\": \"2027-06-01\"\n" +
                "}";

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/v1/projects")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isCreated());

        assertThat(projectRepository.findByIsDeletedFalse()).isNotEmpty();
    }
}
