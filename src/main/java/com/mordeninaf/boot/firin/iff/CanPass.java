package com.mordeninaf.boot.firin.iff;


import com.mordeninaf.boot.firin.model.Yetki;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface CanPass {
    Yetki[] value() default Yetki.Read;
}
