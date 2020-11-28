package com.coderman.api.system.converter;

import com.coderman.api.common.pojo.system.User;
import com.coderman.api.system.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author
 * @Date 2020/3/7 19:56
 * @Version 1.0
 **/
@Component
public class UserConverter {


    /**
     * 转voList
     * @param users
     * @return
     */
    public  List<UserVO> converterToUserVOList(List<User> users){
        List<UserVO> userVOS=new ArrayList<>();
        if(!CollectionUtils.isEmpty(users)){
            for (User user : users) {
                UserVO userVO = converterToUserVO(user);
                userVOS.add(userVO);
            }
        }
        return userVOS;
    }

    /**
     * 转vo
     * @return
     */
    public  UserVO converterToUserVO(User user){
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user,userVO);
        return userVO;
    }


}
