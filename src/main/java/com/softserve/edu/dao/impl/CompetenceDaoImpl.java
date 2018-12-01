package com.softserve.edu.dao.impl;

import com.softserve.edu.dao.CompetenceDao;
import com.softserve.edu.dao.GroupDao;
import com.softserve.edu.dao.UserDao;
import com.softserve.edu.entity.Competence;
import com.softserve.edu.entity.Group;
import com.softserve.edu.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("competenceDao")
public class CompetenceDaoImpl extends
        GenericDaoImpl<Competence> implements CompetenceDao {

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private UserDao userDao;

    @Override
    public List<Group> findGroupsByCompetenceId(int competenceId) {
        return groupDao.findEntityList(Competence.FIND_GROUPS_BY_COMPETENCE_ID, competenceId);
    }

    @Override
    public List<Group> findGroupsByCompetenceUuid(String competenceUuid) {
        return groupDao.findEntityList(
                Competence.FIND_GROUPS_BY_COMPETENCE_UUID, competenceUuid);

    }

    @Override
    public Competence findByName(String name) {
        return findEntity(Competence.FIND_COMPETENCE_BY_NAME, name);
    }

    @Override
    public List<Competence> findCompetencesByUserId(Long userId) {
        User user = userDao.findById(User.class, userId);
        return new ArrayList<>(user.getCompetences());
    }

    @Override
    public List<Competence> findByUserUuid(String userUuid) {
        User user = userDao.findByUuid(User.class, userUuid);
        return new ArrayList<>(user.getCompetences());
    }

    @Override
    public List<Competence> findAllCompetences() {
        return findAll(Competence.class);
    }

}
