package com.coderman.api.book.converter;

import com.coderman.api.book.vo.BookVo;
import com.coderman.api.book.vo.CategoryListVo;
import com.coderman.api.common.pojo.book.Category;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class CategoryListConverter {

     /**
     * 转voList
     * @param categories
     * @return
     */
    public static List<CategoryListVo> converterToVOList(List<Category> categories) {
        List<CategoryListVo> categoryListVos=new ArrayList<>();
        if(!CollectionUtils.isEmpty(categories)){
            for (Category category : categories) {
                CategoryListVo categoryListVo=new CategoryListVo();
                BeanUtils.copyProperties(category,categoryListVo);
                categoryListVos.add(categoryListVo);
            }
        }
        return categoryListVos;
    }
}
