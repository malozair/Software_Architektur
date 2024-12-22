package de.hs.swa.gateway;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@RegisterRestClient(baseUri = "https://www.thecocktaildb.com/api/json/v1/1")
public interface CocktailApiClient {

    @GET
    @Path("/filter.php")
    DrinkResponseWrapper searchByIngredient(@QueryParam("i") String ingredient);

    @GET
    @Path("/search.php")
    DrinkResponseWrapper searchByName(@QueryParam("s") String name);

    @GET
    @Path("/filter.php")
    DrinkResponseWrapper searchByCategory(@QueryParam("c") String category);
}
