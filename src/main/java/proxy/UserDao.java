package proxy;

public class UserDao implements IUserDao {

    @Override
    public void save() {
        System.out.println("保存数据");
    }

    @Override
    public void delete() {
        System.out.println("删除数据");
    }
}
