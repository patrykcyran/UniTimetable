package com.uni.timetable.service;

import com.uni.timetable.model.*;
import com.uni.timetable.repository.PartTimeSemesterClassesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class PartTimeSemesterClassesService {

    private final PartTimeSemesterClassesRepository partTimeSemesterClassesRepository;

    public PartTimeSemesterClassesService(PartTimeSemesterClassesRepository partTimeSemesterClassesRepository) {
        this.partTimeSemesterClassesRepository = partTimeSemesterClassesRepository;
    }

    public List<PartTimeSemesterClasses> findAll() {
        log.debug("Finding all classes");
        List<PartTimeSemesterClasses> semesterClasses = partTimeSemesterClassesRepository.findAll();
        log.debug("Semester classes found" + semesterClasses);
        return semesterClasses;
    }

    public List<PartTimeSemesterClasses> findPartTimeSemesterClassesByMajorAndSemester(String majorName, String studyYear, SemesterType semesterType) {
        log.debug("Finding part time classes by major name, semester type, study year " + majorName + ", " + semesterType + ", " + studyYear);
        List<PartTimeSemesterClasses> partTimeSemesterClasses = partTimeSemesterClassesRepository.findByClasses_MajorGroup_Major_MajorNameAndClasses_MajorGroup_StudyYearAndSemester_SemesterType(majorName, Integer.valueOf(studyYear), semesterType);
        log.debug("Classes found {}", partTimeSemesterClasses);
        return partTimeSemesterClasses;
    }

    public List<PartTimeSemesterClasses> findPartTimeSemesterClassesByMajorSemesterAndGroup(String majorName, String studyYear, SemesterType semesterType, String group) {
        log.debug("Finding part time classes by major name, semester type, study year " + majorName + ", " + semesterType + ", " + studyYear);
        List<PartTimeSemesterClasses> partTimeSemesterClasses = partTimeSemesterClassesRepository.findByClasses_MajorGroup_Major_MajorNameAndClasses_MajorGroup_StudyYearAndSemester_SemesterTypeAndClasses_MajorGroup_Group_GroupName(majorName, Integer.valueOf(studyYear), semesterType, group);
        log.debug("Classes found {}", partTimeSemesterClasses);
        return partTimeSemesterClasses;
    }

    public List<PartTimeSemesterClasses> findPartTimeSemesterClassesBySemester(String studyYear, SemesterType semesterType) {
        log.debug("Finding part time classes by semester type and study year " + semesterType + ", " + studyYear);
        List<PartTimeSemesterClasses> partTimeSemesterClasses = partTimeSemesterClassesRepository.findBySemester_AcademicYearAndSemester_SemesterType(studyYear, semesterType);
        log.debug("Classes found {}", partTimeSemesterClasses);
        return partTimeSemesterClasses;
    }

    public PartTimeSemesterClasses createPartTimeSemesterClasses(Semester semester,
                                                               Classes classes,
                                                               LocalDate classesDate) {
        return PartTimeSemesterClasses.builder()
                .semester(semester)
                .classes(classes)
                .classesDate(classesDate)
                .build();
    }

    public PartTimeSemesterClasses savePartTimeSemesterClasses(Semester semester,
                                               Classes classes,
                                               LocalDate classesDate) {
        PartTimeSemesterClasses partTimeSemesterClasses = PartTimeSemesterClasses.builder()
                .semester(semester)
                .classes(classes)
                .classesDate(classesDate)
                .build();

        log.debug("Part Time Semester classes to save {} ", partTimeSemesterClasses);
        partTimeSemesterClassesRepository.save(partTimeSemesterClasses);
        return partTimeSemesterClasses;
    }

    public void deletePartTimeSemesterClassesById(Long id) {
        log.debug("Deleting part time semester classes by id {}", id);
        partTimeSemesterClassesRepository.deleteByPartTimeSemesterClassesId(id);
    }

    public PartTimeSemesterClasses findById(Long id) {
        return partTimeSemesterClassesRepository.findByPartTimeSemesterClassesId(id);
    }

    public void update(PartTimeSemesterClasses partTimeSemesterClasses) {
        log.debug("Updating part time semester classes {}", partTimeSemesterClasses);
        partTimeSemesterClassesRepository.updateSemesterAndClassesAndClassesDateByPartTimeSemesterClassesId(partTimeSemesterClasses.getSemester(), partTimeSemesterClasses.getClasses(), partTimeSemesterClasses.getClassesDate(), partTimeSemesterClasses.getPartTimeSemesterClassesId());
    }

    public List<PartTimeSemesterClasses> findAllPartTimeClassesByClassroomAndDepartment(String classroom, String department) {
        return partTimeSemesterClassesRepository.findByClasses_DepartmentClassroom_Classroom_ClassroomNameAndClasses_DepartmentClassroom_Department_DepartmentName(classroom, department);
    }

    public List<PartTimeSemesterClasses> findAllPartTimeClassesByGroupAndMajor(String group, String major) {
        return partTimeSemesterClassesRepository.findByClasses_MajorGroup_Group_GroupNameAndClasses_MajorGroup_Major_MajorName(group, major);
    }

    public List<PartTimeSemesterClasses> findAllToGetHoursWhenAdding(String majorName, Integer studyYear, String groupName, String subjectName, SemesterType semesterType, Boolean isDiploma, String academicYear) {
        return partTimeSemesterClassesRepository.findByClasses_MajorGroup_Major_MajorNameAndClasses_MajorGroup_StudyYearAndClasses_MajorGroup_Group_GroupNameAndClasses_Subject_SubjectNameAndSemester_SemesterTypeAndSemester_IsDiplomaAndSemester_AcademicYear(majorName, studyYear, groupName, subjectName, semesterType, isDiploma, academicYear);
    }
}
