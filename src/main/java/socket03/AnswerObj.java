package socket03;

import java.io.Serializable;

/**
 * Created by adam on 2015/5/2.
 */
public class AnswerObj implements Serializable {
    private int code;
    private String reason;

    public AnswerObj(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "AnswerObj{" +
                "code=" + code +
                ", reason='" + reason + '\'' +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
