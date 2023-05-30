package com.hoyong.ilote.core.constants;

public class CommonConstants {

	/* **************************** INTERCEPTOR EXCLUDE PATTERN **************************** */
	public static final String[] INCLUDE_PATH_PATTERNS = {"/v1/api/**","/v2/api/**"}; //인터셉터가 참조하는 mapping 목록
	public static final String[] EXCLUDE_PATH_PATTERNS = {"/v1/api-docs"}; //인터셉터가 제외 되어야할 요청 주소 mapping 목록
}
