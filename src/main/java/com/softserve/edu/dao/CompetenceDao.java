package com.softserve.edu.dao;

import com.softserve.edu.entity.Competence;
import com.softserve.edu.entity.Group;

import java.util.List;

public interface CompetenceDao extends GenericDao<Competence> {

    /**
     * Returns all groups in a certain competence.
     *
     * @param id of group
     * @return list of groups,
     */
    public List<Group> showGroups(int groupId);

    /**
     * Finds all groups of a specific competence
     *
     * @param competenceUuid
     * @return
     */
    public List<Group> findGroupsByCompetenceUuid(final String competenceUuid);

    /**
     * Find competence with certain name.
     *
     * @param name of comp you need
     * @return competence
     */

    public Competence findByName(String name);

    /**
     * Get list of competences, that user want attend to.
     *
     * @param userId
     * @return
     */
    public List<Competence> findCompetencesByUserId(final Long userId);

    /**
     * Finds the list of the competences of the specific user by user's UUID.
     *
     * @param userUuid
     * @return
     */
    public List<Competence> findByUserUuid(final String userUuid);

    /**
     * Get competences with loaded users to them.
     *
     * @return list of competences
     */
    public List<Competence> findAllCompetences();

}
