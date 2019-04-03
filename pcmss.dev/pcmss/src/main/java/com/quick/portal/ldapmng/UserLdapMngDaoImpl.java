package com.quick.portal.ldapmng;

import com.quick.portal.sysUser.SysUserDO;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import java.util.ArrayList;
import java.util.List;


@Transactional
@Service("userLdapMngDao")
public class UserLdapMngDaoImpl implements IUserLdapMngDao {
	

	//注入spring的LdapTemplate，此处在spring的配置文件中需要配置
	private LdapTemplate ldapTemplate;

	public LdapTemplate getLdapTemplate() {
		return ldapTemplate;
	}
	public void setLdapTemplate(LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}
	



	/**
	 * 得到dn
	 * @param
	 * @return
	 */
	private DistinguishedName getDn(String userid) {
		//得到根目录，也就是配置文件中配置的ldap的根目录
		DistinguishedName newContactDN = new DistinguishedName(supDn);
		// 添加cn，即使得该条记录的dn为"cn=cn,根目录",例如"cn=abc,dc=testdc,dc=com"
		newContactDN.add("cn", userid);
		return newContactDN;
	}



	
	/*
	 * 删除LDAP用户信息
	 * (non-Javadoc)
	 * userid:person.getUser_real_name().concat("_").concat(person.getUser_name()
	 */
	public void removeUserLdapInfo(String userid) {
		ldapTemplate.unbind(getDn(userid));
		
	}
	
	/*
	 * 新增LDAP用户信息

	 */
	public void saveUserLdapInfo(SysUserDO person) {
		// TODO Auto-generated method stub
		BasicAttribute basicAttr = new BasicAttribute("objectclass");
		basicAttr.add("top");
		basicAttr.add("inetOrgPerson");
		Attributes attr = new BasicAttributes();
		attr.put(basicAttr);
		//必填属性，不能为null也不能为空字符串
		attr.put("sn", person.getUser_real_name());
		attr.put("givenName", person.getUser_name());

		//可选字段需要判断是否为空，如果为空则不能添加
		if (person.getUser_email() != null
				&& person.getUser_email().length() > 0) {
			attr.put("mail", person.getUser_email());
		}

		if (person.getUser_state() != null) {
			attr.put("employeeType", person.getUser_state().toString());
		}
		if (person.getUser_password() != null
				&& person.getUser_password().length() > 0) {
			attr.put("carLicense", person.getUser_password());
		}
		if (person.getUser_tel() != null
				&& person.getUser_tel().length() > 0) {
			attr.put("telexNumber", person.getUser_tel());
		}
		//bind方法即是添加一条记录。
		try{
			ldapTemplate.bind(getDn(person.getUser_name()), null, attr);
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	


	
	/*
	 * 修改LDAP用户信息 
	 * (non-Javadoc)
	 * @see com.ccbm.ldap.userldapmng.dao.IUserLdapMngDao#updateUserLdapInfo(com.ccbm.ldap.userldapmng.domain.model.Person)
	 */
	public void updateUserLdapInfo(SysUserDO person) {
		if (person == null || person.getUser_name() == null
				|| person.getUser_name().length() <= 0) {
			return;
		}
		List<ModificationItem> mList = new ArrayList<ModificationItem>();

		mList.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("sn",person.getUser_real_name())));
		
		mList.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("givenName",person.getUser_name())));
		



		mList.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("mail",person.getUser_email())));

		mList.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("employeeType",person.getUser_state().toString())));
		mList.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("carLicense",person.getUser_password())));
		mList.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("telexNumber",person.getUser_tel())));
		if (mList.size() > 0) {
			ModificationItem[] mArray = new ModificationItem[mList.size()];
			for (int i = 0; i < mList.size(); i++) {
				mArray[i] = mList.get(i);
			}
			//modifyAttributes 方法是修改对象的操作，与rebind（）方法需要区别开
//			ldapTemplate.modifyAttributes(this.getDn(person.getUserid()), mArray);
			try{
				ldapTemplate.modifyAttributes(this.getDn(person.getUser_name()), mArray);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}


//	public final  static String supDn = "dc=pcdomain,dc=com";

	public final  static String supDn = "";

}
