package org.example.tamaapi.feignClient.item;

import lombok.*;
import org.example.tamaapi.domain.Gender;
import org.example.tamaapi.domain.item.Category;
import org.example.tamaapi.domain.item.Item;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class ItemResponse {

    private Long id;

    private Integer originalPrice;

    private Integer nowPrice;

    private Gender gender;

    private String yearSeason;

    private String name;

    private String description;

    private LocalDate dateOfManufacture;

    private String countryOfManufacture;

    private String manufacturer;

    private Long categoryId;

    private String textile;

    private String precaution;

    public Item toEntity () {
        Category category = new Category(categoryId);
        return new Item(id, originalPrice, nowPrice, gender, yearSeason, name, description, dateOfManufacture, countryOfManufacture, manufacturer, category, textile, precaution);
    }
}
