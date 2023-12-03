package com.example.DoroServer.domain.educationApplicationClassGroup.service;

import static com.example.DoroServer.domain.educationApplicationClassGroup.ClassGroupTestSetup.getClassGroup;
import static com.example.DoroServer.domain.educationApplicationClassGroup.ClassGroupTestSetup.getClassGroupReq;
import static com.example.DoroServer.domain.educationApplicationClassGroup.ClassGroupTestSetup.getClassGroupRes;
import static com.example.DoroServer.domain.educationApplicationClassGroup.ClassGroupTestSetup.getUpdateClassGroupReq;
import static com.example.DoroServer.domain.educationApplicationClassGroup.ClassGroupTestSetup.getUpdateClassGroupRes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import static com.example.DoroServer.domain.educationApplication.EducationApplicationTestSetup.getEducationApplication;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.DoroServer.domain.educationApplication.entity.EducationApplication;
import com.example.DoroServer.domain.educationApplication.repository.EducationApplicationRepository;
import com.example.DoroServer.domain.educationApplicationClassGroup.dto.ClassGroupMapper;
import com.example.DoroServer.domain.educationApplicationClassGroup.dto.ClassGroupReq;
import com.example.DoroServer.domain.educationApplicationClassGroup.dto.ClassGroupRes;
import com.example.DoroServer.domain.educationApplicationClassGroup.entity.ClassGroup;
import com.example.DoroServer.domain.educationApplicationClassGroup.repository.ClassGroupRepository;

@ExtendWith(MockitoExtension.class)
public class ClassGroupServiceTest {

        @InjectMocks
        private ClassGroupService classGroupService;

        @Mock
        private EducationApplicationRepository educationApplicationRepository;

        @Mock
        private ClassGroupRepository classGroupRepository;

        @Mock
        private ClassGroupMapper mapper;

        @Test
        @DisplayName("교육 신청서에 학급 정보 추가 테스트")
        void testAddClassGroup() {
                // given
                EducationApplication educationApplication = getEducationApplication();
                ClassGroupReq classGroupReq = getClassGroupReq();
                ClassGroup classGroup = getClassGroup();
                ClassGroupRes classGroupRes = getClassGroupRes();

                given(educationApplicationRepository.findById(educationApplication.getId()))
                                .willReturn(Optional.of(educationApplication));
                given(mapper.toEntity(classGroupReq)).willReturn(classGroup);
                given(classGroupRepository.save(classGroup)).willReturn(classGroup);
                given(mapper.toDTO(classGroup)).willReturn(classGroupRes);

                // when
                ClassGroupRes savedClassGroup = classGroupService.addClassGroupToApplication(
                                educationApplication.getId(),
                                classGroupReq);

                // then
                verify(educationApplicationRepository, times(1)).findById(educationApplication.getId());
                verify(mapper, times(1)).toEntity(classGroupReq);
                verify(classGroupRepository, times(1)).save(classGroup);
                verify(mapper, times(1)).toDTO(classGroup);
                assertThat(savedClassGroup.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("교육 신청서에 학급 정보 수정 테스트")
        void testUpdateClassGroup() {
                // given
                EducationApplication educationApplication = getEducationApplication();
                ClassGroupReq classGroupReq = getUpdateClassGroupReq();
                ClassGroup classGroup = getClassGroup();
                ClassGroupRes classGroupRes = getUpdateClassGroupRes();

                given(classGroupRepository.findById(classGroup.getId())).willReturn(Optional.of(classGroup));
                given(mapper.toEntity(classGroupReq, classGroup)).willReturn(classGroup);
                given(classGroupRepository.save(classGroup)).willReturn(classGroup);
                given(mapper.toDTO(classGroup)).willReturn(classGroupRes);

                // when
                ClassGroupRes updatedClassGroup = classGroupService.updateClassGroup(educationApplication.getId(),
                                classGroup.getId(), classGroupReq);

                // then
                verify(classGroupRepository, times(1)).findById(classGroup.getId());
                verify(mapper, times(1)).toEntity(classGroupReq, classGroup);
                verify(classGroupRepository, times(1)).save(classGroup);
                verify(mapper, times(1)).toDTO(classGroup);

                assertThat(updatedClassGroup.getNumberOfStudents()).isEqualTo(classGroupRes.getNumberOfStudents());
        }

}
