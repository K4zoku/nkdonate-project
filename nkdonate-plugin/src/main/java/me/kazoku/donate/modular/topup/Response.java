package me.kazoku.donate.modular.topup;

public class Response {
  private final boolean success;
  private final String msg;

  public Response(boolean success, String msg) {
    this.success = success;
    this.msg = msg;
  }

  public boolean isSuccess() {
    return success;
  }

  public String getMsg() {
    return msg;
  }
}
