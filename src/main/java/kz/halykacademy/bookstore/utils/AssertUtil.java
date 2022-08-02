package kz.halykacademy.bookstore.utils;

import com.sun.istack.NotNull;
import kz.halykacademy.bookstore.errors.ClientBadRequestException;
import org.springframework.lang.Nullable;

public abstract class AssertUtil {

    public static void notNull(@Nullable String object, @NotNull String message) {
        if (object == null || object.equals("")) {
            throw new ClientBadRequestException(message);
        }
    }

    public static void notNull(@Nullable Object object, @NotNull String message) {
        if (object == null) {
            throw new ClientBadRequestException(message);
        }
    }
}
