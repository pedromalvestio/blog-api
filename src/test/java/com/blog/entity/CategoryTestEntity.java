package com.blog.entity;

import com.blog.model.Category;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomUtils.nextLong;

public class CategoryTestEntity {

    public static Category createGenericCategory(){
        Category category = new Category();
        category.setId(nextLong(1,100));
        category.setName(randomAlphanumeric(30));

        return category;
    }

}
