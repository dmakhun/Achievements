package com.softserve.edu.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.softserve.edu.entity.Competence;
import com.softserve.edu.entity.Group;
import com.softserve.edu.entity.User;
import com.softserve.edu.exception.GroupManagerException;
import com.softserve.edu.exception.UserManagerException;
import com.softserve.edu.manager.CompetenceManager;
import com.softserve.edu.manager.GroupManager;
import com.softserve.edu.manager.UserManager;

@Controller
public class ManagerController {

	private static final String GENERALERROR = "redirect:/myerror/10";
	private static final Logger LOGGER = Logger
			.getLogger(ManagerController.class);

	@Autowired
	GroupManager groupManager;
	@Autowired
	CompetenceManager competenceManager;
	@Autowired
	UserManager userManager;
	@Autowired
	private MessageSource messageSource;

	@RequestMapping(value = "/manager/groups", method = RequestMethod.GET)
	public String groups(
			@RequestParam(value = "status", required = false, defaultValue = "") String status,
			Model model) {

		try {
			List<Competence> competenceList = competenceManager
					.findAllCompetences();
			Map<String, List<Group>> groups = new HashMap<>();
			for (Competence competence : competenceList) {
				groups.put(competence.getName(),
						groupManager.findByCompetence(competence.getId(), true));
			}
			model.addAttribute("status", status);
			model.addAttribute("groups", groups);
			model.addAttribute("competences", competenceList);
			return "groups";
		} catch (Exception e) {
			LOGGER.error(e);
			return GENERALERROR;
		}
	}

	@RequestMapping(value = "/manager/groups/manage", method = RequestMethod.POST)
	public ResponseEntity<String> groupManagement(
			@RequestParam(value = "id") Long id,
			@RequestParam(value = "type") String type,
			@RequestParam(value = "group_name") String groupName,
			@RequestParam(value = "competence") Long competenceId,
			@RequestParam(value = "dateStart") String dateStart,
			@RequestParam(value = "dateEnd") String dateEnd, Locale locale)
			throws ParseException {
		try {
			switch (type.toLowerCase()) {
			case "create":
				return createGroup(groupName, competenceId, dateStart, dateEnd,
						locale);

			case "modify":
				return modifyGroup(id, groupName, competenceId, dateStart,
						dateEnd, locale);

			case "delete":
				return deleteGroup(id);
			}
		} catch (Exception e) {
			LOGGER.error(e);
		}
		return new ResponseEntity<String>(HttpStatus.METHOD_NOT_ALLOWED);
	}

	private ResponseEntity<String> createGroup(String groupName,
			Long competence, String start, String end, Locale locale)
			throws ParseException {

		Long id = null;
		try {
			ResponseEntity<String> pass = groupChecks(groupName, start, end,
					locale);
			if (pass.getStatusCode() != HttpStatus.OK) {
				return pass;
			}

			Date starting = new SimpleDateFormat("yyyy-MM-dd").parse(start);
			Date ending = new SimpleDateFormat("yyyy-MM-dd").parse(end);

			id = groupManager.create(groupName, starting, ending, competence);
		} catch (GroupManagerException e) {
			LOGGER.error(e);
		}
		return new ResponseEntity<>(id.toString(), HttpStatus.OK);
	}

	private ResponseEntity<String> modifyGroup(Long groupId, String name,
			Long competence, String start, String end, Locale locale)
			throws ParseException {
		try {
			ResponseEntity<String> pass = groupChecks(name, start, end, locale);
			if (pass.getStatusCode() != HttpStatus.OK) {
				return pass;
			}
			Date starting = new SimpleDateFormat("yyyy-MM-dd").parse(start);
			Date ending = new SimpleDateFormat("yyyy-MM-dd").parse(end);
			groupManager.modify(groupId, name, starting, ending, competence);
		} catch (GroupManagerException e) {
			LOGGER.error(e);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	private ResponseEntity<String> deleteGroup(Long groupId) {
		try {
			groupManager.deleteById(groupId);
		} catch (GroupManagerException e) {
			LOGGER.error(e);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	private ResponseEntity<String> groupChecks(String name, String start,
			String end, Locale locale) {
		try {
			Date starting = new SimpleDateFormat("yyyy-MM-dd").parse(start);
			Date ending = new SimpleDateFormat("yyyy-MM-dd").parse(end);

			if (name.isEmpty()) {
				return new ResponseEntity<String>(messageSource.getMessage(
						"groups.error.emptyname", null, locale),
						HttpStatus.NOT_ACCEPTABLE);
			}

			if (starting.after(ending)) {
				return new ResponseEntity<String>(messageSource.getMessage(
						"groups.error.inversed", null, locale),
						HttpStatus.NOT_ACCEPTABLE);
			}

			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e);
		}
		return new ResponseEntity<String>(messageSource.getMessage(
				"groups.error.datepattern", null, locale),
				HttpStatus.NOT_ACCEPTABLE);
	}

	@RequestMapping(value = "/manager/group/{id}", method = RequestMethod.GET)
	public String concreteGroup(@PathVariable(value = "id") Long groupId,
			Model model) {
		try {
			List<User> userList = groupManager.users(groupId);
			model.addAttribute("users", userList);
			return "userlist";
		} catch (Exception e) {
			LOGGER.error(e);
			return GENERALERROR;
		}
	}

	@RequestMapping(value = "/manager/attendees", method = RequestMethod.GET)
	public String attendees(Model model) {
		try {
			List<Competence> competenceList = competenceManager
					.findAllCompetences();
			Map<String, List<Group>> groups = new HashMap<>();
			for (Competence competence : competenceList) {
				groups.put(competence.getName(),
						groupManager.findByCompetence(competence.getId(), true));
			}

			model.addAttribute("competences", competenceList);
			model.addAttribute("competence_groups", groups);

			return "attendees";
		} catch (Exception e) {
			LOGGER.error(e);
			return GENERALERROR;
		}
	}

	@RequestMapping(value = "/manager/attendees", method = RequestMethod.POST)
	public String attendUser(@RequestParam(value = "user_id") Long userId,
			@RequestParam(value = "group_id") Long groupId,
			@RequestParam(value = "competence_id") Long competenceId) {
		try {
			groupManager.addUser(userId, groupId);
			userManager.removeUserToCompetence(userId, competenceId);
		} catch (GroupManagerException e) {
			LOGGER.error(e);
		} catch (UserManagerException e) {
			LOGGER.error(e);
		}

		return "redirect:/manager/attendees";
	}

	@RequestMapping(value = "/admin/removeManager", method = RequestMethod.GET)
	public String removeManager(
			@RequestParam(value = "status", defaultValue = "", required = false) String status,
			Model model) {
		try {
						
			List<User> managers = userManager.findAllManagers();
			
			model.addAttribute("userlist", managers);
			model.addAttribute("status", status);

			return "removeManager";
		} catch (Exception e) {
			LOGGER.error(e);
			return GENERALERROR;
		}
	}

}