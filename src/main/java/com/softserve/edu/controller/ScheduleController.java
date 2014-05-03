package com.softserve.edu.controller;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.softserve.edu.manager.ScheduleManager;
import com.softserve.edu.manager.ScheduleManagerImplementation;
import com.softserve.edu.manager.ScheduleRowsManagerImplementation;
import com.softserve.edu.manager.UserManager;

/**
 * 
 * Controller serves a request to schedule.
 * 
 * @author edgar.
 * 
 */
@Controller
public class ScheduleController {

	private static final String GENERALERROR = "redirect:/myerror/10";
	private static final Logger LOGGER = Logger
			.getLogger(ScheduleController.class);

	@Autowired
	UserManager userManager;

	@Autowired
	ScheduleManager scheduleManager;

	/**
	 * 
	 * @param model
	 * @return scheduleTable.jsp
	 */
	@RequestMapping(value = "/schedule/{group:[a-zA-Z0-9\\.\\-_]+}/{dateAdd}")
	public String schedule(@PathVariable("group") String group,
			@PathVariable("dateAdd") Integer dateAdd, Model model) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, 7 * dateAdd);
			Map<Long, String> mapWeek = new ScheduleRowsManagerImplementation(
					calendar).getWeekHead();
			Map<Long, String> map = new ScheduleManagerImplementation().table(
					calendar, group.replace('_', ' '));

			model.addAttribute("group", group);
			model.addAttribute("mapWeek", mapWeek);
			model.addAttribute("map", map);
			model.addAttribute("next", dateAdd + 1);
			model.addAttribute("prev", dateAdd - 1);
			return "schedule";
		} catch (Exception e) {
			LOGGER.error(e);
			return GENERALERROR;
		}
	}

	@RequestMapping(value = "/scheduleTable", method = RequestMethod.GET)
	public String scheduleTable(Model model) {
		try {
			Authentication auth = SecurityContextHolder.getContext()
					.getAuthentication();
			model.addAttribute("set",
					userManager.findActiveNameGroups(auth.getName()));
			return "scheduleTable";
		} catch (Exception e) {
			LOGGER.error(e);
			return GENERALERROR;
		}

	}

	@RequestMapping(value = "/addSchedule", method = RequestMethod.GET)
	public String addSchedule(Model model) {
		return "addSchedule";
	}

	@RequestMapping(value = "/addSchedule", method = RequestMethod.POST)
	String uploadFileHandler(@RequestParam("file") MultipartFile file,
			Model model) throws IllegalStateException, IOException {

		try {
			File serverFile = new ScheduleManagerImplementation()
					.saveFileOnServer(file);
			scheduleManager.fillDBfromCSV(serverFile);
			model.addAttribute("status", "file upload successfully");
			return "addSchedule";
		} catch (Exception e) {
			LOGGER.error(e);
			model.addAttribute("status", e.getMessage());
			return "addSchedule";
		}
	}

}