package com.softserve.edu.dao;

import com.softserve.edu.entity.Role;
import com.softserve.edu.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RoleDaoTest {

    @Autowired
    private RoleDao roleDao;

    @Test
    public void testFindUsers() {
        List<User> users = roleDao.findUsers(1);
        System.out.println("Users with role id 1");
        for (Iterator<User> i = users.iterator(); i.hasNext(); ) {
            System.out.println(i.next().getName());
        }
        assertEquals(2, users.size());
    }

    @Test
    public void testFindRole() {
        long i = roleDao.findRole("admin");
        System.out.println("id role with name 'admin': " + i);
        assertEquals(2, i);
    }

    @Test
    public void testFindRoleByRolename() {
        Role role = roleDao.findRoleByName("user");
        System.out.println("Uuid from user " + role.getUuid());
        assertEquals("i1", role.getUuid());
        assertNotNull(role);
    }

    @Test
    public void testFindUsersByRoleUuid() {
        List<User> users = roleDao.findUsersByRoleUuid("i1");
        System.out.println("users with roleUuid 'i1':");
        for (User user : users) {
            System.out.println(user.getName());
        }
        assertEquals(2, users.size());
    }

}