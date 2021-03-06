package com.edu.academy.dao;

import com.edu.academy.entity.Competence;
import com.edu.academy.entity.Group;
import com.edu.academy.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static java.util.Arrays.asList;

@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
@Transactional
@Rollback
@ExtendWith(SpringExtension.class)
public class GroupRepositoryTest {

    @Autowired
    GroupRepository groupRepository;
    @Autowired
    CompetenceRepository competenceRepository;
    @Autowired
    UserRepository userRepository;

    private Long competenceId;

    @BeforeEach
    public void setUp() {
        Competence competence = new Competence("competence", null);
        competenceRepository.save(competence);
        competenceId = competenceRepository.findByName("competence").getId();
        Group aGroup = createPendingGroup(competence);
        Group aGroup2 = createOpenGroup(competence);
        groupRepository.saveAll(asList(aGroup, createClosedGroup(competence), aGroup2));
    }

    public Group createPendingGroup(Competence competence) {
        LocalDate startDate = LocalDate.now().plusMonths(1);
        LocalDate endDate = startDate.plusMonths(3);
        return new Group(competence, "groupName", startDate, endDate, null);
    }

    public Group createOpenGroup(Competence competence) {
        LocalDate startDate = LocalDate.now().minusMonths(1);
        LocalDate endDate = startDate.plusMonths(4);
        return new Group(competence, "groupName", startDate, endDate, null);
    }

    public Group createClosedGroup(Competence competence) {
        LocalDate startDate = LocalDate.now().minusMonths(4);
        LocalDate endDate = startDate.plusMonths(3);
        return new Group(competence, "groupName", startDate, endDate, null);
    }

    @Test
    public void testQuery() {
        User user = new UserRepositoryTest().createUser();
        Group group = createOpenGroup(null);
        user.addGroup(group);
        userRepository.save(user);
        groupRepository.findOpenedByUserId(user.getId());
    }

}
