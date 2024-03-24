package com.g11.savingtrack.constants;

public class PassbookManagerConstants {

  public static class CommonConstants {
    public static final String ENCODING_UTF_8 = "UTF-8";
    public static final String LANGUAGE = "Accept-Language";
  }

  public static class AuditorConstant {
    public static final String ANONYMOUS = "anonymousUser";
    public static final String SYSTEM = "SYSTEM";
  }

  public static class StatusException {
    public static final Integer NOT_FOUND = 404;
    public static final Integer CONFLICT = 409;
    public static final Integer BAD_REQUEST = 400;
  }

  public static class MessageException {

  }

  public static class AuthConstant {
    public static String TYPE_TOKEN = "Bear ";
    public static String AUTHORIZATION = "Authorization";
    public static String CLAIM_USERNAME_KEY = "username";
    public static String CLAIM_AUTHORITIES_KEY = "authorities";
    public static Integer ENABLED = 1;
    public static Integer DISABLED = 0;
  }

  public static class MessageCode {
    public static String SUCCESS = "Success";
    public static final String CONFIRM_PASSWORD_NOT_MATCH = "ValidationConfirmPassword";
  }

  public static class InvalidMessageException {
    public static final String INVALID_USERNAME = "com.group11.passbook-manager.validation.account.invalidUserName";
    public static final String WRONG_FORMAT_PASSWORD = "com.group11.passbook-manager.validation.account.invalidPassword";
    public static final String INVALID_EMAIL = "com.group11.passbook-manager.validation.account.invalidEmail";
    public static final String INVALID_PHONE_NUMBER = "com.group11.passbook-manager.validation.account.invalidPhoneNumber";
    public static final String INVALID_FULL_NAME = "com.group11.passbook-manager.validation.user.invalidFullName";
    public static final String INVALID_DATE_FORMAT = "com.group11.passbook-manager.validation.user.invalidDateFormat";
    public static final String NOT_NULL_OR_EMPTY = " can't be null, empty, or blank";
  }

  public static class ValueOfBank{
    public static final double INTEREST_RATE_NO_TERM = 0.5;
  }
}
