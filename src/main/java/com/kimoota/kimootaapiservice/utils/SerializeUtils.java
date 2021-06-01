package com.kimoota.kimootaapiservice.utils;

import org.springframework.util.SerializationUtils;

import java.util.Base64;

public class SerializeUtils {

    public static String serialize(Object o) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(o));
    }

    public static <T>T deserialize(String raw, Class<T> cls) {
        return cls.cast(
                SerializationUtils.deserialize(
                        Base64.getDecoder().decode(raw)));
    }
}
