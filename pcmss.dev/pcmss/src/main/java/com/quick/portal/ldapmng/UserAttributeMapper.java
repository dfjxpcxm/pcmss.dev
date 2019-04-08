package com.quick.portal.ldapmng;

import com.quick.portal.sysUser.SysUserDO;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;

/**
 * 这个类的作用是将ldap中的属性转化为实体类的属性值，
 * 在查询信息的时候会用到
 */
public class UserAttributeMapper implements ContextMapper  {

    public Object mapFromContext(Object ctx) {
        DirContextAdapter context = (DirContextAdapter)ctx;
        SysUserDO person = new SysUserDO();
        person.setUser_real_name(context.getStringAttribute("sn"));
        person.setUser_name(context.getStringAttribute("givenName"));
        person.setUser_name(context.getStringAttribute("cn"));
        person.setUser_state(Integer.valueOf(context.getStringAttribute("employeeType")));
        person.setUser_email(context.getStringAttribute("mail"));
        person.setUser_password(context.getStringAttribute("carLicense"));
        person.setUser_tel(context.getStringAttribute("telexNumber"));

        return person;
    }


}