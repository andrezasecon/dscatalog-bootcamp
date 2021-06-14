package com.andrezasecon.app.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andrezasecon.app.dto.RoleDTO;
import com.andrezasecon.app.dto.UserDTO;
import com.andrezasecon.app.dto.UserInsertDTO;
import com.andrezasecon.app.entities.Role;
import com.andrezasecon.app.entities.User;
import com.andrezasecon.app.repositories.RoleRepository;
import com.andrezasecon.app.repositories.UserRepository;
import com.andrezasecon.app.services.exceptions.DataBaseException;
import com.andrezasecon.app.services.exceptions.ResourceNotFoundException;

@Service
public class UserService {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    RoleRepository roleRepository;
   
    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(Pageable pageable) {
        Page<User> list = userRepository.findAll(pageable);
        return list.map(x -> new UserDTO(x));
    }

    @Transactional(readOnly = true)
    public UserDTO findUserById(Long id) {
        Optional<User> obj = userRepository.findById(id);
        User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO insertUser(UserInsertDTO dto) {
        User entity = new User();
        copyDtoToEntity(dto, entity);
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity = userRepository.save(entity);
        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO dto) {
        try {
            User entity = userRepository.getOne(id);
            copyDtoToEntity(dto, entity);
            entity = userRepository.save(entity);
            return new UserDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id " + id + " not found");
        }
    }

    public void deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id " + id + " not found");
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Integrity violation");
        }
    }

    private void copyDtoToEntity(UserDTO dto, User entity) {
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.getRoles().clear();
        
        for(RoleDTO roleDto : dto.getRoles()){
           Role role = roleRepository.getOne(roleDto.getId());
            entity.getRoles().add(role);
        }

    }
}
