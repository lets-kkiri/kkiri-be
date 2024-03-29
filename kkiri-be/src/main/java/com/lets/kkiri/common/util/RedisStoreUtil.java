package com.lets.kkiri.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisStoreUtil {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    /**
     * redis에 데이터를 저장한다.
     * @param key
     * @param data
     * @param <T>
     * @return
     */
    public <T> boolean saveData(String key, T data) {
        try {
            String value = objectMapper.writeValueAsString(data);
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch(Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * redis에 저장된 데이터를 가져온다.
     * @param key
     * @param classType
     * @param <T>
     * @return
     */
    public <T> Optional<T> getData(String key, Class<T> classType) {
        String value = redisTemplate.opsForValue().get(key);

        if(value == null){
            return Optional.empty();
        }

        try {
            return Optional.of(objectMapper.readValue(value, classType));
        } catch(Exception e){
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * redis에 저장된 moimId에 해당하는 모든 세션 데이터를 가져온다.
     * @param moimId 모임 아이디, key pattern으로 사용
     * @param classType 가져올 데이터 타입 (ex. WebSocketSession.class)
     * @return 가져온 데이터 리스트
     */
    public <T> ArrayList<T> getAllSessionsByMoimId(Long moimId, Class<T> classType) {
        ArrayList<T> values = new ArrayList<>();

        ScanOptions scanOptions = ScanOptions.scanOptions().match(moimId+"-*").count(10).build();
        Cursor<byte[]> keys = Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().scan(scanOptions);

        while (keys.hasNext()) {
            String key = new String(keys.next());
            String value = redisTemplate.opsForValue().get(key);
            try {
                values.add(objectMapper.readValue(value, classType));
            } catch(Exception e){
                log.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
                // TODO: 2023-05-11 에러처리
            }
        }
        return values;
    }
/*
    public <T> boolean updateData(String key, T data) {
        try {
            String value = objectMapper.writeValueAsString(data);
            redisTemplate.opsForValue().append(key, value);
            return true;
        } catch(Exception e){
            log.error(e.getMessage());
            return false;
        }
    }
 */
}
