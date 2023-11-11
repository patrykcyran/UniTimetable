package com.uni.timetable.service;

import com.uni.timetable.model.Group;
import com.uni.timetable.model.Major;
import com.uni.timetable.repository.GroupRepository;
import com.uni.timetable.repository.MajorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GroupService {

    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public List<Group> findAll() {
        log.debug("Finding all groups");
        List<Group> groups = groupRepository.findAll();
        log.debug("Groups found" + groups);
        return groups;
    }

    public List<String> findAllGroupsNames() {
        log.debug("Finding all groups names");
        List<String> groupsNames = groupRepository.findAll().stream().map(Group::getGroupName).toList();
        log.debug("Groups names found" + groupsNames);
        return groupsNames;
    }
}
