package serialization;

import java.io.Serializable;

public class Target implements Serializable {

    private String username;
    private int age;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Target)) {
            return false;
        }

        Target person = (Target) obj;

        if (!this.username.equals(person.getUsername())) {
            return false;
        }

        if (this.age != person.getAge()) {
            return false;
        }

        if (!this.password.equals(person.getPassword())) {
            return false;
        }

        return true;
    }
}
