package com.springboot.library.dto.request;

import com.springboot.library.enums.Genre;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookUpdateDto(
        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Author is required")
        String author,

        @NotNull(message = "Publication year is required")
        @Min(value = 1000, message = "Publication year must be 1000 or later")
        @Max(value = 2100, message = "Publication year must be 2100 or earlier")
        Integer publicationYear,

        @NotNull(message = "Genre is required")
        Genre genre
) {
}
