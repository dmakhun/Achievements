package com.softserve.edu.manager;

import com.softserve.edu.entity.Group;
import com.softserve.edu.exception.GroupManagerException;

import java.time.LocalDate;

public interface GroupManager {

    Long create(String name, LocalDate startDate,
                LocalDate endDate, Long competenceId) throws GroupManagerException;

    void modify(Long groupId, String name, LocalDate start,
                LocalDate end, Long competenceId) throws GroupManagerException;

    void addUser(Long userId, Long groupId) throws GroupManagerException;

    void create(Group aGroup) throws GroupManagerException;

    boolean validateGroup(Group aGroup);

    void removeAssociation(Group aGroup);


}
