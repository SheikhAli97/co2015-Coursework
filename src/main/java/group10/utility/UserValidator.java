package group10.utility;

import group10.domain.User;
import group10.persistence.UserRepository;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

public class UserValidator implements Validator {

    private UserRepository repository;

    public UserValidator(UserRepository rep){
        this.repository = rep;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User other = (User) target;

        // attempt to find user with same username
        Optional<User> fromDb = repository.findByUsername(other.getUsername());

        if(fromDb.isPresent()){
            // errors
            errors.rejectValue("username","","Username already exists!");
        }
    }
}