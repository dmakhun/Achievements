package com.softserve.edu.rest;

import com.softserve.edu.entity.AchievementType;
import com.softserve.edu.exception.AchievementTypeManagerException;
import com.softserve.edu.manager.AchievementTypeManager;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Path("/achievement_type")
public class AchievementTypeRest {

    private static final Logger logger = LoggerFactory
            .getLogger(AchievementTypeRest.class);

    @Autowired
    private AchievementTypeManager achievementTypeManager;

    /**
     * This method returns all existing achievement types http://localhost:8080/Achievements
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
            achievementTypes = achievementTypeManager.achievementTypesList();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Response.status(500).entity(e.getMessage()).build();
        }
        return Response.ok(new JaxbList<>(achievementTypes)).build();
    }

    /**
     * Creates a new achievement type according to the data of consumed XML
     * http://localhost:8080/Achievements/rest/achievement_type/ createAchievementType
     */
    @POST
    @Path("/createAchievementType")
    @Consumes(MediaType.APPLICATION_XML)
    public Response createAchievementType(AchievementType achievementType) {

        try {
            achievementTypeManager.createAchievementTypeByUuid(achievementType.getName(),
                    achievementType.getPoints(), achievementType
                            .getCompetence().getUuid());

        } catch (AchievementTypeManagerException e) {
            logger.error(e.getMessage());
            return Response.status(500).entity(e.getMessage()).build();
        }

        return Response.status(201).build();
    }

    /**
     * deletes achievement type by its uuid http://localhost:8080/Achievements/rest
     * /achievement_type/deleteAchivementTypeByUuid/UUID
     */
    @DELETE
    @Path("deleteAchivementTypeByUuid/{uuid}")
    public Response deleteAchivementTypeByUuid(
            @PathParam("uuid") final String uuid) {

        try {
            achievementTypeManager.deleteAchievementType(uuid);
        } catch (AchievementTypeManagerException e) {
            logger.error(e.getMessage());
            return Response.status(500).entity(e.getMessage()).build();
        }

        return Response.status(200).entity("Achivement Type was removed")
                .build();
    }

}
