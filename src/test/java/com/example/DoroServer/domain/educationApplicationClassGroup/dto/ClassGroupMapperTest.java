package com.example.DoroServer.domain.educationApplicationClassGroup.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.DoroServer.domain.educationApplicationClassGroup.entity.ClassGroup;
import com.example.DoroServer.global.config.ModelMapperConfig;

import java.lang.reflect.Field;

import static com.example.DoroServer.domain.educationApplicationClassGroup.ClassGroupTestSetup.getClassGroup;
import static com.example.DoroServer.domain.educationApplicationClassGroup.ClassGroupTestSetup.getClassGroupReq;
import static com.example.DoroServer.domain.educationApplicationClassGroup.ClassGroupTestSetup.getClassGroupRes;
import static com.example.DoroServer.domain.educationApplicationClassGroup.ClassGroupTestSetup.getUpdateClassGroup;
import static com.example.DoroServer.domain.educationApplicationClassGroup.ClassGroupTestSetup.getUpdateClassGroupReq;

@SpringBootTest(classes = { ClassGroupMapper.class, ModelMapperConfig.class })
public class ClassGroupMapperTest {

    @Autowired
    private ClassGroupMapper mapper;

    @DisplayName("학급정보 toEntity Mapper 테스트")
    @Test
    void testToEntity() {
        // given
        ClassGroupReq classGroupReq = getClassGroupReq();

        ClassGroup classGroup = getClassGroup();

        // when
        ClassGroup result = mapper.toEntity(classGroupReq);

        // then
        for (Field field : ClassGroup.class.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getName().equals("id")) {
                continue;
            }
            try {
                assertEquals(field.get(classGroup), field.get(result));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @DisplayName("학급정보 업데이트 toEntity Mapper 테스트")
    @Test
    void testToEntityWithDestination() {
        // given
        ClassGroupReq classGroupReq = getUpdateClassGroupReq();
        ClassGroup classGroup = getClassGroup();
        ClassGroup updateClassGroup = getUpdateClassGroup();

        // when
        ClassGroup result = mapper.toEntity(classGroupReq, classGroup);

        // then
        for (Field field : ClassGroup.class.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getName().equals("id")) {
                continue;
            }
            try {
                assertEquals(field.get(updateClassGroup), field.get(result));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        assertThat(result.getNumberOfStudents()).isEqualTo(20);
        assertThat(result.getRemark()).isEqualTo("오후 1시-3시");
    }

    @DisplayName("학급정보 toDTO Mapper 테스트")
    @Test
    void testToDTO() {
        // given
        ClassGroup classGroup = getClassGroup();
        ClassGroupRes classGroupRes = getClassGroupRes();

        // when
        ClassGroupRes result = mapper.toDTO(classGroup);

        // then
        for (Field field : ClassGroupRes.class.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                assertEquals(field.get(classGroupRes), field.get(result));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}