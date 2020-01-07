package com.softserve.edu.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class FieldForSearchController<T> {

    private Class<T> clazz;
    private Map<String, String> map = new HashMap<>();

    public FieldForSearchController(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Map<String, String> findAnnotation() {

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(FieldForSearch.class)) {
                FieldForSearch searchField = field
                        .getAnnotation(FieldForSearch.class);
                map.put(field.getName(), searchField.nameForView());

            }
        }
        return map;
    }
}
