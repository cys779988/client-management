package dev.be.exception;

public enum ErrorCode {
	INTERNAL_SERVER_ERROR(500, "C_001", "서버 에러"), 
	METHOD_NOT_ALLOWED(405, "C_002", "지원하지 않는 메서드입니다."),
	INVALID_INPUT_VALUE(400, "C_003", "적절하지 않은 요청 값입니다."),
	INVALID_TYPE_VALUE(400, "C_004", "요청 값의 타입이 잘못되었습니다."),
	EMPTY_RESULT_DATA_ACCESS(400, "C_005", "삭제할 데이터가 없습니다."),
	
	AUTH_ERROR(400, "AU_001", "인증 관련 오류가 발생했습니다."),
	DUPLICATED_EMAIL(400, "AU_002", "이미 존재하는 E-mail입니다."),
	DUPLICATED_NAME(400, "AU_002", "이미 존재하는 이름입니다."),
	BAD_LOGIN_EAMIL(400, "AU_003", "잘못된 아이디 입니다."),
	BAD_LOGIN_PASSWORD(400, "AU_004", "잘못된 패스워드 입니다."),
	BAD_LOGIN_EMPTY_VALUE(400, "AU_005", "아이디와 비밀번호를 입력해주세요.");
	
	private final int status;
	private final String code;
	private final String message;

	ErrorCode(int status, String code, String message) {
		this.status = status;
		this.message = message;
		this.code = code;
	}

	public String getMessage() {
		return this.message;
	}

	public String getCode() {
		return code;
	}

	public int getStatus() {
		return status;
	}
}
