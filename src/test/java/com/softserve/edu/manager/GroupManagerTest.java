package com.softserve.edu.manager;

import com.softserve.edu.dao.CompetenceDao;
import com.softserve.edu.dao.GenericDao;
import com.softserve.edu.dao.GroupDao;
import com.softserve.edu.entity.Competence;
import com.softserve.edu.entity.Group;
import com.softserve.edu.entity.User;
import com.softserve.edu.exception.GroupManagerException;
import com.softserve.edu.manager.impl.GroupManagerImplementation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class GroupManagerTest {

    @Mock
    private GroupDao groupDao;
    @Mock
    private CompetenceDao competenceDao;
    @Mock
    private GenericDao<Group> genericDao;

    @InjectMocks
    private GroupManager groupManager =
            new GroupManagerImplementation();

    private Competence competence;
    private List<Group> listGroups;
    private Date date;
    private Group group;
    private long idMockLong = 1;
    private String uuIdMock = "8a3e8480-d2d5-4b49-9c49-0ace2b718441";

    @Before
    public void setUp() {
        competence = new Competence();
        group = new Group();
        listGroups = new ArrayList<>();
        listGroups.add(group);
    }

    @Test
    public final void testInFutureEquals() {
        when(groupDao.inFuture()).thenReturn(listGroups);
        List<Group> listActual = groupManager.inFuture();
        assertEquals(listGroups, listActual);
    }

    @Test
    public final void testInFutureNotNull() {
        List<Group> listActual = groupManager.inFuture();
        assertNotNull(listActual);
    }

    @Test
    public final void testInFutureLongEquals() {
        when(groupDao.inFuture(idMockLong)).thenReturn(listGroups);
        List<Group> listActual = groupManager.inFuture(idMockLong);
        assertEquals(listGroups, listActual);
    }

    @Test
    public final void testInFutureLongNotNull() {
        when(groupDao.inFuture(idMockLong)).thenReturn(listGroups);
        List<Group> listActual = groupManager.inFuture(idMockLong);
        assertNotNull(listActual);
    }

    @Test
    public final void testInFutureLongNull() {
        when(groupDao.inFuture(null)).thenReturn(null);
        List<Group> listActual = groupManager.inFuture(null);
        assertNull(listActual);
    }

    @Test
    public final void testFindByCompetenceEquals() {
        when(groupDao.findByCompetence(idMockLong, false)).thenReturn(listGroups);
        List<Group> listActual = groupManager.findByCompetence(idMockLong, false);
        assertEquals(listGroups, listActual);
    }

    @Test
    public final void testFindByCompetenceNull() {
        when(groupDao.findByCompetence(idMockLong, false)).thenReturn(listGroups);
        List<Group> listActual = groupManager.findByCompetence(idMockLong, false);
        assertNotNull(listActual);
    }

    @Test
    public final void testFindByCompetenceOnlyOpenedEquals() {
        when(groupDao.findByCompetence(idMockLong, true)).thenReturn(listGroups);
        List<Group> listActual = groupManager.findByCompetence(idMockLong, true);
        assertEquals(listGroups, listActual);
    }

    @Test
    public final void testFindByCompetenceOnlyOpenedNotNull() {
        when(groupDao.findByCompetence(idMockLong, true)).thenReturn(listGroups);
        List<Group> listActual = groupManager.findByCompetence(idMockLong, true);
        assertNotNull(listActual);
    }

    @Test
    public final void testFindByCompetenceUuidEquals() {
        when(groupDao.findByCompetenceUuid(uuIdMock, false)).thenReturn(listGroups);
        List<Group> listActual = groupManager.findByCompetenceUuid(uuIdMock, false);
        assertEquals(listGroups, listActual);
    }

    @Test
    public final void testFindByCompetenceUuidNotNull() {
        when(groupDao.findByCompetenceUuid(uuIdMock, false)).thenReturn(listGroups);
        List<Group> listActual = groupManager.findByCompetenceUuid(uuIdMock, false);
        assertNotNull(listActual);
    }

    @Test
    public final void testFindByCompetenceUuidNull() {
        when(groupDao.findByCompetenceUuid(null, false)).thenReturn(null);
        List<Group> listActual = groupManager.findByCompetenceUuid(null, false);
        assertNull(listActual);
    }

    @Test
    public final void testFindByCompetenceUuidOnlyOpenedEquals() {
        when(groupDao.findByCompetenceUuid(uuIdMock, true)).thenReturn(listGroups);
        List<Group> listActual = groupManager.findByCompetenceUuid(uuIdMock, true);
        assertEquals(listGroups, listActual);
    }

    @Test
    public final void testFindByCompetenceUuidOnlyOpenedNotNull() {
        when(groupDao.findByCompetenceUuid(uuIdMock, true)).thenReturn(listGroups);
        List<Group> listActual = groupManager.findByCompetenceUuid(uuIdMock, true);
        assertNotNull(listActual);
    }

    @Test
    public final void testFindByCompetenceUuidOnlyOpenedNull() {
        when(groupDao.findByCompetenceUuid(null, false)).thenReturn(null);
        List<Group> listActual = groupManager.findByCompetenceUuid(null, false);
        assertNull(listActual);
    }

    @Test
    public final void testModify() throws GroupManagerException {
        when(competenceDao.findById(Competence.class, idMockLong)).thenReturn(competence);
        when(groupDao.findById(Group.class, idMockLong)).thenReturn(group);
        groupManager.modify(idMockLong, uuIdMock, date, date, idMockLong);
    }

    @Test
    public final void testCreateStringDateDateString() throws GroupManagerException {
        when(competenceDao.findByUuid(Competence.class, uuIdMock)).thenReturn(competence);
        //when(groupDao.save(group)).thenReturn(true);
        groupManager.create(uuIdMock, date, date, idMockLong);
    }

    @Test
    public final void testAddUserLongLong() throws GroupManagerException {
        groupManager.addUser(idMockLong, idMockLong);
    }

    @Test
    public final void testAddUserStringString() throws GroupManagerException {
        groupManager.addUser(uuIdMock, uuIdMock);
    }

    @Test
    public final void testUsersEquals() {
        List<User> expected = new ArrayList<>();
        List<User> actual = new ArrayList<>();
        actual = groupManager.users(idMockLong);
        assertEquals(expected, actual);
    }

    @Test
    public final void testUsersNotNull() {
        List<User> actual = new ArrayList<>();
        actual = groupManager.users(idMockLong);
        assertNotNull(actual);
    }

    @Test
    public final void testFindUsersByGroupUuidEquals() {
        List<User> expected = new ArrayList<>();
        List<User> actual = new ArrayList<>();
        actual = groupManager.findUsersByGroupUuid(uuIdMock);
        assertEquals(expected, actual);
    }

    @Test
    public final void testFindUsersByGroupUuidNotNull() {
        List<User> actual = new ArrayList<>();
        actual = groupManager.findUsersByGroupUuid(uuIdMock);
        assertNotNull(actual);
    }

    @Test
    public final void testDeleteById() throws GroupManagerException {
        groupManager.deleteById(idMockLong);
    }

}