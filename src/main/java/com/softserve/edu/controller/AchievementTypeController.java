package com.softserve.edu.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softserve.edu.dao.AchievementTypeDao;
import com.softserve.edu.dao.CompetenceDao;
import com.softserve.edu.entity.AchievementType;
import com.softserve.edu.entity.Competence;
import com.softserve.edu.exception.AchievementTypeManagerException;
import com.softserve.edu.manager.AchievementTypeManager;
import com.softserve.edu.manager.CompetenceManager;

@Controller
public class AchievementTypeController {

	private static final String GENERALERROR = "redirect:/myerror/10";
	private static final Logger LOGGER = Logger
			.getLogger(AchievementTypeController.class);

	@Autowired
	CompetenceManager competenceManager;
	@Autowired
	CompetenceDao competenceDao;
	@Autowired
	AchievementTypeDao achievementTypeDao;
	@Autowired
	AchievementTypeManager achievementTypeManager;

	@RequestMapping(value = "/admin/achievementtype/allAchievements", method = RequestMethod.GET)
	public String addAchievementTypeAll(
			@RequestParam(value = "status", required = false, defaultValue = "") String status,
			@RequestParam(value = "name", required = false) String name,
			Model model) {
		try {
			List<AchievementType> achievementTypeList = achievementTypeManager
					.achievementTypeList();
			
			model.addAttribute("achievementTypeList", achievementTypeList);
			model.addAttribute("status", status);
			
			return "allAchievements";
		} catch (Exception e) {
			LOGGER.error(e);
			return GENERALERROR;
		}
	}

	@RequestMapping(value = "/admin/achievementtype/add", method = RequestMethod.GET)
	public String addAchievementTypeAllCompetences(Model model) {
		try {
			List<Competence> competencelist = competenceManager
					.findAllCompetences();
			
			model.addAttribute("competencelist", competencelist);
			
			return "AllCompetencies";
		} catch (Exception e) {
			LOGGER.error(e);
			return GENERALERROR;
		}
	}

	@RequestMapping(value = "/admin/achievementtype/add/{id}", method = RequestMethod.POST)
	public @ResponseBody
	String addAchievementTypePost(
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "points", required = false) String points,
			@PathVariable(value = "id") int competenceId, Model model) {
		try {
			int achPoints = Integer.parseInt(points);
			achievementTypeManager.create(name, achPoints, competenceId);
			
			return "success";
		} catch (AchievementTypeManagerException e) {
			LOGGER.error(e);
			return GENERALERROR;
		}

	}

	@RequestMapping(value = "/admin/achievementtype/list/{id}", method = RequestMethod.GET)
	public String list(@PathVariable(value = "id") Long competenceId,
			Model model) {
		try {
			List<AchievementType> achievements = achievementTypeDao
					.findByCompetenceId(competenceId);
			
			model.addAttribute("achievements", achievements);
			model.addAttribute("competenceId", competenceId);

			return "achievementTypeList";
		} catch (Exception e) {
			LOGGER.error(e);
			return GENERALERROR;
		}
	}

	@RequestMapping(value = "admin/removeAchievementType/{id}", method = RequestMethod.GET)
	public String removeAchievementTypeById(
			@PathVariable(value = "id") Long achievementTypeId) {

		try {
			achievementTypeManager.deleteById(achievementTypeId);
			
			return "redirect:/admin/achievementtype/allAchievements?status=success";
		} catch (AchievementTypeManagerException e) {
			LOGGER.error(e);
			return GENERALERROR;
		}
	}

}