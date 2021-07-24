package data;

import entities.User;
import services.UserService;

import java.util.List;

public class DBTest {

	public static void main(String[] args) {
		UserService userService = new UserService();
        List<User> users = userService.findAll();
        users.forEach(user -> System.out.println(user.toString()));
	}

}
