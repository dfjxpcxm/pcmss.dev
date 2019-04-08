package com.quick.portal.ldapmng;

import com.quick.portal.userDepartment.UserDepartmentDO;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;

/**
 * 这个类的作用是将ldap中的属性转化为实体类的属性值，
 * 在查询信息的时候会用到
 */
public class OrgAttributeMapper implements ContextMapper  {

    public Object mapFromContext(Object ctx) {
        DirContextAdapter context = (DirContextAdapter)ctx;
        UserDepartmentDO departmentDO = new UserDepartmentDO();
        departmentDO.setDep_global_id(context.getStringAttribute("businessCategory"));
        departmentDO.setDep_name(context.getStringAttribute("description"));
        departmentDO.setDep_level(Integer.valueOf(context.getStringAttribute("postalAddress")));
        departmentDO.setSup_dep_global_id(context.getStringAttribute("postalCode"));
        return departmentDO;
    }




}