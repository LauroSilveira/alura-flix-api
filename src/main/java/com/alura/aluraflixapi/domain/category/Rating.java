package com.alura.aluraflixapi.domain.category;

public enum Rating {

  FREE(1, "Free", "#FFFFFF"),
  FANTASY(2, "Fantasy", "#FFD700"),
  TERROR(3, "Terror", "#FF0000"),
  ACTION(4, "Action", "#008000"),
  TRILLER(5, "Thiller", "#808080"),
  ROMANTIC_COMEDY(6, "Romantic Comedy", "#E41B17");

  public int getCategoryId() {
    return categoryId;
  }

  public String getTitle() {
    return title;
  }

  Rating(int categoryId, String title, String hexDecimalColor) {
    this.categoryId = categoryId;
    this.title = title;
    this.hexDecimalColor = hexDecimalColor;
  }

  public String getHexDecimalColor() {
    return hexDecimalColor;
  }

  final int categoryId;

  final String title;

  final String hexDecimalColor;
}
