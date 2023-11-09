package com.uni.timetable.service;

import com.uni.timetable.model.Classes;
import com.uni.timetable.model.SemesterClasses;
import com.uni.timetable.model.SemesterType;
import com.uni.timetable.model.StudyType;
import com.uni.timetable.repository.ClassesRepository;
import com.uni.timetable.repository.SemesterClassesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SemesterClassesService {

    private final SemesterClassesRepository semesterClassesRepository;

    public SemesterClassesService(SemesterClassesRepository semesterClassesRepository) {
        this.semesterClassesRepository = semesterClassesRepository;
    }

    public List<SemesterClasses> findAll() {
        log.debug("Finding all classes");
        List<SemesterClasses> semesterClasses = semesterClassesRepository.findAll();
        log.debug("Semester classes found" + semesterClasses);
        return semesterClasses;
    }

    public List<SemesterClasses> findSemesterClassesByLecturerAndSemester(String lecturerName, String academicYear, SemesterType semesterType) {
        log.debug("Finding classes by lecturer name, semester type and academic year " + lecturerName + ", " + semesterType + ", " + academicYear);
        List<SemesterClasses> semesterClasses = semesterClassesRepository.findBySemester_AcademicYearAndSemester_SemesterTypeAndClasses_LecturerId_Name(academicYear, semesterType, lecturerName);
        log.debug("Classes found " + semesterClasses);
        return semesterClasses;
    }

    public List<SemesterClasses> findSemesterClassesByFullTimeMajorAndSemester(String majorName, String studyYear, SemesterType semesterType) {
        return findSemesterClassesByMajorSemesterAndStudyType(majorName, studyYear, semesterType, StudyType.FULL_TIME);
    }

    public List<SemesterClasses> findSemesterClassesByPartTimeMajorAndSemester(String majorName, String studyYear, SemesterType semesterType) {
        return findSemesterClassesByMajorSemesterAndStudyType(majorName, studyYear, semesterType, StudyType.PART_TIME);
    }

    private List<SemesterClasses> findSemesterClassesByMajorSemesterAndStudyType(String majorName, String studyYear, SemesterType semesterType, StudyType studyType) {
        log.debug("Finding classes by major name, semester type, study year and study type " + majorName + ", " + semesterType + ", " + studyYear + ", " + studyType);
        List<SemesterClasses> semesterClasses = semesterClassesRepository.findByClasses_MajorGroup_StudyYearAndSemester_SemesterType(Integer.valueOf(studyYear), semesterType);
        semesterClasses = semesterClasses.stream().filter(classes ->
                classes.getClasses().getMajorGroup().getMajor().getMajorName().equals(majorName) &&
                        classes.getClasses().getMajorGroup().getMajor().getStudyType().equals(studyType)).toList();
        log.debug("Classes found " + semesterClasses);
        return semesterClasses;
    }

    public List<SemesterClasses> findSemesterClassesByDepartmentAndClassroom(String departmentName, String classroomName) {
        return semesterClassesRepository.findByClasses_DepartmentClassroom_Department_DepartmentNameAndClasses_DepartmentClassroom_Classroom_ClassroomName(departmentName, classroomName);
    }
}
