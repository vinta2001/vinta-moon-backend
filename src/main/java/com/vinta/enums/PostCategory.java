package com.vinta.enums;


import lombok.Getter;

@Getter
public enum PostCategory {
    FASHION(1, "时尚", "fashion"),
    LIFESTYLE(2, "生活", "lifestyle"),
    COSMETICS(3, "美妆", "cosmetics"),
    FOOD(4, "美食", "food"),
    MOVIE_AND_TV(5, "影视", "movie_and_tv"),
    CAREER(6, "职场", "career"),
    LOVE(7, "情感", "love"),
    HOUSEHOLD_PRODUCT(8, "家居", "household_product"),
    GAMING(9, "游戏", "gaming"),
    TRAVEL(10, "旅行", "travel"),
    FITNESS(11, "健身", "fitness"),
    ;

    private final Integer id;
    private final String name;
    private final String desc;

    PostCategory(Integer id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }

    public static PostCategory getCategoryByName(String name) {
        for (PostCategory category : PostCategory.values()) {
            if (category.getName().equals(name)) {
                return category;
            }
        }
        return null;
    }
}
