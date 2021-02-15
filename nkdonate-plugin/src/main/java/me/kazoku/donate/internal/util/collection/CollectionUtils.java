package me.kazoku.donate.internal.util.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class CollectionUtils {

  public static boolean isValidElementType(Collection<?> collection, Class<?> elementType) {
    return !collection.isEmpty() && collection.stream().allMatch(elementType::isInstance);
  }

  public static boolean isStringCollection(Collection<?> collection) {
    return isValidElementType(collection, String.class);
  }

  public static boolean isStringCollection(Object o) {
    return o instanceof Collection<?> && isStringCollection((Collection<?>) o);
  }

  @SuppressWarnings("unchecked")
  public static Optional<List<String>> objToStrList(Object o) {
    if (isStringCollection(o)) return Optional.of(new ArrayList<>((Collection<String>) o));
    else return Optional.empty();
  }

}
