package com.coderman.api.book.vo;

import com.coderman.api.system.vo.MenuNodeVO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
public class CategoryListVo {

    private Long id;

    @NotBlank(message = "图书检索名称")
    private String name;

    private Long sort;

    private Long pid;

    private List<CategoryListVo> children=new ArrayList<>();


    /*
     * 排序,根据Sort排序
     */
    public static Comparator<CategoryListVo> sort(){
        Comparator<CategoryListVo> comparator = (o1, o2) -> {
            if(o1.getSort() != o2.getSort()){
                return (int) (o1.getSort() - o2.getSort());
            }
            return 0;
        };
        return comparator;
    }
}
