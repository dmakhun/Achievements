package com.edu.academy.controller;

import com.edu.academy.dao.AchievementTypeRepository;
import com.edu.academy.manager.AchievementManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import static com.edu.academy.util.Constants.GENERAL_ERROR;

@Controller
public class AchievementController {

    private static final Logger logger = LoggerFactory.getLogger(AchievementController.class);

    @Autowired
    private AchievementManager achievementManager;
    @Autowired
    private AchievementTypeRepository achievementTypeRepository;

    @RequestMapping(value = "/manager/user/award/{id}", method = RequestMethod.GET)
    public String awardConcreteUser(
            @RequestParam(value = "status", required = false, defaultValue = "") String status,
            Model model) {
        try {
            model.addAttribute("achievements", achievementTypeRepository.findAll());
            model.addAttribute("status", status);
            return "awardUser";
        } catch (Exception e) {
            logger.error("Can't award a user: ", e);
            return GENERAL_ERROR;
        }
    }

    @RequestMapping(value = "/manager/user/award/{id}", method = RequestMethod.POST)
    public String awardConcreteUser(
            @RequestParam(value = "achievement_type_id") Long achievementTypeId,
            @RequestParam(value = "comment") String comment,
            @PathVariable(value = "id") Long userId, Model model) {
        try {
            achievementManager.awardUser(userId, achievementTypeId, comment);
            model.addAttribute("achievements", achievementTypeRepository.findAll());
        } catch (Exception e) {
            logger.error("Can't award a user: ", e);
            return GENERAL_ERROR;
        }
        return "redirect:/manager/user/award/" + userId + "?status=success";
    }
}
