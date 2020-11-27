package com.coderman.api.system.service.impl;

import com.coderman.api.common.bean.ActiveUser;
import com.coderman.api.common.config.jwt.JWTToken;
import com.coderman.api.common.exception.ErrorCodeEnum;
import com.coderman.api.common.exception.ServiceException;
import com.coderman.api.common.pojo.system.*;
import com.coderman.api.common.utils.JWTUtils;
import com.coderman.api.common.utils.MD5Utils;
import com.coderman.api.system.converter.UserConverter;
import com.coderman.api.system.enums.UserStatusEnum;
import com.coderman.api.system.enums.UserTypeEnum;
import com.coderman.api.system.mapper.*;
import com.coderman.api.system.service.UserService;
import com.coderman.api.system.vo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.validation.constraints.NotBlank;
import java.util.*;

/**
 * @Author
 * @Date 2020/3/7 15:44
 * @Version 1.0
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserConverter userConverter;




    /**
     * 查询用户
     * @param name 用户名
     * @return
     */
    @Override
    public User findUserByName(String name) {
        User t = new User();
        t.setUsername(name);
        return userMapper.selectOne(t);
    }



    /**
     * 用户列表
     * @param userVO
     * @return
     */
    @Override
    public PageVO<UserVO> findUserList(Integer pageNum,Integer pageSize,UserVO userVO) {
        //已经拥有的
        PageHelper.startPage(pageNum,pageSize);
        Example o = new Example(User.class);
        String username = userVO.getUsername();
        String nickname = userVO.getNickname();
        Integer sex = userVO.getSex();
        String email = userVO.getEmail();
        Example.Criteria criteria = o.createCriteria();
        if(username!=null&&!"".equals(username)){
            criteria.andLike("username","%"+username+"%");
        }
        if(nickname!=null&&!"".equals(nickname)){
            criteria.andLike("nickname","%"+nickname+"%");
        }
        if(email!=null&&!"".equals(email)){
            criteria.andLike("email","%"+email+"%");
        }
        if(sex!=null){
            criteria.andEqualTo("sex",sex);
        }
        criteria.andNotEqualTo("type",1);
        List<User> userList = userMapper.selectByExample(o);
        List<UserVO> userVOS = userConverter.converterToUserVOList(userList);
        PageInfo<User> info=new PageInfo<>(userList);
        return new PageVO<>(info.getTotal(),userVOS);
    }



    /**
     * 更新用户禁用状态
     * @param id
     * @param status
     */
    @Override
    public void updateStatus(Long id, Boolean status) {
        User dbUser = userMapper.selectByPrimaryKey(id);
        if(dbUser==null){
            throw new ServiceException("要更新状态的用户不存在");
        }
        ActiveUser activeUser= (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        if(dbUser.getId().equals(activeUser.getUser().getId())){
            throw new ServiceException(ErrorCodeEnum.DoNotAllowToDisableTheCurrentUser);
        }else {
            User t = new User();
            t.setId(id);
            t.setStatus(status? UserStatusEnum.DISABLE.getStatusCode() :
                    UserStatusEnum.AVAILABLE.getStatusCode());
            userMapper.updateByPrimaryKeySelective(t);
        }
    }

    /**
     * 添加用户
     * @param userAddVO
     */
    @Transactional
    @Override
    public void add(UserAddVO userAddVO) {
        @NotBlank(message = "用户名不能为空") String username = userAddVO.getUsername();
        Example o = new Example(User.class);
        o.createCriteria().andEqualTo("username",username);
        int i = userMapper.selectCountByExample(o);
        if(i!=0){
            throw new ServiceException("该用户名已被占用");
        }
        User user = new User();
        BeanUtils.copyProperties(userAddVO,user);
        String salt=UUID.randomUUID().toString().substring(0,32);
        user.setPassword(MD5Utils.md5Encryption(user.getPassword(), salt));
        user.setModifiedTime(new Date());
        user.setCreateTime(new Date());
        user.setBirth(new Date());
        user.setNickname(username);
        user.setSex(0);
        user.setPhoneNumber("15689665688");
        user.setEmail("book@163.com");
        user.setSalt(salt);
        user.setType(UserTypeEnum.SYSTEM_USER.getTypeCode());//添加的用户默认是普通用户
        user.setStatus(UserStatusEnum.AVAILABLE.getStatusCode());//添加的用户默认启用
        user.setAvatar("http://badidol.com/uploads/images/avatars/201910/24/18_1571921832_HG9E55x9NY.jpg");
        userMapper.insert(user);
    }

    /**
     * 更新
     * @param id
     * @param userVO
     */
    @Transactional
    @Override
    public void update(Long id,UserEditVO userVO) {
        User dbUser = userMapper.selectByPrimaryKey(id);
        @NotBlank(message = "用户名不能为空") String username = userVO.getUsername();
        if(dbUser==null){
            throw new ServiceException("要删除的用户不存在");
        }
        Example o = new Example(User.class);
        o.createCriteria().andEqualTo("username",username);
        List<User> users = userMapper.selectByExample(o);
        if(!CollectionUtils.isEmpty(users)){
            if(!users.get(0).getId().equals(id)){
                throw new ServiceException("该用户名已被占用");
            }
        }
        User user = new User();
        BeanUtils.copyProperties(userVO,user);
        user.setModifiedTime(new Date());
        user.setId(dbUser.getId());
        userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 编辑
     * @param id
     * @return
     */
    @Transactional
    @Override
    public UserEditVO edit(Long id) {
        User user = userMapper.selectByPrimaryKey(id);
        if(user==null){
            throw new ServiceException("要编辑的用户不存在");
        }
        UserEditVO userEditVO = new UserEditVO();
        BeanUtils.copyProperties(user,userEditVO);
        return userEditVO;
    }


    /**
     * 用户登入
     * @param username
     * @param password
     * @return
     */
    @Override
    public Map login(String username, String password) {
        Map map=new HashMap();
        String token;
        User user = findUserByName(username);
        if (user != null) {
            String salt = user.getSalt();
            //秘钥为盐
            String target = MD5Utils.md5Encryption(password, salt);
            //生成Token
            token = JWTUtils.sign(username, target);
            JWTToken jwtToken = new JWTToken(token);
            try {
                SecurityUtils.getSubject().login(jwtToken);
            } catch (AuthenticationException e) {
                throw new ServiceException(e.getMessage());
            }
        } else {
            throw new ServiceException(ErrorCodeEnum.USER_ACCOUNT_NOT_FOUND);
        }
        map.put("token",token);
        map.put("user",user);
        return map;
    }


    /**
     * 用户详情
     *
     * @return
     */
    @Override
    public UserVO info() {
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        Long id=activeUser.getUser().getId();
        User user = userMapper.selectByPrimaryKey(id);
        UserVO userVO=new UserVO();
        BeanUtils.copyProperties(user,userVO);
        return userVO;
    }
}
