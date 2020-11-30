package com.coderman.api.system.service;

import com.coderman.api.common.pojo.system.User;
import com.coderman.api.system.vo.*;

import java.util.List;
import java.util.Map;

/**
 * @Author
 * @Date 2020/3/7 15:43
 * @Version 1.0
 **/
public interface UserService {

    /**
     * 根据用户名查询用户
     *
     * @param name 用户名
     * @return
     */
     User findUserByName(String name);




    /**
     * 用户列表
     * @param userVO
     * @return
     */
    PageVO<UserVO> findUserList(Integer pageNum,Integer pageSize,UserVO userVO);



    /**
     * 更新状态
     *
     * @param id
     * @param status
     */
    void updateStatus(Long id, Integer status);

    /**
     * 添加用户
     * @param userAddVO
     */
    void add(UserAddVO userAddVO);

    /**
     * 更新用户
     *
     * @param userVO
     * @param userVO
     */
    void update(UserEditVO userVO);





    /**
     * 用户登入
     *
     * @param userAddVO
     * @return
     */
    Map login(UserAddVO userAddVO);


    /**
     * 用户详情
     *
     * @return
     */
    UserVO info(User user);

}
