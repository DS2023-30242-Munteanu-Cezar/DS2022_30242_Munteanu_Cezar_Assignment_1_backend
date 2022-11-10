package MunteanuCezar.SD1;

import MunteanuCezar.SD1.entities.Role;
import MunteanuCezar.SD1.entities.User;
import MunteanuCezar.SD1.repositories.RoleRepository;
import MunteanuCezar.SD1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class Sd1Application {

	@Autowired
	RoleRepository roleRepository;


	public static void main(String[] args) {
		SpringApplication.run(Sd1Application.class, args);
	}

//	@PostConstruct
//	public void help(){
//		Role role = new Role();
//		role.setRoleCode("ADMIN");
//		role.setRoleDescription("Admin of aplication!");
//		roleRepository.save(role);
//
//		Role role2 = new Role();
//		role2.setRoleCode("USER");
//		role2.setRoleDescription("User of aplication!");
//		roleRepository.save(role2);
//
//	}

}
