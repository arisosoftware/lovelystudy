package com.lovelystudy.web.admin;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import com.lovelystudy.core.base.BaseController;
import com.lovelystudy.core.bean.Return;
import com.lovelystudy.module.security.pojo.Role;
import com.lovelystudy.module.security.pojo.RolePermission;
import com.lovelystudy.module.security.service.PermissionService;
import com.lovelystudy.module.security.service.RolePermissionService;
import com.lovelystudy.module.security.service.RoleService;

/**
 * 
 */
@Controller
@RequestMapping("/admin/role")
public class RoleController extends BaseController {

	@Autowired
	private PermissionService permissionService;
	@Autowired
	private RolePermissionService rolePermissionService;
	@Autowired
	private RoleService roleService;

	@GetMapping("/add")
	public String add(Model model) {
		List<Map<String, Object>> node = permissionService.findAll();
		model.addAttribute("data", new Gson().toJson(node));
		return "admin/role/add";
	}

	@GetMapping("/delete")
	public String delete(Integer id) {
		roleService.delete(id);
		return redirect("/admin/role/list");
	}

	@GetMapping("/edit")
	public String edit(Integer id, Model model) {
		Role role = roleService.findById(id);
		List<Map<String, Object>> node = permissionService.findAll();
		List<RolePermission> rolePermissions = rolePermissionService.findByRoleId(id);
		model.addAttribute("role", role);
		model.addAttribute("data", new Gson().toJson(node));
		model.addAttribute("rolePermissions", rolePermissions);
		return "admin/role/edit";
	}

	@GetMapping("/list")
	public String list(Model model) {
		List<Role> roles = roleService.findAll();
		model.addAttribute("roles", roles);
		return "admin/role/list";
	}

	@PostMapping("/add")
	@ResponseBody
	public Return save(@RequestParam("nodeIds[]") Integer[] nodeIds, String name) {
		roleService.saveOrUpdate(null, name, nodeIds);
		return Return.success();
	}

	@PostMapping("/edit")
	@ResponseBody
	public Return update(Integer id, @RequestParam("nodeIds[]") Integer[] nodeIds, String name) {
		roleService.saveOrUpdate(id, name, nodeIds);
		return Return.success();
	}
}
