package com.example.DoroServer.domain.educationApplication.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.lang.reflect.Field;
import java.util.List;

import static com.example.DoroServer.domain.educationApplication.EducationApplicationTestSetup.getEducationApplication;
import static com.example.DoroServer.domain.educationApplication.EducationApplicationTestSetup.getEducationApplicationReq;
import static com.example.DoroServer.domain.educationApplication.EducationApplicationTestSetup.getEducationApplicationRes;
import static com.example.DoroServer.domain.educationApplication.EducationApplicationTestSetup.getEducationApplicationResList;
import static com.example.DoroServer.domain.educationApplication.EducationApplicationTestSetup.getEducationApplications;
import static com.example.DoroServer.domain.educationApplication.EducationApplicationTestSetup.getUpdateEducationApplication;
import static com.example.DoroServer.domain.educationApplication.EducationApplicationTestSetup.getUpdateEducationApplicationReq;
import static com.example.DoroServer.domain.educationApplication.EducationApplicationTestSetup.UPDATED_NAME;
import static com.example.DoroServer.domain.educationApplication.EducationApplicationTestSetup.UPDATED_POSITION;
import static com.example.DoroServer.domain.educationApplication.EducationApplicationTestSetup.UPDATED_OVERALL_REMARK;

import com.example.DoroServer.domain.educationApplication.entity.EducationApplication;
import com.example.DoroServer.global.config.ModelMapperConfig;

@SpringBootTest(classes = { EducationApplicationMapper.class, ModelMapperConfig.class })
public class EducationApplicationMapperTest {

    @Autowired
    private EducationApplicationMapper mapper;

    @DisplayName("교육 신청 toEntity Mapper 테스트")
    @Test
    void testToEntity() {
        // given
        EducationApplicationReq applicationReq = getEducationApplicationReq();
        EducationApplication educationApplication = getEducationApplication();

        // when
        EducationApplication result = mapper.toEntity(applicationReq);

        // then
        for (Field field : EducationApplication.class.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getName().equals("id")) {
                continue;
            }
            try {
                assertEquals(field.get(educationApplication), field.get(result));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @DisplayName("교육 신청 업데이트 toEntity Mapper 테스트")
    @Test
    void testToEntityWithDestination() {
        // given
        EducationApplicationReq updateApplicationReq = getUpdateEducationApplicationReq();
        EducationApplication educationApplication = getEducationApplication();
        EducationApplication updateEducationApplication = getUpdateEducationApplication();

        // when
        EducationApplication result = mapper.toEntity(updateApplicationReq, educationApplication);

        // then
        for (Field field : EducationApplication.class.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getName().equals("id")) {
                continue;
            }
            try {
                assertEquals(field.get(updateEducationApplication), field.get(result));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        assertThat(result.getName()).isEqualTo(UPDATED_NAME);
        assertThat(result.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(result.getOverallRemark()).isEqualTo(UPDATED_OVERALL_REMARK);
    }

    @DisplayName("교육 신청 toDTO Mapper 테스트")
    @Test
    void testToDTO() {
        // given
        EducationApplication educationApplication = getEducationApplication();
        EducationApplicationRes educationApplicationRes = getEducationApplicationRes();

        // when
        EducationApplicationRes result = mapper.toDTO(educationApplication);

        // then
        for (Field field : EducationApplicationRes.class.getDeclaredFields()) {
            field.setAccessible(true);

            try {
                assertEquals(field.get(educationApplicationRes), field.get(result));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @DisplayName("교육 신청 toDTO Mapper 테스트 (List 파라미터)")
    @Test
    void testToDTOList() {
        // given
        List<EducationApplication> educationApplications = getEducationApplications();
        List<EducationApplicationRes> educationApplicationResList = getEducationApplicationResList();

        // when
        List<EducationApplicationRes> result = mapper.toDTO(educationApplications);

        // then
        assertEquals(educationApplicationResList.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            for (Field field : EducationApplicationRes.class.getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    assertEquals(field.get(educationApplicationResList.get(i)), field.get(result.get(i)));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}