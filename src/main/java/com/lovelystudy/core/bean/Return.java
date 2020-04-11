package com.lovelystudy.core.bean;
 
public class Return {

	public static Return error() {
		return error(null);
	}

	public static Return error(int code, String description) {
		Return result = new Return();
		result.setCode(code);
		result.setDescription(description);
		result.setDetail(null);
		return result;
	}

	public static Return error(String description) {
		return error(201, description);
	}

	public static Return success() {
		return success(null);
	}

	public static Return success(Object detail) {
		Return result = new Return();
		result.setCode(200);
		result.setDescription("success");
		result.setDetail(detail);
		return result;
	}

	private int code;

	private String description;

	private Object detail;

	public int getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public Object getDetail() {
		return detail;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDetail(Object detail) {
		this.detail = detail;
	}
}
