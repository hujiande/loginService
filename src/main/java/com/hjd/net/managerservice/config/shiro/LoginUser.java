package com.hjd.net.managerservice.config.shiro;

import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @author hujiande
 *
 */
@Data
public class LoginUser implements Serializable {

    private static final long serialVersionUID = 1L;

    public Long userId;          // 主键ID
    public String account;      // 账号
    public String name;         // 姓名
	public Long orgId;      // 组织ID


	public LoginUser() {
	}

	public LoginUser(String account, Long orgId) {
		this.account=account;
		this.orgId=orgId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
}
