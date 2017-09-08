package com.masget.shiro.chapter16.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.masget.shiro.chapter16.entity.Role;

/**
 * <p>Role: Zhang Kaitao
 * <p>Date: 14-1-28
 * <p>Version: 1.0
 */
@Repository
public class RoleDaoImpl implements RoleDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public Role createRole(final Role role) {
    	//查询当前表最大id
    	
        final String sql = "insert into shiro.sys_role(id,role, description, resource_ids, available) values(?,?,?,?,?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement psst = connection.prepareStatement(sql, new String[]{"id"});
                int count = 1;
                psst.setInt(count++, getMaxId("shiro.sys_role"));
                psst.setString(count++, role.getRole());
                psst.setString(count++, role.getDescription());
                psst.setString(count++, role.getResourceIdsStr());
                psst.setBoolean(count++, role.getAvailable());
                return psst;
            }
        }, keyHolder);
        role.setId(keyHolder.getKey().longValue());
        return role;
    }

    
    public Role updateRole(Role role) {
        final String sql = "update shiro.sys_role set role=?, description=?, resource_ids=?, available=? where id=?";
        jdbcTemplate.update(
                sql,
                role.getRole(), role.getDescription(), role.getResourceIdsStr(), role.getAvailable(), role.getId());
        return role;
    }

    public void deleteRole(Long roleId) {
        final String sql = "delete from shiro.sys_role where id=?";
        jdbcTemplate.update(sql, roleId);
    }


    
    public Role findOne(Long roleId) {
        final String sql = "select id, role, description, resource_ids as resourceIdsStr, available from shiro.sys_role where id=?";
        List<Role> roleList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Role.class), roleId);
        if(roleList.size() == 0) {
            return null;
        }
        return roleList.get(0);
    }

    
    public List<Role> findAll() {
        final String sql = "select id, role, description, resource_ids as resourceIdsStr, available from shiro.sys_role";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(Role.class));
    }
    
    @SuppressWarnings("unused")
	private  int getMaxId(String tableName){
    	 String sql = "select max(id) id from "+tableName;
    	 int id = jdbcTemplate.queryForInt(sql, null);
         return id+1;
    }

}
