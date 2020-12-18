package me.kazoku.donate.internal.util.collection;

import java.util.Collection;

public class CollectionUtils {
  public static boolean isValidElementType(Collection<?> collection, Class<?> elementType) {
    return !collection.isEmpty() && collection.stream().allMatch(elementType::isInstance);
  }

  public static boolean isStringCollection(Collection<?> collection) {
    return isValidElementType(collection, String.class);
  }
}
