package com.realestate.plan.controller;

import com.realestate.plan.entity.Builder;
import com.realestate.plan.entity.Project;
import com.realestate.plan.enums.AreaUnit;
import com.realestate.plan.enums.BuilderStatus;
import com.realestate.plan.enums.BuilderType;
import com.realestate.plan.enums.ProjectStatus;
import com.realestate.plan.enums.TowerStatus;
import com.realestate.plan.enums.TowerType;
import com.realestate.plan.repository.BuilderRepository;
import com.realestate.plan.repository.ProjectRepository;
import com.realestate.plan.repository.TowerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@Transactional
@Rollback
public class TowerIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private BuilderRepository builderRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TowerRepository towerRepository;

    private Builder savedBuilder;
    private Project savedProject;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .apply(springSecurity())
                .build();

        String unique = java.util.UUID.randomUUID().toString().substring(0, 8);
        Builder builder = Builder.builder()
                .builderCode("TEST-BLD-" + unique)
                .companyName("Test Builder")
                .type(BuilderType.PRIVATE_LIMITED)
                .status(BuilderStatus.ACTIVE)
                .primaryPhone("999999" + (int) (Math.random() * 10000))
                .emailId("testbuilder+" + unique + "@example.com")
                .build();
        savedBuilder = builderRepository.save(builder);

        Project project = Project.builder()
                .projectCode("PRJ-" + unique)
                .projectName("Test Project")
                .status(ProjectStatus.PLANNED)
                .builder(savedBuilder)
                .build();
        savedProject = projectRepository.save(project);
    }

    @Test
    void createTower_shouldPersistTower() throws Exception {
        String payload = "{\n" +
                "  \"towerName\": \"Tower A\",\n" +
                "  \"projectId\": " + savedProject.getId() + ",\n" +
                "  \"towerType\": \"RESIDENTIAL\",\n" +
                "  \"towerStatus\": \"PLANNED\",\n" +
                "  \"liftCount\": 2,\n" +
                "  \"totalFloors\": 10,\n" +
                "  \"floorCapacity\": 4,\n" +
                "  \"areaUnit\": \"SQFT\",\n" +
                "  \"builtUpArea\": 4500.5\n" +
                "}";

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/v1/towers")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isCreated());

        assertThat(towerRepository.findByProjectIdAndIsDeletedFalse(savedProject.getId())).isNotEmpty();
    }

    @Test
    void createTower_duplicateNameReturnsConflict() throws Exception {
        String firstPayload = "{\n" +
                "  \"towerName\": \"Tower B\",\n" +
                "  \"projectId\": " + savedProject.getId() + ",\n" +
                "  \"towerType\": \"RESIDENTIAL\",\n" +
                "  \"towerStatus\": \"PLANNED\",\n" +
                "  \"liftCount\": 2,\n" +
                "  \"totalFloors\": 8,\n" +
                "  \"floorCapacity\": 4\n" +
                "}";

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/v1/towers")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(firstPayload))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isCreated());

        String duplicatePayload = "{\n" +
                "  \"towerName\": \"Tower B\",\n" +
                "  \"projectId\": " + savedProject.getId() + ",\n" +
                "  \"towerType\": \"COMMERCIAL\",\n" +
                "  \"towerStatus\": \"PLANNED\",\n" +
                "  \"liftCount\": 1,\n" +
                "  \"totalFloors\": 5,\n" +
                "  \"floorCapacity\": 2\n" +
                "}";

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/v1/towers")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(duplicatePayload))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isConflict());
    }
}
