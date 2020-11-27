package com.coderman.api.common.utils;

import com.coderman.api.book.vo.CategoryListVo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 该类用于递归构建树形菜单
 * Created by  on 2020/2/6 15:34
 */
public class CategoryTreeBuilder {



    /**
     * 构建多级树
     * @param nodes
     * @return
     */
    public static List<CategoryListVo> build(List<CategoryListVo> nodes){
        //根节点
        List<CategoryListVo> rootCategory = new ArrayList<>();
        for (CategoryListVo nav : nodes) {
            if(nav.getPid()==0){
                rootCategory.add(nav);
            }
        }
        /* 根据Category类的sort排序 */
        Collections.sort(rootCategory,CategoryListVo.sort());
        /*为根菜单设置子菜单，getChild是递归调用的*/
        for (CategoryListVo nav : rootCategory) {
            /* 获取根节点下的所有子节点 使用getChild方法*/
            List<CategoryListVo> childList = getChild(nav.getId(), nodes);
            nav.setChildren(childList);//给根节点设置子节点
        }
        return rootCategory;
    }


    /**
     * 获取子
     * @param id
     * @param nodes
     * @return
     */
    private static List<CategoryListVo> getChild(Long id, List<CategoryListVo> nodes) {
        //子菜单
        List<CategoryListVo> childList = new ArrayList<CategoryListVo>();
        for (CategoryListVo nav : nodes) {
            // 遍历所有节点，将所有菜单的父id与传过来的根节点的id比较
            //相等说明：为该根节点的子节点。
            if(nav.getPid().equals(id)){
                childList.add(nav);
            }
        }
        //递归
        for (CategoryListVo nav : childList) {
            nav.setChildren(getChild(nav.getId(), nodes));
        }
        Collections.sort(childList,CategoryListVo.sort());//排序
        //如果节点下没有子节点，返回一个空List（递归退出）
        if(childList.size() == 0){
            return new ArrayList<CategoryListVo>();
        }
        return childList;
    }


}
