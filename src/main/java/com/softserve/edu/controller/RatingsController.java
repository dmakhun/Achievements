package com.softserve.edu.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.softserve.edu.entity.User;
import com.softserve.edu.exception.UserManagerException;
import com.softserve.edu.manager.UserManager;

@Controller
public class RatingsController {

	@Autowired
	UserManager userManager;

	@RequestMapping(value = "/manager/ratings", method = RequestMethod.GET)
	public String ratings(Model model) throws UserManagerException {
		List<User> users = userManager.findAllUsers();
		Map<User, Long> mapS = new HashMap<User, Long>();
		
		for (User user : users) {
			mapS.put(user, userManager.sumOfPoints(user));
		}
		
		mapS = sortByComparator(mapS);
		
		model.addAttribute("mapS", mapS);
		return "ratings";
	}
	
	private static Map<User, Long> sortByComparator(Map<User, Long> unsortMap) {
		 
		List<Object> list = new LinkedList<Object>(unsortMap.entrySet());
 
		// sort list based on comparator
		Collections.sort(list, Collections.reverseOrder(new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getValue())
                                       .compareTo(((Map.Entry) (o2)).getValue());
			}
		}));
 
		// put sorted list into map again
        //LinkedHashMap make sure order in which keys were inserted
		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
}