package de.hs.swa.control;

import de.hs.swa.boundary.MocktailMapper;
import de.hs.swa.boundary.MocktailDTO;
import de.hs.swa.entity.Ingredient;
import de.hs.swa.entity.Mocktail;
import de.hs.swa.entity.CustomerKatalog;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class KatalogService {

    @Inject
    CustomerKatalog customerKatalog;

    public String addMocktail(MocktailDTO mocktailDTO) {
        String id = UUID.randomUUID().toString();
        Mocktail mocktail = new Mocktail(
                id,
                mocktailDTO.getIngredients().stream()
                        .map(dto -> new Ingredient(dto.getName(), dto.getMenge()))
                        .collect(Collectors.toList()),
                mocktailDTO.getName()
        );

        boolean result = customerKatalog.addMocktail(mocktail);
        return result ? id : null;
    }


    public MocktailDTO readMocktail(String id) {
        return customerKatalog.getMocktailList().stream()
                .filter(mocktail -> mocktail.getId().equals(id))
                .findFirst()
                .map(MocktailMapper::toDTO)
                .orElse(null);
    }

    public Set<MocktailDTO> readMocktails() {
        return customerKatalog.getMocktailList().stream()
                .map(MocktailMapper::toDTO)
                .collect(Collectors.toSet());
    }

    public Set<Mocktail> readMocktailEntities() {
        return customerKatalog.getMocktailList();
    }

    public String editMocktail(MocktailDTO mocktailDTO) {
        Mocktail mocktail = MocktailMapper.fromDTO(mocktailDTO);
        boolean result = customerKatalog.editMocktail(mocktail);

        return result ? "Mocktail edited successfully" : "Mocktail couldn't be edited";
    }


    public String deleteMocktail(String id) {
        boolean result = customerKatalog.getMocktailList().removeIf(mocktail -> mocktail.getId().equals(id));
        return result ? "Mocktail deleted successfully" : "Mocktail not found";
    }

}
