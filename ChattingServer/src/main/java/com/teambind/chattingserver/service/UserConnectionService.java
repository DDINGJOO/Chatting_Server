package com.teambind.chattingserver.service;

import com.teambind.auth.dto.InviteCode;
import com.teambind.auth.dto.User;
import com.teambind.auth.entity.UserConnectionEntity;
import com.teambind.auth.entity.UserId;
import com.teambind.auth.repository.UserConnectionRepository;
import com.teambind.constant.UserConnectionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserConnectionService {
	private final UserService userService;
	private final UserConnectionRepository userConnectionRepository;
	Logger log = LoggerFactory.getLogger(UserConnectionService.class);
	
	public UserConnectionService(UserService userService, UserConnectionRepository userConnectionRepository) {
		this.userService = userService;
		this.userConnectionRepository = userConnectionRepository;
	}
	
	public Pair<Optional<UserId>, String> invite(UserId invitoerUserId, InviteCode inviteCode) {
		Optional<User> partner = userService.getUser(inviteCode);
		if (partner.isEmpty()) {
			log.info("invalid invite code. {} from {}", inviteCode, invitoerUserId);
			return Pair.of(Optional.empty(), "invalid invite code");
		}
		
		UserId partnerUserId = partner.get().userId();
		String partnerUserName = partner.get().userName();
		
		if (partnerUserId.equals(invitoerUserId)) {
			log.info("cannot invite self. {} from {}", inviteCode, invitoerUserId);
			return Pair.of(Optional.empty(), "cannot invite self");
		}
		
		UserConnectionStatus status = getStatus(invitoerUserId, partnerUserId);
		return switch (status) {
			case NONE, DISCONNECTED -> {
				Optional<String> invitorUsername = userService.getUsername(invitoerUserId);
				if(invitorUsername.isEmpty()) {
					log.warn("invitor username is empty. {}", invitoerUserId);
					yield Pair.of(Optional.empty(), "invitor username is empty");
				}
				try{
					setStatus(invitoerUserId, partnerUserId, UserConnectionStatus.PENDING);
					yield Pair.of(Optional.of(partnerUserId), "invited");
				}catch (Exception e) {
					log.error("setStatus error : {}", e.getMessage());
					yield Pair.of(Optional.empty(), "setStatus error");
				}
			}
			case ACCEPTED -> Pair.of(Optional.empty(), "already accepted with " + partnerUserName);
			case PENDING, REJECTED -> {
				log.info("{} invites {} but dose not deliver inviation request", invitoerUserId, partnerUserId);
				yield Pair.of(Optional.empty(), "already invited");
			}
			default -> {
				log.error("unknown status : {}", status);
				yield Pair.of(Optional.empty(), "unknown status");
			}
		};
	}
	
	private UserConnectionStatus getStatus(UserId invitorUserId, UserId partnerUserId) {
		return userConnectionRepository.findByPartnerUserAIdAndPartnerUserBId(
				Long.min(invitorUserId.id(), partnerUserId.id()),
				Long.max(invitorUserId.id(), partnerUserId.id())).map(status -> UserConnectionStatus.valueOf(status.getStatus())).orElse(
				UserConnectionStatus.NONE
		);
	}
	
	
	@Transactional
	protected void setStatus(UserId invitorUserId, UserId partnerUserId, UserConnectionStatus status) {
		if (status == UserConnectionStatus.ACCEPTED) {
			throw new IllegalArgumentException("Can't set to accepted");
		}
		userConnectionRepository.save(new UserConnectionEntity(
				Long.min(invitorUserId.id(), partnerUserId.id()),
				Long.max(invitorUserId.id(), partnerUserId.id()),
				status, invitorUserId.id()));
	}
}
