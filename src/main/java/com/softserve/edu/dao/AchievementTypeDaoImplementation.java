package com.softserve.edu.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.softserve.edu.entity.AchievementType;

@Repository("achievementTypeDao")
public class AchievementTypeDaoImplementation extends
		GenericDaoImplementation<AchievementType> implements AchievementTypeDao {

	@Autowired
	UserDao userDao;

	@Override
	public List<AchievementType> findByCompetenceId(Long competenceId) {
		return findEntityList(AchievementType.GET_LIST_ACHIEVEMENT_TYPE,
				competenceId);
	}

	@Override
	public List<AchievementType> findByCompetenceUuid(String competenceUuid) {
		return findEntityList(
				AchievementType.GET_ACHIEVEMENT_TYPES_BY_COMPETENCE_ID,
				competenceUuid);
	}

}
