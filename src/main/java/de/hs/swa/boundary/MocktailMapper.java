package de.hs.swa.boundary;

import de.hs.swa.entity.Mocktail;
import de.hs.swa.entity.Ingredient;

import java.util.List;
import java.util.stream.Collectors;

public class MocktailMapper {

    public static MocktailDTO toDTO(Mocktail mocktail) {
        return new MocktailDTO(
                mocktail.getId(),
                mocktail.getName(),
                mocktail.getIngredient().stream()
                        .map(ingredient -> new IngredientDTO(ingredient.getName(), ingredient.getMenge()))
                        .collect(Collectors.toList())
        );
    }

    public static Mocktail fromDTO(MocktailDTO mocktailDTO) {
        return new Mocktail(
                mocktailDTO.getId(),
                mocktailDTO.getIngredients().stream()
                        .map(dto -> new Ingredient(dto.getName(), dto.getMenge()))
                        .collect(Collectors.toList()),
                mocktailDTO.getName()
        );
    }
}
