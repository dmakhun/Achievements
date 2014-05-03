package com.softserve.edu.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.softserve.edu.entity.AchievementType;
import com.softserve.edu.exception.AchievementTypeManagerException;
import com.softserve.edu.manager.AchievementTypeManager;

@Path("/achievement_type")
public class AchievementTypeRest {
	private static final Logger LOGGER = Logger
			.getLogger(AchievementTypeRest.class);

	@Autowired
	private AchievementTypeManager achievementTypeManager;

	/**
	 * This method returns all existing achievement types
	 * http://localhost:8080/Achievements
	 * /rest/achievement_type/findAllAchievementTypes
	 * 
	 * @return xml representation of all achievement types
	 */
	@Path("/findAllAchievementTypes")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response findAllAChievementTypes() {

		List<AchievementType> achievementTypes;

		try {
			achievementTypes = achievementTypeManager.achievementTypeList();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return Response.status(500).entity(e.getMessage()).build();
		}
		return Response.ok(new JaxbList<AchievementType>(achievementTypes))
				.build();

	}

	/**
	 * Creates a new achievement type according to the data of consumed XML
	 * http://localhost:8080/Achievements/rest/achievement_type/
	 * createAchievementType
	 * 
	 * @param achievementType
	 * @return
	 */
	@POST
	@Path("/createAchievementType")
	@Consumes(MediaType.APPLICATION_XML)
	public Response createAchievementType(AchievementType achievementType) {

		try {
			achievementTypeManager.create(achievementType.getName(),
					achievementType.getPoints(), achievementType
							.getCompetence().getUuid());

		} catch (AchievementTypeManagerException e) {
			LOGGER.error(e.getMessage());
			return Response.status(500).entity(e.getMessage()).build();
		}

		return Response.status(201).build();
	}

	/**
	 * deletes achievement type by its uuid
	 * http://localhost:8080/Achievements/rest
	 * /achievement_type/deleteAchivementTypeByUuid/UUID
	 * 
	 * @param uuid
	 * @return
	 */
	@DELETE
	@Path("deleteAchivementTypeByUuid/{uuid}")
	public Response deleteAchivementTypeByUuid(
			@PathParam("uuid") final String uuid) {

		try {
			achievementTypeManager.deleteByUuid(uuid);
		} catch (AchievementTypeManagerException e) {
			LOGGER.error(e.getMessage());
			return Response.status(500).entity(e.getMessage()).build();
		}

		return Response.status(200).entity("Achivement Type was removed")
				.build();
	}

}