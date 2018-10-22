package pl.adrianszromba.app.Initialgraphy.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import pl.adrianszromba.app.Initialgraphy.appl.dto.UserDto;

public interface UserService extends UserDetailsService {
    UserDto createUser (UserDto user);
}
