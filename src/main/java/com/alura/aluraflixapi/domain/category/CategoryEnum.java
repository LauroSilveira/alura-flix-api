package com.alura.aluraflixapi.domain.category;

public enum CategoryEnum {

  FREE(1),
  FANTASY(2),
  TERROR(3),
  TRILLER(3);

  final int categoryId;

  CategoryEnum(int categoryId) {
    this.categoryId = categoryId;
  }
}
