package hhxy.dn.wph.mapper;

import hhxy.dn.wph.entity.*;
import hhxy.dn.wph.mapper.provider.UserProvider;
import org.apache.ibatis.annotations.*;


import java.util.List;

import static hhxy.dn.wph.util.DBTableUtil.*;

/**
 * @Author: 邓宁
 * @Date: Created in 13:39 2018/11/12
 */
public interface UserMapper {
    String user_field = " user_no,user_id,nickname,telephone,sex,birthday,email,vip,headImage ";

    //注册用户基本信息
    @Insert("insert into"+ USER +
            "(user_id,telephone,nickname,status,vip,headImage,created) " +
            "values (#{user_id},#{telephone},#{nickname},#{status},#{vip},#{headImage},#{created})")
    @Options(useGeneratedKeys = true,keyProperty = "user_no")
    Integer userRegister(User user);

    //注册用户密码
    @Insert("insert into"+ USER_PWD +"(password,user_no)values(#{password},#{user_no})")
    Integer userPasswordRegister(UserPassword userPassword);

    //查询用户信息
    @Select("select"+ user_field +"from"+ USER +"where telephone = #{telephone} and status = 1")
    User getUserByTelephone(String telephone);

    //查询用户密码
    @Select("select password from"+ USER_PWD +"where user_no = #{user_no}")
    String getUserPassword(Integer user_no);

    //查询手机号是否注册
    @Select("select telephone from"+ USER +"where telephone = #{telephone}")
    String userCheckTelephone(String telephone);

    //更新用户信息
    @UpdateProvider(type = UserProvider.class,method = "updateUser")
    Integer updateUser(User user);

    //添加用户收货地址
    @InsertProvider(type = UserProvider.class,method = "saveUserAddress")
    Integer saveUserAddress(UserAddress address);

    //更新用户收货地址
    @UpdateProvider(type = UserProvider.class,method = "updateUserAddress")
    Integer updateUserAddress(UserAddress address);

    //根据收货地址编号找用户ID
    @Select("select user_id from"+ USER_ADDRESS +"where address_id=#{address_id}")
    Integer findUserIdByAddressId(Integer address_id);

    //重置默认收货地址
    @Update("update"+ USER_ADDRESS +"set is_default = 0 where user_id = #{user_id}")
    Integer updateAllUserAddress(Integer user_id);

    //更新默认收货地址
    @Update("update"+ USER_ADDRESS +"set is_default = 1 where address_id = #{address_id}")
    Integer updateDefaultUserAddress(Integer address_id);

    //删除用户收货地址
    @Delete("delete from"+ USER_ADDRESS +"where address_id = #{address_id}")
    Integer deleteUserAddressByAddressID(Integer address_id);

    //查询用户所有收货地址
    @SelectProvider(type = UserProvider.class,method = "findAllUserAddress")
    List<UserAddress> findAllUserAddress(Integer user_id);

    //用户搜索商品关键词
    @Insert("insert into"+ SEARCH_HISTORY +"(user_id,search_title)values(#{user_id},#{search_title})")
    Integer saveSearchHistory(@Param("search_title") String search_title,@Param("user_id") Integer user_id);

    /*
     * @Description:查询搜索历史记录
     * 按照主键search_id 降序排列，查询前6条数据(后6条记录)
     */
    @Select("SELECT search_title FROM "+SEARCH_HISTORY+" " +
            "WHERE user_id=#{user_id} ORDER BY search_id DESC LIMIT 6")
    List<String> findAllSearchHistory(Integer user_id);

    //用户清除搜索历史记录
    @Delete("delete from "+SEARCH_HISTORY+" where user_id=#{user_id}")
    Integer deleteAllSearchHistory(Integer user_id);

    //用户收藏商户
    @Insert("insert into"+ USER_COLLECT_SELLER +
            "(user_id,seller_id)values(user_id = #{user_id},seller_id = #{seller_id})")
    Integer collectSeller(UserCollectSeller collect);

    //根据用户ID获得收藏的商户
    @Select("select * from"+ SELLER +"where seller_id in(" +
            "select seller_id from"+ USER_COLLECT_SELLER +"where user_id = #{userId})")
    List<Seller> getCollectSellerByUserId(Integer userId);

    //更新用户头像
    @Update("update"+ USER +"set headImage = #{originalFilename} where user_id = #{userId}")
    Integer updateUserHeadIcon(@Param("originalFilename")String originalFilename,@Param("userId") Integer userId);

    //查询用户是否收藏了该商户
    @Select("select user_id,seller_id from" + USER_COLLECT_SELLER +
            "where seller_id = #{sellerId} and user_id = #{userId}")
    UserCollectSeller selectUserCollectSeller(@Param("sellerId") Integer sellerId, @Param("userId") Integer userId);

    //根据用户编号获取用户信息
    @Select("select"+ user_field +"from"+ USER +"where user_no = #{user_no} and status = 1")
    User getUserByNo(Integer user_no);
}
