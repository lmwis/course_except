package com.fehead.course.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fehead.course.dao.GroupMapper;
import com.fehead.course.dao.UserMapper;
import com.fehead.course.dao.entity.Group;
import com.fehead.course.dao.entity.User;
import com.fehead.lang.controller.BaseController;
import com.fehead.lang.error.BusinessException;
import com.fehead.lang.error.EmBusinessError;
import com.fehead.lang.response.CommonReturnType;
import com.fehead.lang.response.FeheadResponse;
import com.fehead.course.utils.RandomUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.binding.BindingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lmwis
 * @description:
 * @date 2019-09-02 16:09
 * @Version 1.0
 */
@RestController
@RequestMapping("/group")
public class GroupController extends BaseController {


    @Autowired
    GroupMapper groupMapper;
    @Autowired
    UserMapper userMapper;

    @PostMapping()
    @ApiOperation("创建组织与部门")
    public FeheadResponse createGroup(@RequestParam("user_id") int userId, String name, @RequestParam("branch_name") String branchName) throws BusinessException {
        if(!validateNull(name,branchName)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        if(userMapper.selectById(userId)==null){ // 用户检查
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        List<Group> groups = new ArrayList<>();
        String[] branchNames = branchName.split(",");
        for (String s : branchNames) {
            QueryWrapper<Group> queryWrapper = new QueryWrapper();
            queryWrapper.eq("group_name",name);
            queryWrapper.eq("branch_name",s);
            Group groupInSql = groupMapper.selectOne(queryWrapper);
            if(groupInSql!=null){ // 组已经存在
                throw new BusinessException(EmBusinessError.USER_NOT_EXIST,"组织或部门已经存在");
            }
            Group group = new Group();
            group.setBranchName(s);
            group.setGroupName(name);
            group.setUserId(userId);
            // 生成随机密钥
            group.setGroupKey(RandomUtil.getStringRandom(10));
            groups.add(group);

            // 插入数据库
            groupMapper.insert(group);
        }



        return CommonReturnType.create(groups);

    }

    /**
     * 根据id查询组织
     * @param id
     * @return
     * @throws BusinessException
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询组织和部门")
    public FeheadResponse getGroupById(@RequestParam("user_id") int userId,@PathVariable("id")int id) throws BusinessException {

        groupActionValidate(userId,id);

        Group group = groupMapper.selectById(id);

        return CommonReturnType.create(group);

    }

    /**
     * 修改组织名
     *  仅仅组织创建者能够修改
     * @param id
     * @param name
     * @return
     * @throws BusinessException
     */
    @PutMapping()
    @ApiOperation("修改组织和部门的名字，仅组织创建者能够修改")
    public FeheadResponse updateGroupById(@RequestParam("user_id") int userId,int id
            ,String name,@RequestParam("branch_name") String branchName) throws BusinessException {

        Group group = groupMapper.selectById(id);
        if(userId!=group.getUserId()){ // 权限判断
            throw new BusinessException(EmBusinessError.SERVICE_REQUIRE_ROLE_ADMIN);
        }
        group.setGroupKey(name);
        group.setBranchName(branchName);
        groupMapper.updateById(group);

        return CommonReturnType.create(group);

    }

    /**
     * 根据key加入组织
     * @param userId
     * @param key
     * @return
     * @throws BusinessException
     */
    @PutMapping("/apply")
    @ApiOperation("加入组织的部门")
    public FeheadResponse applyGroup(@RequestParam("user_id") int userId,String key) throws BusinessException {

        if(userMapper.selectById(userId)==null){ // 用户检查
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        Group group = groupMapper.selectByKey(key);
        try {
            if(groupMapper.selectUserIdAndGroupIdEqu(userId,group.getId())!=0){ // 判断是否已经加入
                throw new BusinessException(EmBusinessError.USER_ALREAY_EXIST,"已经加入该组织");
            }
        }catch (BindingException exception){    // 表示未加入组织
            if(group==null){ // 密钥不符合
                throw new BusinessException(EmBusinessError.USER_NOT_EXIST,"请求组织不存在");
            }
            groupMapper.applyGroup(userId,group.getId());
            return CommonReturnType.create(group);
        }

        return null;

    }

    /**
     * 查看自己加入的组织
     * @param userId
     * @return
     * @throws BusinessException
     */
    @GetMapping("/attendGroup")
    @ApiOperation("查看自己所参与的组织")
    public FeheadResponse getMyAttendGroup(@RequestParam("user_id") int userId) throws BusinessException {
        if(userMapper.selectById(userId)==null){ // 用户检查
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        List<Group> groups = groupMapper.selectAttendGroups(userId);
        return CommonReturnType.create(groups);

    }

    /**
     * 查看自己管理的组织
     * @param userId
     * @return
     * @throws BusinessException
     */
    @GetMapping("/adminGroup")
    @ApiOperation("查看自己管理的组织")
    public FeheadResponse getMyAdminGroup(@RequestParam("user_id") int userId) throws BusinessException {
        if(userMapper.selectById(userId)==null){ // 用户检查
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }

        List<Group> groups = groupMapper.selectAdminGroups(userId);
        return CommonReturnType.create(groups);

    }

    /**
     * 对组织进行操作的时候进行校验
     * 校验
     * @param userId
     * @param id
     * @return
     * @throws BusinessException
     */
    protected boolean groupActionValidate(int userId,int id) throws BusinessException {
        if(userMapper.selectById(userId)==null){ // 用户检查
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        Group group = groupMapper.selectById(id);
        if(group==null){ // 组织不存在
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST,"请求组织不存在");
        }
        if(userId!=group.getUserId()){ // 权限判断
            throw new BusinessException(EmBusinessError.SERVICE_REQUIRE_ROLE_ADMIN);
        }
        return true;
    }

    /**
     * 解散部门
     *  若解散的是最后一个部门 则组织自动消失
     * @param userId
     * @param id
     * @return
     * @throws BusinessException
     */
    @GetMapping("/delete")
    @ApiOperation("解散部门 若解散的是最后一个部门 则组织自动消失")
    public FeheadResponse deleteGroup(@RequestParam("user_id") int userId,int id) throws BusinessException {

        // 校验
        groupActionValidate(userId,id);

        // 删除
        groupMapper.deleteById(id);
        // 再删除组织人员关联
        groupMapper.deleteGroupUser(id);
        return CommonReturnType.create(null);

    }

    /**
     * 返回一个组织里的所有人
     * @param userId
     * @param id
     * @return
     * @throws BusinessException
     */
    @GetMapping("/users")
    @ApiOperation(value = "获取组织里面的所有成员，包括创建者",response = CommonReturnType.class)
    public FeheadResponse getGroupUsers(@RequestParam("user_id") int userId,int id) throws BusinessException {
        // 校验
        groupActionValidate(userId,id);

        List<User> users = groupMapper.selectAllUsersByGroupId(id);
        User user = userMapper.selectById(userId);
        users.add(user);

        return CommonReturnType.create(users);
    }

}
