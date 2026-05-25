package com.realestate.plan.controller;

import com.realestate.plan.entity.Builder;
import com.realestate.plan.entity.Project;
import com.realestate.plan.entity.Tower;
import com.realestate.plan.enums.*;
import com.realestate.plan.repository.BuilderRepository;
import com.realestate.plan.repository.ProjectRepository;
import com.realestate.plan.repository.TowerRepository;
import com.realestate.plan.repository.FloorRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@Rollback
public class FloorIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private BuilderRepository builderRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TowerRepository towerRepository;

    @Autowired
    private FloorRepository floorRepository;

    private Builder savedBuilder;
    private Project savedProject;
    private Tower savedTower;

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

        Tower tower = Tower.builder()
                .towerCode("TWR-" + unique)
                .towerName("Test Tower")
                .towerType(TowerType.RESIDENTIAL)
                .towerStatus(TowerStatus.PLANNED)
                .liftCount(2)
                .totalFloors(10)
                .floorCapacity(4)
                .project(savedProject)
                .build();
        savedTower = towerRepository.save(tower);
    }

    @Test
    void createFloor_shouldPersistFloor() throws Exception {
        String payload = "{\n" +
                "  \"towerId\": " + savedTower.getId() + ",\n" +
                "  \"floorNumber\": 1,\n" +
                "  \"floorType\": \"FIRST\",\n" +
                "  \"floorStatus\": \"PLANNED\",\n" +
                "  \"totalFlats\": 4,\n" +
                "  \"builtUpArea\": 4500.5,\n" +
                "  \"areaUnit\": \"SQFT\"\n" +
                "}";

        mockMvc.perform(post("/api/v1/floors")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isCreated());

        assertThat(floorRepository.findByTowerIdAndIsDeletedFalse(savedTower.getId())).hasSize(1);
    }

    @Test
    void createFloor_duplicateFloorNumberReturnsConflict() throws Exception {
        String firstPayload = "{\n" +
                "  \"towerId\": " + savedTower.getId() + ",\n" +
                "  \"floorNumber\": 2,\n" +
                "  \"floorType\": \"SECOND\",\n" +
                "  \"floorStatus\": \"PLANNED\",\n" +
                "  \"totalFlats\": 4\n" +
                "}";

        mockMvc.perform(post("/api/v1/floors")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(firstPayload))
                .andExpect(status().isCreated());

        String duplicatePayload = "{\n" +
                "  \"towerId\": " + savedTower.getId() + ",\n" +
                "  \"floorNumber\": 2,\n" +
                "  \"floorType\": \"SECOND\",\n" +
                "  \"floorStatus\": \"UNDER_CONSTRUCTION\",\n" +
                "  \"totalFlats\": 4\n" +
                "}";

        mockMvc.perform(post("/api/v1/floors")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(duplicatePayload))
                .andExpect(status().isConflict());
    }

    @Test
    void updateFloor_shouldUpdateFloorStatus() throws Exception {
        String createPayload = "{\n" +
                "  \"towerId\": " + savedTower.getId() + ",\n" +
                "  \"floorNumber\": 3,\n" +
                "  \"floorType\": \"THIRD\",\n" +
                "  \"floorStatus\": \"PLANNED\",\n" +
                "  \"totalFlats\": 4\n" +
                "}";

        mockMvc.perform(post("/api/v1/floors")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(createPayload))
                .andExpect(status().isCreated());

        var floors = floorRepository.findByTowerIdAndIsDeletedFalse(savedTower.getId());
        assertThat(floors).hasSize(1);

        Long floorId = floors.get(0).getId();

        String updatePayload = "{\n" +
                "  \"towerId\": " + savedTower.getId() + ",\n" +
                "  \"floorNumber\": 3,\n" +
                "  \"floorType\": \"THIRD\",\n" +
                "  \"floorStatus\": \"UNDER_CONSTRUCTION\",\n" +
                "  \"totalFlats\": 4\n" +
                "}";

        mockMvc.perform(put("/api/v1/floors/" + floorId)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(updatePayload))
                .andExpect(status().isOk());
    }

    @Test
    void deleteFloor_shouldSoftDelete() throws Exception {
        String createPayload = "{\n" +
                "  \"towerId\": " + savedTower.getId() + ",\n" +
                "  \"floorNumber\": 4,\n" +
                "  \"floorType\": \"FOURTH\",\n" +
                "  \"floorStatus\": \"PLANNED\",\n" +
                "  \"totalFlats\": 4\n" +
                "}";

        mockMvc.perform(post("/api/v1/floors")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(createPayload))
                .andExpect(status().isCreated());

        var floors = floorRepository.findByTowerIdAndIsDeletedFalse(savedTower.getId());
        Long floorId = floors.get(0).getId();

        mockMvc.perform(delete("/api/v1/floors/" + floorId))
                .andExpect(status().isNoContent());

        var remainingFloors = floorRepository.findByTowerIdAndIsDeletedFalse(savedTower.getId());
        assertThat(remainingFloors).isEmpty();
    }

    @Test
    void getFloorsByTower_shouldReturnList() throws Exception {
        String payload1 = "{\n" +
                "  \"towerId\": " + savedTower.getId() + ",\n" +
                "  \"floorNumber\": 5,\n" +
                "  \"floorType\": \"FIFTH\",\n" +
                "  \"floorStatus\": \"PLANNED\",\n" +
                "  \"totalFlats\": 4\n" +
                "}";

        String payload2 = "{\n" +
                "  \"towerId\": " + savedTower.getId() + ",\n" +
                "  \"floorNumber\": 6,\n" +
                "  \"floorType\": \"SIXTH\",\n" +
                "  \"floorStatus\": \"PLANNED\",\n" +
                "  \"totalFlats\": 4\n" +
                "}";

        mockMvc.perform(post("/api/v1/floors")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(payload1))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/floors")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(payload2))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/floors/tower/" + savedTower.getId()))
                .andExpect(status().isOk());

        var floors = floorRepository.findByTowerIdAndIsDeletedFalse(savedTower.getId());
        assertThat(floors).hasSize(2);
    }
}
