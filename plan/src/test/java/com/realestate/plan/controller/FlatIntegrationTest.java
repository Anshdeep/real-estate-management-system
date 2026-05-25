package com.realestate.plan.controller;

import com.realestate.plan.entity.Builder;
import com.realestate.plan.entity.Project;
import com.realestate.plan.entity.Tower;
import com.realestate.plan.repository.BuilderRepository;
import com.realestate.plan.repository.ProjectRepository;
import com.realestate.plan.repository.TowerRepository;
import com.realestate.plan.repository.FloorRepository;
import com.realestate.plan.repository.FlatRepository;
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
public class FlatIntegrationTest {

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

    @Autowired
    private FlatRepository flatRepository;

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
                .type(com.realestate.plan.enums.BuilderType.PRIVATE_LIMITED)
                .status(com.realestate.plan.enums.BuilderStatus.ACTIVE)
                .primaryPhone("999999" + (int) (Math.random() * 10000))
                .emailId("testbuilder+" + unique + "@example.com")
                .build();
        savedBuilder = builderRepository.save(builder);

        Project project = Project.builder()
                .projectCode("PRJ-" + unique)
                .projectName("Test Project")
                .status(com.realestate.plan.enums.ProjectStatus.PLANNED)
                .builder(savedBuilder)
                .build();
        savedProject = projectRepository.save(project);

        Tower tower = Tower.builder()
                .towerCode("TWR-" + unique)
                .towerName("Test Tower")
                .towerType(com.realestate.plan.enums.TowerType.RESIDENTIAL)
                .towerStatus(com.realestate.plan.enums.TowerStatus.PLANNED)
                .liftCount(2)
                .totalFloors(10)
                .floorCapacity(4)
                .project(savedProject)
                .build();
        savedTower = towerRepository.save(tower);
    }

    @Test
    void createFlat_shouldPersistFlat() throws Exception {
        var floors = floorRepository.findByTowerIdAndIsDeletedFalse(savedTower.getId());
        if (floors.isEmpty()) {
            // create a floor for the tower if not present
            String floorPayload = "{\n" +
                    "  \"towerId\": " + savedTower.getId() + ",\n" +
                    "  \"floorNumber\": 1,\n" +
                    "  \"floorType\": \"FIRST\",\n" +
                    "  \"floorStatus\": \"PLANNED\",\n" +
                    "  \"totalFlats\": 4\n" +
                    "}";

            mockMvc.perform(post("/api/v1/floors")
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .content(floorPayload))
                    .andExpect(status().isCreated());
        }

        var floor = floorRepository.findByTowerIdAndIsDeletedFalse(savedTower.getId()).get(0);

        String payload = "{\n" +
                "  \"floorId\": " + floor.getId() + ",\n" +
                "  \"flatNumber\": \"A-101\",\n" +
                "  \"bhkType\": \"TWO_BHK\",\n" +
                "  \"flatStatus\": \"AVAILABLE\",\n" +
                "  \"builtUpArea\": 1100.0,\n" +
                "  \"superBuiltUpArea\": 1250.0,\n" +
                "  \"carpetArea\": 900.0,\n" +
                "  \"areaUnit\": \"SQFT\"\n" +
                "}";

        mockMvc.perform(post("/api/v1/flats")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isCreated());

        assertThat(flatRepository.findByFloorIdAndIsDeletedFalse(floor.getId())).hasSize(1);
    }

    @Test
    void createFlat_duplicateFlatNumberReturnsConflict() throws Exception {
        var floors = floorRepository.findByTowerIdAndIsDeletedFalse(savedTower.getId());
        if (floors.isEmpty()) {
            String floorPayload = "{\n" +
                    "  \"towerId\": " + savedTower.getId() + ",\n" +
                    "  \"floorNumber\": 2,\n" +
                    "  \"floorType\": \"SECOND\",\n" +
                    "  \"floorStatus\": \"PLANNED\",\n" +
                    "  \"totalFlats\": 4\n" +
                    "}";

            mockMvc.perform(post("/api/v1/floors")
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .content(floorPayload))
                    .andExpect(status().isCreated());
        }

        var floor = floorRepository.findByTowerIdAndIsDeletedFalse(savedTower.getId()).get(0);

        String firstPayload = "{\n" +
                "  \"floorId\": " + floor.getId() + ",\n" +
                "  \"flatNumber\": \"B-201\",\n" +
                "  \"bhkType\": \"TWO_BHK\",\n" +
                "  \"flatStatus\": \"AVAILABLE\"\n" +
                "}";

        mockMvc.perform(post("/api/v1/flats")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(firstPayload))
                .andExpect(status().isCreated());

        String duplicatePayload = "{\n" +
                "  \"floorId\": " + floor.getId() + ",\n" +
                "  \"flatNumber\": \"B-201\",\n" +
                "  \"bhkType\": \"TWO_BHK\",\n" +
                "  \"flatStatus\": \"RESERVED\"\n" +
                "}";

        mockMvc.perform(post("/api/v1/flats")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(duplicatePayload))
                .andExpect(status().isConflict());
    }

    @Test
    void updateFlat_shouldUpdateFlatStatus() throws Exception {
        var floors = floorRepository.findByTowerIdAndIsDeletedFalse(savedTower.getId());
        if (floors.isEmpty()) {
            String floorPayload = "{\n" +
                    "  \"towerId\": " + savedTower.getId() + ",\n" +
                    "  \"floorNumber\": 3,\n" +
                    "  \"floorType\": \"THIRD\",\n" +
                    "  \"floorStatus\": \"PLANNED\",\n" +
                    "  \"totalFlats\": 4\n" +
                    "}";

            mockMvc.perform(post("/api/v1/floors")
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .content(floorPayload))
                    .andExpect(status().isCreated());
        }

        var floor = floorRepository.findByTowerIdAndIsDeletedFalse(savedTower.getId()).get(0);

        String createPayload = "{\n" +
                "  \"floorId\": " + floor.getId() + ",\n" +
                "  \"flatNumber\": \"C-301\",\n" +
                "  \"bhkType\": \"THREE_BHK\",\n" +
                "  \"flatStatus\": \"AVAILABLE\"\n" +
                "}";

        mockMvc.perform(post("/api/v1/flats")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(createPayload))
                .andExpect(status().isCreated());

        var flats = flatRepository.findByFloorIdAndIsDeletedFalse(floor.getId());
        assertThat(flats).hasSize(1);

        Long flatId = flats.get(0).getId();

        String updatePayload = "{\n" +
                "  \"floorId\": " + floor.getId() + ",\n" +
                "  \"flatNumber\": \"C-301\",\n" +
                "  \"bhkType\": \"THREE_BHK\",\n" +
                "  \"flatStatus\": \"BOOKED\"\n" +
                "}";

        mockMvc.perform(put("/api/v1/flats/" + flatId)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(updatePayload))
                .andExpect(status().isOk());
    }

    @Test
    void deleteFlat_shouldSoftDelete() throws Exception {
        var floors = floorRepository.findByTowerIdAndIsDeletedFalse(savedTower.getId());
        if (floors.isEmpty()) {
            String floorPayload = "{\n" +
                    "  \"towerId\": " + savedTower.getId() + ",\n" +
                    "  \"floorNumber\": 4,\n" +
                    "  \"floorType\": \"FOURTH\",\n" +
                    "  \"floorStatus\": \"PLANNED\",\n" +
                    "  \"totalFlats\": 4\n" +
                    "}";

            mockMvc.perform(post("/api/v1/floors")
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .content(floorPayload))
                    .andExpect(status().isCreated());
        }

        var floor = floorRepository.findByTowerIdAndIsDeletedFalse(savedTower.getId()).get(0);

        String createPayload = "{\n" +
                "  \"floorId\": " + floor.getId() + ",\n" +
                "  \"flatNumber\": \"D-401\",\n" +
                "  \"bhkType\": \"ONE_BHK\",\n" +
                "  \"flatStatus\": \"AVAILABLE\"\n" +
                "}";

        mockMvc.perform(post("/api/v1/flats")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(createPayload))
                .andExpect(status().isCreated());

        var flats = flatRepository.findByFloorIdAndIsDeletedFalse(floor.getId());
        Long flatId = flats.get(0).getId();

        mockMvc.perform(delete("/api/v1/flats/" + flatId))
                .andExpect(status().isNoContent());

        var remaining = flatRepository.findByFloorIdAndIsDeletedFalse(floor.getId());
        assertThat(remaining).isEmpty();
    }
}
