package com.softserve.edu.manager.impl;

import com.softserve.edu.dao.CompetenceDao;
import com.softserve.edu.dao.GroupDao;
import com.softserve.edu.dao.GroupRepository;
import com.softserve.edu.entity.Competence;
import com.softserve.edu.entity.Group;
import com.softserve.edu.entity.User;
import com.softserve.edu.exception.GroupManagerException;
import com.softserve.edu.manager.GroupManager;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("groupManager")
public class GroupManagerImplementation implements GroupManager {

    private static final Logger logger = LoggerFactory
            .getLogger(GroupManagerImplementation.class);

    @Autowired
    GroupDao groupDao;

    @Autowired
    CompetenceDao competenceDao;

    @Autowired
    GroupRepository groupRepository;

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Group> findAllByCompetenceId(Long competenceId, boolean onlyOpened) {
        return groupDao.findByCompetenceId(competenceId, onlyOpened);
    }

    @Override
    @Transactional
    public Long create(String name, Date startDate, Date endDate,
            Long competenceId) throws GroupManagerException {

        Competence competence = competenceDao.findById(Competence.class,
                competenceId);

        Group aGroup = new Group();
        aGroup.setName(name);
        aGroup.setDateOpened(startDate);
        aGroup.setDateClosed(endDate);
        aGroup.setCompetence(competence);

        try {
            groupDao.save(aGroup);
            logger.info("Group was saved");
        } catch (Exception e) {
            logger.error("cannot save aGroup", e);
            throw new GroupManagerException("cannot save aGroup", e);
        }
        return aGroup.getId();
    }

    @Override
    @Transactional
    public void modify(final Long groupId, final String name, final Date start,
            final Date end, final Long competenceId)
            throws GroupManagerException {

        Competence competence = competenceDao.findById(Competence.class,
                competenceId);
        Group aGroup = groupDao.findById(Group.class, groupId);
        aGroup.setName(name);
        aGroup.setDateOpened(start);
        aGroup.setDateClosed(end);
        aGroup.setCompetence(competence);
        try {
            groupDao.update(aGroup);
            logger.info("Group was updated");
        } catch (Exception e) {
            logger.error("cannot updateUser aGroup", e);
            throw new GroupManagerException("cannot updateUser aGroup", e);
        }
    }

    @Override
    @Transactional
    public void create(Group aGroup) throws GroupManagerException {
        if (!validateGroup(aGroup)) {
            logger.warn("Group is not valid. Try another arguments");
            throw new GroupManagerException(
                    "Group is not valid. Try another arguments");
        }
        try {
            groupDao.save(aGroup);
            logger.info("Group was created");
        } catch (Exception e) {
            logger.error("Cannot createAchievementType aGroup", e);
            throw new GroupManagerException("Cannot createAchievementType aGroup", e);
        }
    }

    @Override
    @Transactional
    public void addUser(Long userId, Long groupId) throws GroupManagerException {
        try {
            groupDao.addUser(userId, groupId);
            logger.info("User was added");
        } catch (Exception e) {
            logger.error("cannot add user to group", e);
            throw new GroupManagerException("cannot add user to group", e);
        }
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<User> users(Long groupId) {

        return groupDao.userList(groupId);
    }

    @Override
    @Transactional
    public void deleteById(Long groupId) throws GroupManagerException {
        Group aGroup = groupDao.findById(Group.class, groupId);
        removeAssociation(aGroup);
        try {
            groupDao.delete(aGroup);
            logger.info("Group was deleted by id");
        } catch (Exception e) {
            logger.error("cannot deleteAchievementType aGroup", e);
            throw new GroupManagerException("cannot deleteAchievementType aGroup", e);
        }
    }

    @Override
    @Transactional
    public boolean validateGroup(Group group) {
        boolean isValid = false;
        Group existingGroup = groupRepository.findByName(group.getName());
        if (existingGroup != null) {
            logger.warn("Such group exists already", existingGroup.getName());
            return false;
        }
        if (group.getName().length() > 3 && group.getName().length() < 30) {
            if (group.getDateClosed().after(group.getDateOpened())) {
                logger.info("Group is valid");
                isValid = true;
            }
        }
        return isValid;
    }

    @Override
    @Transactional
    public void removeAssociation(Group group) {

        Competence competence = group.getCompetence();
        competence.getGroups().remove(group);

        Set<User> users = group.getUsers();
        for (User user : users) {
            user.getaGroups().remove(group);
        }
        users.clear();
        group.setUsers(users);

        logger.info("Group association was removed");
    }

}
