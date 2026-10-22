package com.springboot.library.dto.response;

import com.springboot.library.enums.Genre;

public record BookInfoDto(
        Long id,
        String title,
        String author,
        String isbn,
        int publicationYear,
        Genre genre
) {
}

