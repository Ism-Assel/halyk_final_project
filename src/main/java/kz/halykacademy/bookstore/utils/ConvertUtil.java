package kz.halykacademy.bookstore.utils;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

// todo пока не работает
public class ConvertUtil {
    @Autowired
    private static ModelMapper modelMapper;

    public static <T> T convertTo(Object from, Class<T> aClass) {
        return modelMapper.map(from, aClass);
    }
}
