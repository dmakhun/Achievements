package com.edu.academy.controller;

import com.edu.academy.dao.AchievementRepository;
import com.edu.academy.dao.GroupRepository;
import com.edu.academy.dao.RoleRepository;
import com.edu.academy.dao.UserRepository;
import com.edu.academy.entity.User;
import com.edu.academy.exception.UserManagerException;
import com.edu.academy.manager.CompetenceManager;
import com.edu.academy.manager.UserManager;
import com.edu.academy.util.FieldForSearchController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static com.edu.academy.util.Constants.GENERAL_ERROR;
import static com.edu.academy.util.Constants.ROLE_MANAGER;

@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserManager userManager;
    @Autowired
    private CompetenceManager competenceManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private AchievementRepository achievementRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @RequestMapping(value = "/userHome", method = RequestMethod.GET)
    public String userHome(Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userManager.findByUsername(authentication.getName());

            model.addAttribute("groups", groupRepository.findByUsers_Id(user.getId()));
            model.addAttribute("achievements", achievementRepository.findByUserId(user.getId()));
            model.addAttribute("availableCompetences", competenceManager.findAvailable(user));
            model.addAttribute("userCompetences", groupRepository.findOpenedByUserId(user.getId()));

            return "userHome";
        } catch (Exception e) {
            logger.error("Home user page error ", e);
            return GENERAL_ERROR;
        }
    }

    @RequestMapping(value = "/userHome", method = RequestMethod.POST)
    public String attend(
            @RequestParam(value = "competence") long competenceId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            userManager
                    .appendCompetence(userManager.findByUsername(authentication.getName()).getId(),
                            competenceId);
            return "redirect:/user/userHome";
        } catch (UserManagerException e) {
            logger.error(e.getMessage());
            return GENERAL_ERROR;
        }

    }

    @RequestMapping(value = "/admin/removeManager", method = RequestMethod.POST)
    public String removeManager(
            @RequestParam(value = "userId", required = false, defaultValue = "") Long userId) {
        try {
            userManager.deleteById(userId);
            return "redirect:/admin/removeManager?status=success";
        } catch (UserManagerException e) {
            logger.error(e.getMessage());
            return GENERAL_ERROR;
        }
    }

    @RequestMapping(value = "/admin/addManager", method = RequestMethod.GET)
    public String addManager(
            @RequestParam(value = "status", defaultValue = "", required = false) String status,
            Model model) {
        model.addAttribute("status", status);
        return "addManager";
    }

    @RequestMapping(value = "/admin/addManager", method = RequestMethod.POST)
    public
    @ResponseBody
    String addManager(@Valid User user, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return "redirect:/admin/allManagers?status=error";
            }
            user.setRole(roleRepository.findByName(ROLE_MANAGER));
            userManager.createUser(user);
            return "admin/allManagers";
        } catch (Exception e) {
            logger.error(e.getMessage());
            return GENERAL_ERROR;
        }
    }

    @RequestMapping(value = "/admin/allManagers", method = RequestMethod.GET)
    public String allManagers(
            @RequestParam(value = "status", defaultValue = "", required = false) String status,
            Model model) {
        try {
            List<User> managers = userRepository.findByRoleName(ROLE_MANAGER);
            Map<String, String> searchBy = new FieldForSearchController<>(
                    User.class).findAnnotation();
            model.addAttribute("total", managers.size());
            model.addAttribute("user", new User());
            model.addAttribute("searchBy", searchBy);
            model.addAttribute("userlist", managers);
            model.addAttribute("status", status);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return GENERAL_ERROR;
        }
        return "allManagers";
    }

    @RequestMapping(value = "/admin/managers/search/{pattern}", method = RequestMethod.GET)
    public String allManagersDynamicSearch(
            @RequestParam(value = "parameter") String parameter,
            @RequestParam(value = "volume") int max,
            @RequestParam(value = "pagination") int page,
            @RequestParam(value = "isFirstChar") boolean isFirstChar,
            @PathVariable(value = "pattern") String pattern,
            Model model) {
        try {
            Iterable<User> foundManagers = userManager
                    .dynamicSearch(parameter, pattern, ROLE_MANAGER, page, max,
                            isFirstChar);
            Iterable<User> allManagers = userManager
                    .dynamicSearch(parameter, pattern, ROLE_MANAGER, 1,
                            userRepository.findByRoleName(ROLE_MANAGER).size(), isFirstChar);

            model.addAttribute("userlist", foundManagers);
            model.addAttribute("currentSize", Stream.of(allManagers).count());

            return "dynamicSearch";
        } catch (Exception e) {
            logger.error(e.getMessage());
            return GENERAL_ERROR;
        }
    }

    @RequestMapping(value = "/admin/removeManager/{id}", method = RequestMethod.GET)
    public String removeManagerById(@PathVariable(value = "id") Long userId) {
        try {
            userManager.deleteById(userId);
            return "redirect:/admin/allManagers?status=success";
        } catch (UserManagerException e) {
            return "errorPage";
        }
    }

    @RequestMapping(value = "/image", method = RequestMethod.GET)
    String getImage(Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext()
                    .getAuthentication();
            model.addAttribute("username", authentication.getName());

            return "image";
        } catch (Exception e) {
            logger.error(e.getMessage());
            return GENERAL_ERROR;
        }
    }

    @RequestMapping(value = "/image", method = RequestMethod.POST)
    String uploadFileHandler(@RequestParam("file") MultipartFile file) {
        try {
            if (!file.isEmpty() || file.getContentType().startsWith("image/")) {
                Authentication authentication = SecurityContextHolder.getContext()
                        .getAuthentication();
                byte[] imageInByte = file.getBytes();
                User user = userManager.findByUsername(authentication.getName());
                user.setPicture(imageInByte);
                userRepository.save(user);
            }
            return "redirect:/image";
        } catch (Exception e) {
            logger.error(e.getMessage());
            return GENERAL_ERROR;
        }

    }

    @RequestMapping(value = "/showImage/{username}")
    public ResponseEntity<byte[]> showImage(
            @PathVariable(value = "username") String username) {
        try {
            User user = userManager.findByUsername(username);
            byte[] image;
            if (user != null && user.getPicture() != null) {
                image = user.getPicture();
            } else {
                URL url = Thread.currentThread().getContextClassLoader()
                        .getResource("defaultPicture.png");
                File file = new File(Objects.requireNonNull(url).getPath().replaceAll("%20", " "));
                image = Files.readAllBytes(file.toPath());
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);

            return new ResponseEntity<>(image, headers, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @RequestMapping(value = "/userprofile", method = RequestMethod.GET)
    public String userProfile(Model model, Principal principal) {
        try {
            User user = userManager.findByUsername(principal.getName());
            model.addAttribute("name", user.getName());
            model.addAttribute("email", user.getEmail());
            model.addAttribute("surname", user.getSurname());
            model.addAttribute("username", user.getUsername());

            return "mainProfile";
        } catch (Exception e) {
            logger.error(e.getMessage());
            return GENERAL_ERROR;
        }
    }

    @RequestMapping(value = "/editprofile", method = RequestMethod.GET)
    public String editProfile(Model model, Principal principal) {
        try {
            User user = userManager.findByUsername(principal.getName());
            model.addAttribute("id", user.getId());
            model.addAttribute("name", user.getName());
            model.addAttribute("email", user.getEmail());
            model.addAttribute("surname", user.getSurname());
            model.addAttribute("username", user.getUsername());

            return "userProfile";
        } catch (Exception e) {
            logger.error(e.getMessage());
            return GENERAL_ERROR;
        }
    }

    @RequestMapping(value = "/editprofile", method = RequestMethod.POST)
    public String editProfileUpdate(@Valid User user, BindingResult result,
                                    Model model, Principal principal) {
        User currentUser = userManager.findByUsername(principal.getName());
        model.addAttribute("name", user.getName());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("surname", user.getSurname());
        model.addAttribute("username", currentUser.getUsername());

        if (result.hasErrors()) {
            model.addAttribute("error", "fields incorrectly filled out.");
            return "userProfile";
        }
        try {
            userManager.updateUser(currentUser.getId(), user.getName(),
                    user.getSurname(), null, null, user.getEmail(), null);
            return "mainProfile";
        } catch (UserManagerException e) {
            model.addAttribute("error", "email already exists.");
            logger.error(e.getMessage());
            return "userProfile";
        }
    }

    @RequestMapping(value = "/passwordchanging", method = RequestMethod.GET)
    public String passwordChange() {
        return "passwordchanging";
    }

    @RequestMapping(value = "/passwordchanging", method = RequestMethod.POST)
    public String passwordChanger(
            @RequestParam(value = "oldPassword", required = false, defaultValue = "") String oldPassword,
            @RequestParam(value = "newPassword", required = false, defaultValue = "") String newPassword,
            Principal principal, Model model) {

        try {
            User user = userManager.findByUsername(principal.getName());

            if (encoder.matches(oldPassword, user.getPassword())
                    && newPassword.length() >= 4) {
                userManager.updateUser(user.getId(), null, null, null, newPassword,
                        null, null);
                model.addAttribute("name", user.getName());
                model.addAttribute("email", user.getEmail());
                model.addAttribute("surname", user.getSurname());
                model.addAttribute("username", user.getUsername());
                return "mainProfile";
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            model.addAttribute("error", true);
        }
        return "passwordchanging";

    }
}
