package com.quick.portal.ldapmng;

import com.quick.portal.sysUser.SysUserDO;
import com.quick.portal.userDepartment.UserDepartmentDO;
import org.springframework.ldap.core.DistinguishedName;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

public class LdapADHelper {
    public LdapADHelper() {
    }
    private String host,url,adminName,adminPassword;
    private LdapContext ctx = null;
    /**
     * 初始化ldap
     */
    public void initLdap(){
        //ad服务器
        this.host = "192.168.52.137:389"; // AD服务器
        this.url = new String("ldap://" + host );//默认端口为80的可以不用填写，其他端口需要填写，如ldap://xxx.com:8080
        this.adminName = "cn=Manager,dc=pcdomain,dc=com";// 注意用户名的写法：domain\User 或  User@domain.com
        this.adminPassword = "ldap123";
        Hashtable HashEnv = new Hashtable();
        HashEnv.put(Context.SECURITY_AUTHENTICATION, "simple"); // LDAP访问安全级别
        HashEnv.put(Context.SECURITY_PRINCIPAL, adminName); // AD User
        HashEnv.put(Context.SECURITY_CREDENTIALS, adminPassword); // AD Password
        HashEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory"); // LDAP工厂类
        HashEnv.put(Context.PROVIDER_URL, url);
        try {
            ctx = new InitialLdapContext(HashEnv, null);
            System.out.println("初始化ldap成功！");
        } catch (NamingException e) {
            e.printStackTrace();
            System.err.println("Throw Exception : " + e);
        }
    }
    /**
     * 关闭ldap
     */
    public void closeLdap(){
        try {
            this.ctx.close();
        } catch (NamingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /**
     *
     * @param type organizationalUnit:组织架构 group：用户组 user|person：用户
     * @param name
     * @return
     */
    public String GetADInfo(String type ,String filter ,String name) {

        String userName = name; // 用户名称
        if (userName == null) {
            userName = "";
        }
        String company = "";
        String result = "";
        try {
            // 域节点
            String searchBase = "dc=pcdomain,dc=com";
            // LDAP搜索过滤器类
            //cn=*name*模糊查询 cn=name 精确查询
//          String searchFilter = "(objectClass="+type+")";
            String searchFilter = "(&(objectClass="+type+")("+filter+"=*" + name + "*))";
            // 创建搜索控制器
            SearchControls searchCtls = new SearchControls();
            //  设置搜索范围
            searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
//          String returnedAtts[] = {  "memberOf" }; // 定制返回属性
//      searchCtls.setReturningAttributes(returnedAtts); // 设置返回属性集 不设置则返回所有属性
            // 根据设置的域节点、过滤器类和搜索控制器搜索LDAP得到结果
            NamingEnumeration answer = ctx.search(searchBase, searchFilter,searchCtls);// Search for objects using the filter

            // 初始化搜索结果数为0
            int totalResults = 0;// Specify the attributes to return
            int rows = 0;
            while (answer.hasMoreElements()) {// 遍历结果集
                SearchResult sr = (SearchResult) answer.next();// 得到符合搜索条件的DN
                ++rows;
                String dn = sr.getName();
                System.out.println(dn);
                Attributes Attrs = sr.getAttributes();// 得到符合条件的属性集
                if (Attrs != null) {
                    try {
                        for (NamingEnumeration ne = Attrs.getAll(); ne.hasMore();) {
                            Attribute Attr = (Attribute) ne.next();// 得到下一个属性
                            System.out.println(" AttributeID=属性名："+ Attr.getID().toString());
                            // 读取属性值
                            for (NamingEnumeration e = Attr.getAll(); e.hasMore(); totalResults++) {
                                company = e.next().toString();
                                System.out.println("    AttributeValues=属性值："+ company);
                            }
                            System.out.println("    ---------------");

                        }
                    } catch (NamingException e) {
                        System.err.println("Throw Exception : " + e);
                    }
                }// if
            }// while
            System.out.println("************************************************");
            System.out.println("Number: " + totalResults);
            System.out.println("总共用户数："+rows);
        } catch (NamingException e) {
            e.printStackTrace();
            System.err.println("Throw Exception : " + e);
        }
        return result;
    }

    public static void main(String args[]) {
        // 实例化
        LdapADHelper ad = new LdapADHelper();
        ad.initLdap();
//        ad.GetADInfo("inetOrgPerson","cn","test");//查找用户
//        ad.GetADInfo("organ","ou","福建厦门");//查找组织架构

//        ad.GetADInfo("user","cn","李XX");//查找用户
//        ad.GetADInfo("organizationalUnit","ou","工程");//查找组织架构
//        ad.GetADInfo("group","cn","福建xxx");//查找用户组

/*        Map<String, String> map = new HashMap<String, String>();
        map.put("deptCode", "a01");
        map.put("ou", "软件部");
        map.put("description", "这是一个部门节点");
        map.put("state", "正常");*/
//        ad.insertOu(map,"");

        SysUserDO userVO  = new SysUserDO();
        userVO.setUser_tel("12345678901");
        userVO.setUser_real_name("测试201");
        userVO.setUser_name("test2");
        userVO.setUser_id(111);
        userVO.setUser_email("test2@163.com");
        userVO.setUser_password("123456");
        userVO.setUser_state(1);

//        ad.saveUserLdapInfo(userVO,"dc=pcdomain,dc=com");


        UserDepartmentDO depVO = new UserDepartmentDO();
        depVO.setDep_global_id("dep_008");
        depVO.setDep_name("测试部3");
        depVO.setDep_state(1);
        depVO.setSup_dep_global_id("dep_003");
//        depVO.setCre_time(new Date());

//        ad.saveOrgLdapInfo(depVO,"dc=pcdomain,dc=com");


        String orgId = depVO.getDep_global_id().concat("_").concat(depVO.getDep_name());


//        ad.removeOrgLdapInfo(orgId);

        ad.updateOrgLdapInfo(depVO);


        ad.closeLdap();
    }






    public void saveUserLdapInfo(SysUserDO person, String supDn) {
        // TODO Auto-generated method stub
        BasicAttribute basicAttr = new BasicAttribute("objectclass");
        basicAttr.add("inetOrgPerson");
        basicAttr.add("top");

//        basicAttr.add("posixAccount");

        Attributes attr = new BasicAttributes();
        attr.put(basicAttr);
        //必填属性，不能为null也不能为空字符串
        attr.put("sn", person.getUser_real_name());
//        attr.put("Last name", person.getUser_name());
//       attr.put("posixAccount", person.getUser_id());
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
        DistinguishedName newContactDN = new DistinguishedName(supDn);
 //       newContactDN.add("uid", "雄安");
        newContactDN.add("cn",person.getUser_real_name().concat("_").concat(person.getUser_name()));
        //bind方法即是添加一条记录。
        try{
            ctx.bind(newContactDN,null,attr);
        } catch (NamingException e) {
            e.printStackTrace();
            System.err.println("Throw Exception : " + e);
        }
    }


    public void saveOrgLdapInfo(UserDepartmentDO person,String supDn) {
        BasicAttribute basicAttr = new BasicAttribute("objectclass");
        basicAttr.add("top");
        basicAttr.add("organizationalUnit");
        Attributes attr = new BasicAttributes();
        attr.put(basicAttr);
        //必填属性，不能为null也不能为空字符串
//		attr.put("description", person.getDep_name());
        attr.put("businessCategory", person.getDep_global_id());
        attr.put("description", person.getDep_name());

        //可选字段需要判断是否为空，如果为空则不能添加
        if (person.getDep_state() != null) {
            attr.put("postalAddress", person.getDep_state());
        }

        if (person.getSup_dep_global_id() != null && !"".equals(person.getSup_dep_global_id())) {
            attr.put("postalCode", person.getSup_dep_global_id());
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String date = df.format(new Date());
        attr.put("destinationIndicator", date);


//        attr.put("preferredDeliveryMethod", date);


        //bind方法即是添加一条记录。
  /*      try{
            ctx.bind(getDn(person.getDep_global_id()), null, attr);
        }catch(Exception e){
            e.printStackTrace();
        }*/

        DistinguishedName newContactDN = new DistinguishedName(supDn);
        newContactDN.add("ou",person.getDep_global_id().concat("_").concat(person.getDep_name()));
        //bind方法即是添加一条记录。
        try{
            ctx.bind(newContactDN,null,attr);
        } catch (NamingException e) {
            e.printStackTrace();
            System.err.println("Throw Exception : " + e);
        }

    }

    public void updateOrgLdapInfo(UserDepartmentDO person) {

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

    /*    mList.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                new BasicAttribute("preferredDeliveryMethod",person.getUpd_time())));
*/
        if (mList.size() > 0) {
            ModificationItem[] mArray = new ModificationItem[mList.size()];
            for (int i = 0; i < mList.size(); i++) {
                mArray[i] = mList.get(i);
            }
            //modifyAttributes 方法是修改对象的操作，与rebind（）方法需要区别开
//			ldapTemplate.modifyAttributes(this.getDn(person.getUserid()), mArray);

          String id =person.getDep_global_id().concat("_").concat(person.getDep_name());
            try{
                ctx.modifyAttributes(this.getDn(id), mArray);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }


    private DistinguishedName getDn(String orgId) {
        //得到根目录，也就是配置文件中配置的ldap的根目录
        DistinguishedName newContactDN = new DistinguishedName(supDn);
        // 添加cn，即使得该条记录的dn为"cn=cn,根目录",例如"cn=abc,dc=testdc,dc=com"
        newContactDN.add("ou", orgId);
        return newContactDN;
    }


    public void removeOrgLdapInfo(String orgId) {
        try{
            ctx.unbind(getDn(orgId));
        } catch (NamingException e) {
            e.printStackTrace();
            System.err.println("Throw Exception : " + e);
        }

    }




 public final  static String supDn = "dc=pcdomain,dc=com";


}