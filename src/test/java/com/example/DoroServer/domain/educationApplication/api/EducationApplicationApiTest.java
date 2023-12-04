package com.example.DoroServer.domain.educationApplication.api;

import static com.example.DoroServer.domain.educationApplication.EducationApplicationTestSetup.getEducationApplicationReq;
import static com.example.DoroServer.domain.educationApplication.EducationApplicationTestSetup.getEducationApplicationRes;
import static com.example.DoroServer.domain.educationApplication.EducationApplicationTestSetup.getRetrieveApplicationReq;
import static com.example.DoroServer.domain.educationApplication.EducationApplicationTestSetup.getUpdateEducationApplicationReq;
import static com.example.DoroServer.domain.educationApplication.EducationApplicationTestSetup.getUpdateEducationApplicationRes;
import static com.example.DoroServer.domain.educationApplicationClassGroup.ClassGroupTestSetup.getClassGroupReq;
import static com.example.DoroServer.domain.educationApplicationClassGroup.ClassGroupTestSetup.getClassGroupRes;
import static com.example.DoroServer.domain.educationApplicationClassGroup.ClassGroupTestSetup.getUpdateClassGroupReq;
import static com.example.DoroServer.domain.educationApplicationClassGroup.ClassGroupTestSetup.getUpdateClassGroupRes;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.DoroServer.domain.educationApplication.dto.EducationApplicationReq;
import com.example.DoroServer.domain.educationApplication.dto.EducationApplicationRes;
import com.example.DoroServer.domain.educationApplication.dto.RetrieveApplicationReq;
import com.example.DoroServer.domain.educationApplication.service.EducationApplicationService;
import com.example.DoroServer.domain.educationApplicationClassGroup.dto.ClassGroupReq;
import com.example.DoroServer.domain.educationApplicationClassGroup.dto.ClassGroupRes;
import com.example.DoroServer.domain.educationApplicationClassGroup.service.ClassGroupService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebMvcTest(EducationApplicationApi.class)
@AutoConfigureMockMvc(addFilters = false) // SecurityConfig를 적용하지 않음
public class EducationApplicationApiTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private EducationApplicationService educationApplicationService;

        @MockBean
        private ClassGroupService classGroupService;

        @MockBean
        private JpaMetamodelMappingContext jpaMetamodelMappingContext; // JPA auditing을 위한 Bean

        private static ObjectMapper mapper;

        @BeforeAll
        static void setUp() {
                mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
        }

        @Test
        @DisplayName("MockMvc를 통한 교육 신청 작성 테스트")
        public void createEducationApplicationTest() throws Exception {
                // given
                EducationApplicationReq applicationReq = getEducationApplicationReq();
                EducationApplicationRes educationApplicationRes = getEducationApplicationRes();

                given(educationApplicationService.save(any(EducationApplicationReq.class)))
                                .willReturn(educationApplicationRes);

                // when & then
                mockMvc.perform(post("/education-application")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(applicationReq)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data.id").value(educationApplicationRes.getId()))
                                .andExpect(jsonPath("$.data.name").value(educationApplicationRes.getName()))
                                .andExpect(jsonPath("$.data.institutionName")
                                                .value(educationApplicationRes.getInstitutionName()))
                                .andExpect(jsonPath("$.data.position").value(educationApplicationRes.getPosition()))
                                .andExpect(jsonPath("$.data.phoneNumber")
                                                .value(educationApplicationRes.getPhoneNumber()))
                                .andExpect(jsonPath("$.data.email").value(educationApplicationRes.getEmail()))
                                .andExpect(jsonPath("$.data.numberOfStudents")
                                                .value(educationApplicationRes.getNumberOfStudents()))
                                .andExpect(jsonPath("$.data.studentRank")
                                                .value(educationApplicationRes.getStudentRank()))
                                .andExpect(jsonPath("$.data.budget").value(educationApplicationRes.getBudget()))
                                .andExpect(jsonPath("$.data.overallRemark")
                                                .value(educationApplicationRes.getOverallRemark()));
        }

        @Test
        @DisplayName("MockMvc를 통한 신청한 교육 불러오기  테스트")
        public void retrieveApplicationTest() throws Exception {
                // given
                RetrieveApplicationReq retrieveApplicationReq = getRetrieveApplicationReq();
                EducationApplicationRes educationApplicationRes = getEducationApplicationRes();

                given(educationApplicationService.findByPhoneNumber(any(RetrieveApplicationReq.class)))
                                .willReturn(List.of(educationApplicationRes));

                // when & then
                mockMvc.perform(get("/education-application")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(retrieveApplicationReq)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data[0].id").value(educationApplicationRes.getId()))
                                .andExpect(jsonPath("$.data[0].name").value(educationApplicationRes.getName()))
                                .andExpect(jsonPath("$.data[0].institutionName")
                                                .value(educationApplicationRes.getInstitutionName()))
                                .andExpect(jsonPath("$.data[0].position").value(educationApplicationRes.getPosition()))
                                .andExpect(jsonPath("$.data[0].phoneNumber")
                                                .value(educationApplicationRes.getPhoneNumber()))
                                .andExpect(jsonPath("$.data[0].email").value(educationApplicationRes.getEmail()))
                                .andExpect(jsonPath("$.data[0].numberOfStudents")
                                                .value(educationApplicationRes.getNumberOfStudents()))
                                .andExpect(jsonPath("$.data[0].studentRank")
                                                .value(educationApplicationRes.getStudentRank()))
                                .andExpect(jsonPath("$.data[0].budget").value(educationApplicationRes.getBudget()))
                                .andExpect(jsonPath("$.data[0].overallRemark")
                                                .value(educationApplicationRes.getOverallRemark()));

        }

        @Test
        @DisplayName("MockMvc를 통한 신청한 교육 수정 테스트")
        void updateEducationApplicationTest() throws Exception {
                // given
                EducationApplicationReq applicationReq = getUpdateEducationApplicationReq();
                EducationApplicationRes educationApplicationRes = getUpdateEducationApplicationRes();

                given(educationApplicationService.update(any(Long.class), any(EducationApplicationReq.class)))
                                .willReturn(educationApplicationRes);

                // when & then
                mockMvc.perform(put("/education-application/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(applicationReq)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data.id").value(educationApplicationRes.getId()))
                                .andExpect(jsonPath("$.data.name").value(educationApplicationRes.getName()))
                                .andExpect(jsonPath("$.data.institutionName")
                                                .value(educationApplicationRes.getInstitutionName()))
                                .andExpect(jsonPath("$.data.position").value(educationApplicationRes.getPosition()))
                                .andExpect(jsonPath("$.data.phoneNumber")
                                                .value(educationApplicationRes.getPhoneNumber()))
                                .andExpect(jsonPath("$.data.email").value(educationApplicationRes.getEmail()))
                                .andExpect(jsonPath("$.data.numberOfStudents")
                                                .value(educationApplicationRes.getNumberOfStudents()))
                                .andExpect(jsonPath("$.data.studentRank")
                                                .value(educationApplicationRes.getStudentRank()))
                                .andExpect(jsonPath("$.data.budget").value(educationApplicationRes.getBudget()))
                                .andExpect(jsonPath("$.data.overallRemark")
                                                .value(educationApplicationRes.getOverallRemark()));
        }

        @Test
        @DisplayName("MockMvc를 통한 신청한 교육에 학급정보 추가 테스트")
        void addClassGroupToApplicationTest() throws Exception {
                // given
                ClassGroupReq classGroupReq = getClassGroupReq();
                ClassGroupRes classGroupRes = getClassGroupRes();

                given(classGroupService.addClassGroupToApplication(any(Long.class), any(ClassGroupReq.class)))
                                .willReturn(classGroupRes);

                // when & then
                mockMvc.perform(post("/education-application/1/classGroup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(classGroupReq)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data.id").value(classGroupRes.getId()))
                                .andExpect(jsonPath("$.data.className").value(classGroupRes.getClassName()))
                                .andExpect(jsonPath("$.data.educationConcept")
                                                .value(classGroupRes.getEducationConcept()))
                                .andExpect(jsonPath("$.data.numberOfStudents")
                                                .value(classGroupRes.getNumberOfStudents()))
                                .andExpect(jsonPath("$.data.remark").value(classGroupRes.getRemark()))
                                .andExpect(jsonPath("$.data.unfixed").value(classGroupRes.isUnfixed()));
        }

        @Test
        @DisplayName("MockMvc를 통한 신청한 교육에 학급정보 업데이트 테스트")
        void updateClassGroupTest() throws Exception {
                // given
                ClassGroupReq classGroupReq = getUpdateClassGroupReq();
                ClassGroupRes classGroupRes = getUpdateClassGroupRes();

                given(classGroupService.updateClassGroup(any(Long.class), any(Long.class), any(ClassGroupReq.class)))
                                .willReturn(classGroupRes);

                // when & then
                mockMvc.perform(put("/education-application/1/classGroup/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(classGroupReq)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data.id").value(classGroupRes.getId()))
                                .andExpect(jsonPath("$.data.className").value(classGroupRes.getClassName()))
                                .andExpect(jsonPath("$.data.educationConcept")
                                                .value(classGroupRes.getEducationConcept()))
                                .andExpect(jsonPath("$.data.numberOfStudents")
                                                .value(classGroupRes.getNumberOfStudents()))
                                .andExpect(jsonPath("$.data.remark").value(classGroupRes.getRemark()))
                                .andExpect(jsonPath("$.data.unfixed").value(classGroupRes.isUnfixed()));
        }

}
