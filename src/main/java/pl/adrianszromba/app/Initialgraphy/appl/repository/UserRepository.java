package pl.adrianszromba.app.Initialgraphy.appl.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.adrianszromba.app.Initialgraphy.io.entity.UserEntity;

@Repository
public interface UserRepository extends CrudRepository {
    UserEntity findUserByEmail(String email);
}
