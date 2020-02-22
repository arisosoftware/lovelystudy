package com.lovelystudy.module.security.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lovelystudy.module.security.mapper.RoleMapper;
import com.lovelystudy.module.security.pojo.Role;
import com.lovelystudy.module.security.pojo.RolePermission;

/**
 * 
 */
@Service
@Transactional
public class RoleService {

	@Autowired
	private AdminUserService adminUserService;
	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private RolePermissionService rolePermissionService;

	public void delete(Integer id) {
		rolePermissionService.deleteRoleId(id);
		roleMapper.deleteByPrimaryKey(id);
		// 删除redis里的用户缓存
		adminUserService.deleteAllRedisAdminUser();
	}

	public List<Role> findAll() {
		return roleMapper.findAll();
	}

	public Role findById(Integer id) {
		return roleMapper.selectByPrimaryKey(id);
	}

	public void saveOrUpdate(Integer id, String name, Integer[] permissionIds) {
		Role role = new Role();
		if (id != null) {
			role = findById(id);
		}
		role.setName(name);
		if (role.getId() == null) {
			roleMapper.insertSelective(role);
		} else {
			roleMapper.updateByPrimaryKeySelective(role);
		}
		// 保存角色权限关联
		rolePermissionService.deleteRoleId(role.getId());
		if (permissionIds.length > 0) {
			List<RolePermission> list = new ArrayList<>();
			for (Integer permissionId : permissionIds) {
				RolePermission rolePermission = new RolePermission();
				rolePermission.setRoleId(role.getId());
				rolePermission.setPermissionId(permissionId);
				list.add(rolePermission);
			}
			rolePermissionService.save(list);
		}
		// 删除redis里的用户缓存
		adminUserService.deleteAllRedisAdminUser();
	}

}
