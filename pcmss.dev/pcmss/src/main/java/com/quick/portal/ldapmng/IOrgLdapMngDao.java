package com.quick.portal.ldapmng;


import com.quick.portal.userDepartment.UserDepartmentDO;

public interface IOrgLdapMngDao {

	
	/*
	 * 新增LDAP用户信息
	 */
	public void saveOrgLdapInfo(UserDepartmentDO person);

	
	/*
	 * 修改LDAP用户信息 
	 */
	public void updateOrgLdapInfo(UserDepartmentDO person);
	
	
	/*
	 * 新增LDAP用户信息 
	 */
	public void removeOrgLdapInfo(String dep_global_id);
	
}
