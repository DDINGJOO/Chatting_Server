package com.teambind.service

import com.teambind.auth.dto.InviteCode
import com.teambind.auth.dto.User
import com.teambind.auth.dto.projection.UserConnectionStatusProjection
import com.teambind.auth.entity.UserId
import com.teambind.auth.repository.UserConnectionRepository
import com.teambind.chattingserver.service.UserConnectionService
import com.teambind.chattingserver.service.UserService
import com.teambind.constant.UserConnectionStatus
import org.springframework.data.util.Pair
import spock.lang.Specification

class UserConnectionServiceSpec extends Specification {
    UserConnectionService userConnectionService
    UserService userService = Stub()
    UserConnectionRepository userConnectionRepository = Stub()


    def setup() {
        userConnectionService = new UserConnectionService(userService, userConnectionRepository)
    }

    def "사용자 연결 신청에 대한 테스트."() {
        given:
        userService.getUser(inviteCodeOfTargetUser) >> Optional.of(new User(targetUserId, targetUsername))
        userService.getUsername(senderUserId) >> Optional.of(senderUsername)
        userConnectionRepository.findByPartnerUserAIdAndPartnerUserBId(_ as Long , _ as Long) >> {
            Optional.of(
                    Stub(UserConnectionStatusProjection){
                        getStatus() >> beforeConnectionStatus.name()
            })
        }


        when:
        def result = userConnectionService.invite(senderUserId, usedInviteCode)

        then:
        result == expectedResult


        where:
        scenario            | senderUserId  | senderUsername | targetUserId  | targetUsername | inviteCodeOfTargetUser      | usedInviteCode              | beforeConnectionStatus    | expectedResult
        'Valid invite code' | new UserId(1) | 'userA'        | new UserId(2) | 'userB'        | new InviteCode('user2Code') | new InviteCode('user2Code') | UserConnectionStatus.NONE | Pair.of(Optional.of(new UserId(2)), 'userA')
        'Already connected ' | new UserId(1) | 'userA'        | new UserId(2) | 'userB'        | new InviteCode('user2Code') | new InviteCode('user2Code') | UserConnectionStatus.ACCEPTED | Pair.of(Optional.of(new UserId(2)),  "already accepted to " + targetUsername)
        'Already invited ' | new UserId(1) | 'userA'        | new UserId(2) | 'userB'        | new InviteCode('user2Code') | new InviteCode('user2Code') | UserConnectionStatus.PENDING | Pair.of(Optional.of(new UserId(2)),  "already invited to " + targetUsername)
    }
}
