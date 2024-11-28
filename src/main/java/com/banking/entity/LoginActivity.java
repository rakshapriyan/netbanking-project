package com.banking.entity;

public class LoginActivity {
	private Long loginId;
	private Long userId;
	private Long loginTimestamp;
	private Long logoutTimestamp;

	// Getters and Setters
	public Long getLoginId() {
		return loginId;
	}

	public void setLoginId(Long loginId) {
		this.loginId = loginId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getLoginTimestamp() {
		return loginTimestamp;
	}

	public void setLoginTimestamp(Long loginTimestamp) {
		this.loginTimestamp = loginTimestamp;
	}

	public Long getLogoutTimestamp() {
		return logoutTimestamp;
	}

	public void setLogoutTimestamp(Long logoutTimestamp) {
		this.logoutTimestamp = logoutTimestamp;
	}
}
