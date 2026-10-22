package com.springboot.library.dto.request;

import com.springboot.library.enums.Genre;
import jakarta.validation.constraints.*;

public record BookReqDto(

        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Author is required")
        String author,

        @NotBlank(message = "ISBN is required")
        @Size(message = "ISBN must be 10 or 13 digits (ISBN-10 may end with X)")
        String isbn,

        @NotNull(message = "Publication year is required")
        @Min(value = 1000, message = "Publication year must be 1000 or later")
        @Max(value = 2100, message = "Publication year must be 2100 or earlier")
        Integer publicationYear,

        @NotNull(message = "Genre is required")
        Genre genre
) {
}

