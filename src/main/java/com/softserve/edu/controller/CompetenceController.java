package com.softserve.edu.controller;

import static com.softserve.edu.util.Constants.GENERAL_ERROR;

import com.softserve.edu.dao.CompetenceRepository;
import com.softserve.edu.dao.GroupRepository;
import com.softserve.edu.entity.Competence;
import com.softserve.edu.exception.UserManagerException;
import com.softserve.edu.manager.CompetenceManager;
import com.softserve.edu.manager.UserManager;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CompetenceController {

    private static final Logger logger = LoggerFactory.getLogger(CompetenceController.class);

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private CompetenceRepository competenceRepository;
    @Autowired
    private CompetenceManager competenceManager;
    @Autowired
    private UserManager userManager;

    @RequestMapping(value = "/competence", method = RequestMethod.GET)
    public String allCompetences(Model model) {
        try {
            model.addAttribute("competences", competenceManager.findAllCompetences());
            return "showCompetence";
        } catch (Exception e) {
            logger.error(e.getMessage());
            return GENERAL_ERROR;
        }
    }

    @RequestMapping(value = "/addCompetence", method = RequestMethod.GET)
    public String addCompetence(
            @RequestParam(value = "status", defaultValue = "", required = false) String status,
            Model model) {
        try {
            model.addAttribute("allCompetences", competenceManager.findAllCompetences());
            model.addAttribute("status", status);
            return "addCompetence";
        } catch (Exception e) {
            logger.error(e.getMessage());
            return GENERAL_ERROR;
        }
    }

    @RequestMapping(value = "/addCompetence", params = {
            "addCompetence"}, method = RequestMethod.POST)
    public String addCompetence(
            @RequestParam(value = "competenceName", required = false, defaultValue = "") String name) {
        competenceRepository.save(new Competence(name, LocalDate.now()));
        return "redirect:/admin/competenceAll?statusAdd=success";
    }

    @RequestMapping(value = "/addManagerList", method = RequestMethod.POST)
    public String addManager(
            @RequestParam(value = "userId", required = false, defaultValue = "") Long userId,
            @RequestParam(value = "roleId", required = false, defaultValue = "") Long roleId) {
        try {
            userManager.updateUser(userId, null, null, null, null, null, roleId);
            return "redirect:/competencies/addManager?status=success";
        } catch (UserManagerException e) {
            logger.error(e.getMessage());
            return GENERAL_ERROR;
        }
    }

    @RequestMapping(value = "/manager/competence", method = RequestMethod.GET)
    public String allCompetencesGroups(Model model) {
        try {
            model.addAttribute("allCompetences", competenceManager.findAllCompetences());
            return "groupsAndCompetence";
        } catch (Exception e) {
            logger.error(e.getMessage());
            return GENERAL_ERROR;
        }
    }

    @RequestMapping(value = "/manager/competence", method = RequestMethod.POST)
    public String allCompetencesGroups(
            @RequestParam(value = "competence") Long competenceId, Model model) {
        try {
            model.addAttribute("allCompetences", competenceManager.findAllCompetences());
            model.addAttribute("openedGroups",
                    groupRepository.findOpenedByCompetenceId(competenceId));
            return "groupsAndCompetence";
        } catch (Exception e) {
            logger.error(e.getMessage());
            return GENERAL_ERROR;
        }
    }

    @RequestMapping(value = "/admin/competenceAll", method = RequestMethod.GET)
    public String getCompetencies(
            Model model,
            @RequestParam(value = "statusRemove", required = false, defaultValue = "") String statusRemove,
            @RequestParam(value = "statusAdd", required = false, defaultValue = "") String statusAdd) {
        try {
            model.addAttribute("allCompetences", competenceManager.findAllCompetences());
            model.addAttribute("statusAdd", statusAdd);
            model.addAttribute("statusRemove", statusRemove);
            return "forDeleteOrAddCompetence";
        } catch (Exception e) {
            logger.error(e.getMessage());
            return GENERAL_ERROR;
        }

    }

    @GetMapping(value = "/admin/removeCompetence/{id}")
    public String removeCompetenceById(@PathVariable(value = "id") Long competenceId) {
        competenceRepository.deleteById(competenceId);
        return "redirect:/admin/competenceAll?statusRemove=success";
    }
}
