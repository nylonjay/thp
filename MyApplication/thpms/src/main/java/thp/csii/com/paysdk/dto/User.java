package thp.csii.com.paysdk.dto;

public class User {

    private Integer id;

    /**
     * 用户名
     */
    private String uname;

    /**
     * 账号
     */
    private String acno;

    /**
     * 密码
     */
    private String password;

    /**
     * 联系电话
     */
    private String tel;

    /**
     * 会员id
     */
    private String memberid;

    /**
     * VIPNO
     */
    private String vipno;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 是否设置支付密码 00 未设置 01 已设置
     */
    private String pin_tag;

    public String getPin_tag() {
        return pin_tag;
    }

    public void setPin_tag(String pin_tag) {
        this.pin_tag = pin_tag;
    }

    public User() {
    }

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取用户名
     *
     * @return uname - 用户名
     */
    public String getUname() {
        return uname;
    }

    /**
     * 设置用户名
     *
     * @param uname 用户名
     */
    public void setUname(String uname) {
        this.uname = uname;
    }

    /**
     * 获取账号
     *
     * @return acno - 账号
     */
    public String getAcno() {
        return acno;
    }

    /**
     * 设置账号
     *
     * @param acno 账号
     */
    public void setAcno(String acno) {
        this.acno = acno;
    }

    /**
     * 获取密码
     *
     * @return password - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取联系电话
     *
     * @return tel - 联系电话
     */
    public String getTel() {
        return tel;
    }

    /**
     * 设置联系电话
     *
     * @param tel 联系电话
     */
    public void setTel(String tel) {
        this.tel = tel;
    }

    /**
     * 获取会员id
     *
     * @return memberid - 会员id
     */
    public String getMemberid() {
        return memberid;
    }

    /**
     * 设置会员id
     *
     * @param memberid 会员id
     */
    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    /**
     * 获取VIPNO
     *
     * @return vipno - VIPNO
     */
    public String getVipno() {
        return vipno;
    }

    /**
     * 设置VIPNO
     *
     * @param vipno VIPNO
     */
    public void setVipno(String vipno) {
        this.vipno = vipno;
    }

    /**
     * 获取邮箱
     *
     * @return email - 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱
     *
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", uname='" + uname + '\'' +
                ", acno='" + acno + '\'' +
                ", password='" + password + '\'' +
                ", tel='" + tel + '\'' +
                ", memberid='" + memberid + '\'' +
                ", vipno='" + vipno + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}