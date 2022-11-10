package MunteanuCezar.SD1.repositories;

import MunteanuCezar.SD1.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Role findByRoleCode(String roleCode);
}
