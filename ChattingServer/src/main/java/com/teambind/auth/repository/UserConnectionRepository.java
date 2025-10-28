package com.teambind.auth.repository;

import com.teambind.auth.dto.projection.InvitorUserIdProjection;
import com.teambind.auth.dto.projection.UserConnectionStatusProjection;
import com.teambind.auth.entity.UserConnectionEntity;
import com.teambind.auth.entity.UserConnectionId;
import com.teambind.constant.UserConnectionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserConnectionRepository extends JpaRepository<UserConnectionEntity, UserConnectionId> {
	Optional<UserConnectionStatusProjection> findByPartnerUserAIdAndPartnerUserBId(
			@NonNull Long partnerUserAId,
			@NonNull Long partnerUserBId);
	
	Optional<UserConnectionEntity> findByPartnerUserAIdAndPartnerUserBIdAndStatus(
			@NonNull Long partnerUserAId,
			@NonNull Long partnerUserBId,
			@NonNull UserConnectionStatus status);
	
	Optional<InvitorUserIdProjection> findInvitorUserIdByPartnerUserAIdAndPartnerUserBId(@NonNull Long partnerUserAId, @NonNull Long partnerUserBId);
	
	
}
