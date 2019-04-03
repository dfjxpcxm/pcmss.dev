package com.quick.portal.ldapmng;


import com.quick.portal.userDepartment.UserDepartmentDO;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Transactional
@Service("orgLdapMngDao")
public class OrgLdapMngDaoImpl implements IOrgLdapMngDao {
	

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
	private DistinguishedName getDn(String orgId) {
		//得到根目录，也就是配置文件中配置的ldap的根目录
		DistinguishedName newContactDN = new DistinguishedName(supDn);
		// 添加cn，即使得该条记录的dn为"cn=cn,根目录",例如"cn=abc,dc=testdc,dc=com"
		newContactDN.add("ou", orgId);
		return newContactDN;
	}



	
	/*
	 * 删除LDAP用户信息
	 * (non-Javadoc)
	 * orgID:  depVO.getDep_global_id().concat("_").concat(depVO.getDep_name());
	 */
	public void removeOrgLdapInfo(String orgId) {
		ldapTemplate.unbind(getDn(orgId));
		
	}
	
	/*
	 * 新增LDAP用户信息

	 */
	public void saveOrgLdapInfo(UserDepartmentDO person) {
		BasicAttribute basicAttr = new BasicAttribute("objectclass");
		basicAttr.add("top");
		basicAttr.add("organizationalUnit");
		Attributes attr = new BasicAttributes();
		attr.put(basicAttr);
		//必填属性，不能为null也不能为空字符串
		attr.put("businessCategory", person.getDep_global_id());
		attr.put("description", person.getDep_name());

		//可选字段需要判断是否为空，如果为空则不能添加
		if (person.getDep_level() != null) {
			attr.put("postalAddress", person.getDep_state().toString());
		}

		if (person.getSup_dep_global_id() != null && !"".equals(person.getSup_dep_global_id())) {
			attr.put("postalCode", person.getSup_dep_global_id());
		}

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String date = df.format(new Date());
		attr.put("destinationIndicator", date);

		//bind方法即是添加一条记录。
		try{
			ldapTemplate.bind(getDn(person.getDep_global_id()), null, attr);
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	


	
	/*
	 * 修改LDAP用户信息 
	 * (non-Javadoc)
	 * @see com.ccbm.ldap.userldapmng.dao.IUserLdapMngDao#updateUserLdapInfo(com.ccbm.ldap.userldapmng.domain.model.Person)
	 */
	public void updateOrgLdapInfo(UserDepartmentDO person) {
		if (person == null || person.getDep_global_id() == null
				|| person.getDep_global_id().length() <= 0) {
			return;
		}
		List<ModificationItem> mList = new ArrayList<ModificationItem>();

		mList.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("businessCategory",person.getDep_global_id())));
		mList.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("description",person.getDep_name())));
		mList.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("postalAddress",person.getDep_state().toString())));
		mList.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("postalCode",person.getSup_dep_global_id())));

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String date = df.format(new Date());

		mList.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute("destinationIndicator",date)));

		if (mList.size() > 0) {
			ModificationItem[] mArray = new ModificationItem[mList.size()];
			for (int i = 0; i < mList.size(); i++) {
				mArray[i] = mList.get(i);
			}
			//modifyAttributes 方法是修改对象的操作，与rebind（）方法需要区别开
//			ldapTemplate.modifyAttributes(this.getDn(person.getUserid()), mArray);
			try{
				ldapTemplate.modifyAttributes(this.getDn(person.getDep_global_id()), mArray);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}

//	public final  static String supDn = "dc=pcdomain,dc=com";

	public final  static String supDn = "";

}
