package com.quick.portal.ldapmng;


import com.quick.portal.userDepartment.UserDepartmentDO;

public interface IOrgLdapMngDao {

	
	/*
	 * 新增LDAP机构信息
	 */
	public void saveOrgLdapInfo(UserDepartmentDO person);

	
	/*
	 * 修改LDAP机构信息
	 */
	public void updateOrgLdapInfo(UserDepartmentDO person);
	
	
	/*
	 * 新增LDAP机构信息
	 */
	public void removeOrgLdapInfo(String dep_global_id);




	/*
	 * 查询LDAP机构信息
	 */
	public int searchOrgLdapCnt(UserDepartmentDO person);
	
}
