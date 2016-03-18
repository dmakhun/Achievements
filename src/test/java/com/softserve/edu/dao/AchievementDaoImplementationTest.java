package com.softserve.edu.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
//@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class AchievementDaoImplementationTest {

    @Autowired
    AchievementDao achievementDao;

    @Test
    public void testUserAchievements() {
        assertEquals(1, achievementDao.findByUserId(7L).size());
    }

    @Test
    public void testGetAchievementsByUserUuid() {
        assertEquals(1, achievementDao.findByUserUuid("i1").size());
    }

}
