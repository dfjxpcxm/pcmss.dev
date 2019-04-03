package com.quick.portal.ldapmng;


import com.quick.portal.sysUser.SysUserDO;

public interface IUserLdapMngDao{

	
	/*
	 * 新增LDAP用户信息
	 */
	public void saveUserLdapInfo(SysUserDO person);

	
	/*
	 * 修改LDAP用户信息 
	 */
	public void updateUserLdapInfo(SysUserDO person);
	
	
	/*
	 * 新增LDAP用户信息 
	 */
	public void removeUserLdapInfo(String userid);
	
}
