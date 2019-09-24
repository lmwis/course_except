package com.fehead.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fehead.dao.entity.Group;
import com.fehead.dao.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author lmwis
 * @description:
 * @date 2019-09-02 16:08
 * @Version 1.0
 */
public interface GroupMapper extends BaseMapper<Group> {


    @Insert("insert into group_user (group_id,user_id) values(#{groupId},#{userId})")
    public void applyGroup(long userId,long groupId);

    @Select("SELECT*FROM group_info LEFT JOIN group_user " +
            "ON group_info.id = group_user.group_id WHERE group_user.user_id=#{userId}")
    public List<Group> selectAttendGroups(long userId);

    @Select("select * from group_info where group_key=#{key}")
    public Group selectByKey(@Param("key") String key);

    @Select("select * from group_info WHERE user_id=#{userId}")
    public List<Group> selectAdminGroups(long userId);

    @Select("select id from group_user where group_id=#{groupId} and user_id=#{userId}")
    public int selectUserIdAndGroupIdEqu(@Param("userId") long userId,@Param("groupId") long groupId);

    @Select("select * from user_info LEFT JOIN group_user " +
            "on group_user.user_id=user_info.id where group_user.group_id=#{groupId}")
    public List<User> selectAllUsersByGroupId(int groupId);

    @Select("select * from user_info LEFT JOIN group_info " +
            "on group_info.user_id=user_info.id where group_info.id=#{groupId}")
    public User selectCreatorInfo(int groupId);

    @Delete("delete from group_user where group_id=#{id}")
    public void deleteGroupUser(int id);

//    @Select("select * from group")
//    public Group selectByGroupNameAndBranchName(@Param("groupName")String groupName,@Param("branchName")String branchName);
}
