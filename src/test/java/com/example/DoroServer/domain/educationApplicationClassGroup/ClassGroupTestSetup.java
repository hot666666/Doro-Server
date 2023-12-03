package com.example.DoroServer.domain.educationApplicationClassGroup;

import com.example.DoroServer.domain.educationApplicationClassGroup.dto.ClassGroupReq;
import com.example.DoroServer.domain.educationApplicationClassGroup.dto.ClassGroupRes;
import com.example.DoroServer.domain.educationApplicationClassGroup.entity.ClassGroup;

import java.time.LocalDate;
import java.util.List;

public class ClassGroupTestSetup {
    public static Long ID = 1L;
    public static String CLASS_NAME = "1학년 1반";
    public static String EDUCATION_CONCEPT = "Q* 알고리즘";
    public static int NUMBER_OF_STUDENTS = 15;
    public static int UPDATED_NUMBER_OF_STUDENTS = 20;
    public static List<LocalDate> EDUCATION_DATES = List.of(
            LocalDate.parse("2021-08-01"),
            LocalDate.parse("2021-08-08"),
            LocalDate.parse("2021-08-15"));
    public static String REMARK = "오후 2시-4시";
    public static String UPDATED_REMARK = "오후 1시-3시";
    public static boolean UNFIXED = true;

    public static ClassGroupReq getClassGroupReq() {
        return ClassGroupReq.builder()
                .className(CLASS_NAME)
                .educationConcept(EDUCATION_CONCEPT)
                .numberOfStudents(NUMBER_OF_STUDENTS)
                .remark(REMARK)
                .educationDates(EDUCATION_DATES)
                .unfixed(UNFIXED)
                .build();
    }

    public static ClassGroupReq getUpdateClassGroupReq() {
        return ClassGroupReq.builder()
                .className(CLASS_NAME)
                .educationConcept(EDUCATION_CONCEPT)
                .numberOfStudents(UPDATED_NUMBER_OF_STUDENTS)
                .remark(UPDATED_REMARK)
                .educationDates(EDUCATION_DATES)
                .unfixed(UNFIXED)
                .build();
    }

    public static ClassGroupRes getClassGroupRes() {
        return ClassGroupRes.builder()
                .id(ID)
                .className(CLASS_NAME)
                .educationConcept(EDUCATION_CONCEPT)
                .numberOfStudents(NUMBER_OF_STUDENTS)
                .remark(REMARK)
                .educationDates(EDUCATION_DATES)
                .unfixed(UNFIXED)
                .build();
    }

    public static ClassGroupRes getUpdateClassGroupRes() {
        return ClassGroupRes.builder()
                .id(ID)
                .className(CLASS_NAME)
                .educationConcept(EDUCATION_CONCEPT)
                .numberOfStudents(UPDATED_NUMBER_OF_STUDENTS)
                .remark(UPDATED_REMARK)
                .educationDates(EDUCATION_DATES)
                .unfixed(UNFIXED)
                .build();
    }

    public static ClassGroup getClassGroup() {
        return ClassGroup.builder()
                .id(ID)
                .className(CLASS_NAME)
                .educationConcept(EDUCATION_CONCEPT)
                .numberOfStudents(NUMBER_OF_STUDENTS)
                .remark(REMARK)
                .educationDates(EDUCATION_DATES)
                .unfixed(UNFIXED)
                .build();
    }

    public static ClassGroup getUpdateClassGroup() {
        return ClassGroup.builder()
                .id(ID)
                .className(CLASS_NAME)
                .educationConcept(EDUCATION_CONCEPT)
                .numberOfStudents(UPDATED_NUMBER_OF_STUDENTS)
                .remark(UPDATED_REMARK)
                .educationDates(EDUCATION_DATES)
                .unfixed(UNFIXED)
                .build();
    }
}
