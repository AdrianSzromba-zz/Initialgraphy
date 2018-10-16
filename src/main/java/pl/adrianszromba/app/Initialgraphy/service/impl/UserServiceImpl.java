package pl.adrianszromba.app.Initialgraphy.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.adrianszromba.app.Initialgraphy.appl.dto.UserDto;
import pl.adrianszromba.app.Initialgraphy.appl.repository.UserRepository;
import pl.adrianszromba.app.Initialgraphy.appl.utils.Utils;
import pl.adrianszromba.app.Initialgraphy.io.entity.UserEntity;
import pl.adrianszromba.app.Initialgraphy.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Utils utils;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, Utils utils) {
        this.userRepository = userRepository;
        this.utils = utils;
    }

    @Override
    public UserDto createUser(UserDto user) {

        if(userRepository.findUserByEmail(user.getEmail()) != null)
            throw new RuntimeException("Record Already exists");

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);

        String publicUserId = utils.generateUserId(30);
        userEntity.setEncryptedPassword("test");
        userEntity.setUserId(publicUserId);

        UserEntity storedUserDetails = userRepository.save(userEntity);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(storedUserDetails, returnValue);

        return returnValue;
    }
}
