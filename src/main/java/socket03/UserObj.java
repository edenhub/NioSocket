package socket03;

import java.io.Serializable;

/**
 * Created by adam on 2015/5/2.
 */
public class UserObj implements Serializable {

    private String username;
    private String password;
    private String msg;

    public UserObj(String username,String password){
        this.username = username;
        this.password = password;
    }

    public UserObj(String username, String password, String msg) {
        this.username = username;
        this.password = password;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "UserObj{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
