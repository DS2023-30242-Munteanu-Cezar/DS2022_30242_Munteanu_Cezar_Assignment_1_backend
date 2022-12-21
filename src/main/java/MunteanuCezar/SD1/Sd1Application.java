package MunteanuCezar.SD1;

import MunteanuCezar.SD1.entities.Measurement;
import MunteanuCezar.SD1.entities.Role;
import MunteanuCezar.SD1.entities.User;
import MunteanuCezar.SD1.rabbitMQ.Worker;
import MunteanuCezar.SD1.repositories.DeviceRepository;
import MunteanuCezar.SD1.repositories.MeasurementRepository;
import MunteanuCezar.SD1.repositories.RoleRepository;
import MunteanuCezar.SD1.repositories.UserRepository;
import MunteanuCezar.SD1.services.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SpringBootApplication
public class Sd1Application {

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	MeasurementRepository measurementRepository;

	@Autowired
	DeviceRepository deviceRepository;


	public static void main(String[] args) {
		SpringApplication.run(Sd1Application.class, args);
	}

	@PostConstruct
	public void help(){

		List<Role> roles = new ArrayList<Role>();
		roles = roleRepository.findAll();
		boolean exist = false;

		for(Role r : roles){
			if(Objects.equals(r.getRoleCode(), "ADMIN") || Objects.equals(r.getRoleCode(), "USER")){
				exist = true;
			}
		}

		if(!exist) {
			Role role = new Role();
			role.setRoleCode("ADMIN");
			role.setRoleDescription("Admin of aplication!");
			roleRepository.save(role);

			Role role2 = new Role();
			role2.setRoleCode("USER");
			role2.setRoleDescription("User of aplication!");
			roleRepository.save(role2);
		}

	}

	@PostConstruct
	public void start(){
		MeasurementService service = new MeasurementService(measurementRepository, deviceRepository);
		int queues = 2;
		for(int i = 0 ;i < queues; i++){
			Worker worker = new Worker(service);
			worker.start();
		}
	}

}
