package com.andrezasecon.app.factory;

import com.andrezasecon.app.entities.Category;

public class CategoryFactory {
	
	public static Category createCategory() {
			
			return new Category(2L, "Electronics");
		}

}
