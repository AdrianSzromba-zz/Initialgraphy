package pl.adrianszromba.app.Initialgraphy.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.adrianszromba.app.Initialgraphy.appl.dto.UserDto;
import pl.adrianszromba.app.Initialgraphy.appl.repository.UserRepository;
import pl.adrianszromba.app.Initialgraphy.appl.utils.Utils;
import pl.adrianszromba.app.Initialgraphy.io.entity.UserEntity;
import pl.adrianszromba.app.Initialgraphy.service.UserService;

import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final Utils utils;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, Utils utils, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.utils = utils;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDto createUser(UserDto user) {

        if(userRepository.findUserByEmail(user.getEmail()) != null)
            throw new RuntimeException("Record Already exists");

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);

        String publicUserId = utils.generateUserId(30);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userEntity.setUserId(publicUserId);

        UserEntity storedUserDetails = userRepository.save(userEntity);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(storedUserDetails, returnValue);

        return returnValue;
    }

	@Override
	public UserDto getUser(String email) {
    	UserEntity userEntity = userRepository.findUserByEmail(email);
		if(userEntity == null) throw new UsernameNotFoundException(email);
    	UserDto returnValue = new UserDto();
    	BeanUtils.copyProperties(userEntity, returnValue);
		return returnValue;
	}

	@Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findUserByEmail(email);
        if(userEntity == null) throw new UsernameNotFoundException(email);

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }
}
