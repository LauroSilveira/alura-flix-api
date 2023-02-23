package com.alura.aluraflixapi.domain.category;

public enum Rating {

  FREE(1),
  FANTASY(2),
  TERROR(3),
  TRILLER(3);

  final int categoryId;

  Rating(int categoryId) {
    this.categoryId = categoryId;
  }
}
