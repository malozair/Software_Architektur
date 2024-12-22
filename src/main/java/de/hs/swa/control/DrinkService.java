package de.hs.swa.control;

import de.hs.swa.boundary.DrinkDTO;
import de.hs.swa.entity.Mocktail;
import de.hs.swa.gateway.CocktailApiClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class DrinkService {

    @Inject
    @RestClient
    CocktailApiClient cocktailApiClient;

    @Inject
    KatalogService katalogService;

    public List<DrinkDTO> searchByCategory(String category) {
        if ("mocktail".equalsIgnoreCase(category)) {
            return katalogService.readMocktailEntities().stream()
                    .map(this::mapToDrinkDTO)
                    .collect(Collectors.toList());
        } else if ("cocktail".equalsIgnoreCase(category)) {
            return cocktailApiClient.searchByCategory(category).getDrinks();
        }
        throw new IllegalArgumentException("Invalid category: " + category);
    }

    public List<DrinkDTO> searchByName(String name) {
        List<DrinkDTO> mocktails = katalogService.readMocktailEntities().stream()
                .filter(mocktail -> mocktail.getName().equalsIgnoreCase(name))
                .map(this::mapToDrinkDTO)
                .collect(Collectors.toList());

        List<DrinkDTO> cocktails = cocktailApiClient.searchByName(name).getDrinks();
        mocktails.addAll(cocktails);
        return mocktails;
    }

    public List<DrinkDTO> searchByIngredient(String ingredient) {
        List<DrinkDTO> mocktails = katalogService.readMocktailEntities().stream()
                .filter(mocktail -> mocktail.getIngredient().stream()
                        .anyMatch(i -> i.getName().equalsIgnoreCase(ingredient)))
                .map(this::mapToDrinkDTO)
                .collect(Collectors.toList());

        // Handle cocktails
        List<DrinkDTO> cocktails = cocktailApiClient.searchByIngredient(ingredient).getDrinks().stream()
                .map(drink -> new DrinkDTO(
                        drink.getId(),
                        drink.getName(),
                        "cocktail",
                        List.of(ingredient) // Add the searched ingredient manually
                ))
                .collect(Collectors.toList());

        mocktails.addAll(cocktails);
        return mocktails;
    }

    private DrinkDTO mapToDrinkDTO(Mocktail mocktail) {
        return new DrinkDTO(
                mocktail.getId(),
                mocktail.getName(),
                "mocktail",
                mocktail.getIngredient().stream()
                        .map(i -> i.getName())
                        .collect(Collectors.toList())
        );
    }
}
