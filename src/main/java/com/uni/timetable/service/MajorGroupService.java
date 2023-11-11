package com.uni.timetable.service;

import com.uni.timetable.model.Department;
import com.uni.timetable.model.MajorGroup;
import com.uni.timetable.repository.DepartmentRepository;
import com.uni.timetable.repository.MajorGroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MajorGroupService {

    private final MajorGroupRepository majorGroupRepository;

    public MajorGroupService(MajorGroupRepository majorGroupRepository) {
        this.majorGroupRepository = majorGroupRepository;
    }

    public List<MajorGroup> findAll() {
        log.debug("Find all major groups");
        List<MajorGroup> majorGroups = majorGroupRepository.findAll();
        log.debug("Major groups found" + majorGroups);
        return majorGroups;
    }

    public MajorGroup findByMajorAndGroupNames(String majorName, String groupName) {
        MajorGroup majorGroup = majorGroupRepository.findByMajor_MajorNameAndGroup_GroupName(majorName, groupName);
        log.debug("Major group found {}", majorGroup);
        return majorGroup;
    }
}
