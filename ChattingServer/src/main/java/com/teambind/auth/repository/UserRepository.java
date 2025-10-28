package com.teambind.auth.repository;

import com.teambind.auth.dto.projection.UsernameProjection;
import com.teambind.auth.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByUsername(@NonNull String username);
	Optional<UsernameProjection> findByUserId(@NonNull Long userId);
	Optional<UserEntity> findByConnectionInviteCode(@NonNull String connectionInviteCode);
	
}
