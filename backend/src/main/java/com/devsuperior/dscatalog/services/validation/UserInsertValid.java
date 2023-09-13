package com.devsuperior.dscatalog.services.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
//define uma anotation que será implementada pela classe UserInsertValidator e usada na classe UserInsertDTO para que seja verificado no banco de
// dados se o email inserido já está sendo utilizado por algum usuário
@Constraint(validatedBy = UserInsertValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserInsertValid {
    String message()default "Validation Error";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
