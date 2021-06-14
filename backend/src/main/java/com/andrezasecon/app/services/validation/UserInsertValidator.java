package com.andrezasecon.app.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.andrezasecon.app.dto.UserInsertDTO;
import com.andrezasecon.app.entities.User;
import com.andrezasecon.app.repositories.UserRepository;
import com.andrezasecon.app.resouces.exceptions.FieldMessage;
 
public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {
	
	@Autowired
	private UserRepository UserRepository;
	
	@Override
	public void initialize(UserInsertValid ann) {
	}

	@Override
	public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {
		
		// Coloque aqui seus testes de validação, acrescentando objetos FieldMessage à lista
		
		List<FieldMessage> list = new ArrayList<>();
		
		User user = UserRepository.findByEmail(dto.getEmail());
		if(user != null) {
			list.add(new FieldMessage("email", "Email já cadastrado!"));
		}
			
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}