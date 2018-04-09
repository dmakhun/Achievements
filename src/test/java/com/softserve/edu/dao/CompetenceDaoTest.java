package com.softserve.edu.dao;

import com.softserve.edu.entity.Competence;
import com.softserve.edu.entity.Group;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CompetenceDaoTest {

    @Autowired
    private CompetenceDao competenceDao;

    @Test
    public void testShowGroups() {
        List<Group> groups = competenceDao.showGroups(1);
        assertEquals(1, groups.size());
    }

    @Test
    public void testFindGroupsByCompetenceUuid() {
        List<Group> groups = competenceDao.findGroupsByCompetenceUuid("i1");
        for (Group group : groups) {
            System.out.println(group.getName());
        }
        assertEquals(2, groups.size());
    }

    @Test
    public void testFindByName() {
        assertEquals("Java", competenceDao.findByName("Java").getName());
    }

    @Test
    public void testFindByUser() {
        List<Competence> competencies = competenceDao.findCompetencesByUserId(7L);
        for (Competence competency : competencies) {
            System.out.println(competency.getName());
        }
        assertEquals(1, competencies.size());
    }

    @Test
    public void testFindByUserUuid() {
        List<Competence> competences = competenceDao.findByUserUuid("i1");
        for (Competence competency : competences) {
            System.out.println(competency.getName());
        }
        assertEquals(2, competences.size());
    }

    @Test
    public void testListWithUsers() {
        System.out.println("Competence size: "
                + competenceDao.findAllCompetences().size());
        assertNotNull(competenceDao.findAllCompetences());
    }

}