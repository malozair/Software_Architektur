package de.hs.swa.boundary;

import de.hs.swa.control.CustomerService;

import de.hs.swa.control.DrinkService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("drinks")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@OpenAPIDefinition(
        info = @Info(
                title = "Drink API",
                version = "1.0.0",
                description = "API for managing and searching drinks (Mocktails and Cocktails)",
                contact = @Contact(name = "Support Team", email = "support@example.com"),
                license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0.html")
        ),
        tags = {
                @Tag(name = "Drink API", description = "API for managing and searching drinks")
        }
)
public class DrinkResource {

    private static final Logger logger = Logger.getLogger(DrinkResource.class.getName());

    @Inject
    CustomerService customerService;

    @Inject
    DrinkService drinkService;


    @GET
    public Response searchDrinks(
            @QueryParam("category") String category,
            @QueryParam("name") String name,
            @QueryParam("ingredient") String ingredient) {
        logger.info("Search request received: category=" + category + ", name=" + name + ", ingredient=" + ingredient);
        try {
            if (category != null) {
                return Response.ok(drinkService.searchByCategory(category)).build();
            } else if (name != null) {
                return Response.ok(drinkService.searchByName(name)).build();
            } else if (ingredient != null) {
                return Response.ok(drinkService.searchByIngredient(ingredient)).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("At least one search parameter (category, name, ingredient) must be provided.")
                        .build();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing search request.", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while processing your request.")
                    .build();
        }
    }








    @GET
    @Path("/mocktails")
    @Operation(summary = "Fetch all mocktails", description = "Returns a list of all available mocktail recipes.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Mocktails fetched successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MocktailDTO.class))),
            @APIResponse(responseCode = "500", description = "Server error")
    })
    @Retry(maxRetries = 3)
    @Timeout(200)
    @CircuitBreaker
    public Response getAllMocktails() {
        logger.info("Fetching all mocktails.");
        try {
            List<MocktailDTO> mocktails = customerService.readAllMocktails();
            logger.fine("Number of mocktails fetched: " + mocktails.size());
            return Response.ok(mocktails).build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while fetching mocktails.", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error while fetching mocktails.").build();
        }
    }

    @POST
    @Operation(summary = "Add a new mocktail", description = "Adds a new mocktail recipe to the catalog.")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Mocktail created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MocktailIdDTO.class))),
            @APIResponse(responseCode = "400", description = "Invalid input data")
    })
    @Retry(maxRetries = 3)
    @Timeout(200)
    @Fallback(fallbackMethod = "addMocktailFallback")
    public Response addMocktail(MocktailDTO mocktailDTO) {
        logger.info("Adding new mocktail: " + mocktailDTO.getName());
        try {
            String id = customerService.addMocktail(mocktailDTO);
            if (id != null) {
                logger.info("Mocktail added successfully with ID: " + id);
                return Response.status(Response.Status.CREATED).entity(new MocktailIdDTO(id)).build();
            } else {
                logger.warning("Failed to add mocktail: " + mocktailDTO.getName());
                return Response.status(Response.Status.BAD_REQUEST).entity("Failed to add mocktail.").build();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while adding new mocktail.", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error while adding new mocktail.").build();
        }
    }

    public Response addMocktailFallback(MocktailDTO mocktailDTO) {
        logger.warning("Fallback triggered for addMocktail.");
        return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("Service temporarily unavailable. Please try again later.").build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Fetch a mocktail by ID", description = "Returns a single mocktail recipe by its ID.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Mocktail fetched successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MocktailDTO.class))),
            @APIResponse(responseCode = "404", description = "Mocktail not found")
    })
    @Retry(maxRetries = 3)
    @Timeout(200)
    public Response getMocktailById(@PathParam("id") String id) {
        logger.info("Fetching mocktail with ID: " + id);
        try {
            MocktailDTO mocktailDTO = customerService.readMocktail(id);
            if (mocktailDTO == null) {
                logger.warning("Mocktail not found with ID: " + id);
                return Response.status(Response.Status.NOT_FOUND).entity("Mocktail not found").build();
            }
            logger.info("Mocktail fetched successfully with ID: " + id);
            return Response.ok(mocktailDTO).build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while fetching mocktail by ID: " + id, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error while fetching mocktail.").build();
        }
    }

    @PATCH
    @Path("/{id}")
    @Operation(summary = "Edit a mocktail by ID", description = "Edits an existing mocktail recipe by its ID.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Mocktail edited successfully"),
            @APIResponse(responseCode = "400", description = "Failed to edit mocktail"),
            @APIResponse(responseCode = "500", description = "Server error")
    })
    @Retry(maxRetries = 3)
    @Timeout(200)
    public Response editMocktail(@PathParam("id") String id, MocktailDTO mocktailDTO) {
        logger.info("Editing mocktail with ID: " + id);
        try {
            mocktailDTO.setId(id);
            String result = customerService.editMocktail(mocktailDTO);
            if ("Mocktail edited successfully".equals(result)) {
                logger.info("Mocktail edited successfully with ID: " + id);
                return Response.ok(result).build();
            } else {
                logger.warning("Failed to edit mocktail with ID: " + id);
                return Response.status(Response.Status.BAD_REQUEST).entity("Failed to edit mocktail.").build();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while editing mocktail with ID: " + id, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error while editing mocktail.").build();
        }
    }


    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a mocktail by ID", description = "Deletes a mocktail recipe by its ID.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Mocktail deleted successfully"),
            @APIResponse(responseCode = "404", description = "Mocktail not found")
    })
    @Retry(maxRetries = 3)
    @Timeout(200)
    public Response deleteMocktail(@PathParam("id") String id) {
        logger.info("Deleting mocktail with ID: " + id);
        try {
            String result = customerService.deleteMocktailById(id);
            if ("Mocktail deleted successfully".equals(result)) {
                logger.info("Mocktail deleted successfully with ID: " + id);
                return Response.ok(result).build();
            } else {
                logger.warning("Failed to delete mocktail with ID: " + id);
                return Response.status(Response.Status.NOT_FOUND).entity(result).build();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while deleting mocktail with ID: " + id, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error while deleting mocktail.").build();
        }
    }
}
