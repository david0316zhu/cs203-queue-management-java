package com.ticketmasterdemo.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ticketmasterdemo.demo.common.exception.EventRegisterException;
import com.ticketmasterdemo.demo.common.exception.InvalidArgsException;
import com.ticketmasterdemo.demo.common.exception.UserException;
import com.ticketmasterdemo.demo.dto.AddMember;
import com.ticketmasterdemo.demo.dto.Registration;
import com.ticketmasterdemo.demo.dto.User;
import com.ticketmasterdemo.demo.dto.UserInfo;
import com.ticketmasterdemo.demo.dto.RegistrationInfo;
import com.ticketmasterdemo.demo.dto.Seat;
import com.ticketmasterdemo.demo.dto.SeatCategorySelection;
import com.ticketmasterdemo.demo.dto.Show;
import com.ticketmasterdemo.demo.dto.Queue;
import com.ticketmasterdemo.demo.repository.EventRegisterRepository;
import com.ticketmasterdemo.demo.repository.EventRepository;
import com.ticketmasterdemo.demo.repository.SeatRepository;
import com.ticketmasterdemo.demo.repository.UserRepository;
import com.ticketmasterdemo.demo.service.EventRegisterService;
import com.ticketmasterdemo.demo.service.enums.ValStatus;
import com.ticketmasterdemo.demo.util.SeatAlgorithm;
import com.ticketmasterdemo.demo.util.Utility;

import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EventRegisterServiceImpl implements EventRegisterService {

    private final EventRegisterRepository eventRegisterRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final SeatRepository seatRepository;
    private final EventServiceImpl eventService;

    @Autowired
    public EventRegisterServiceImpl(UserRepository userRepository, EventRepository eventRepository,
            EventRegisterRepository eventRegisterRepository, SeatRepository seatRepository,
            EventServiceImpl eventService) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.eventRegisterRepository = eventRegisterRepository;
        this.seatRepository = seatRepository;
        this.eventService = eventService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Registration registerGroup(Registration form) throws UserException {
        log.info("Begin registerGroup Service method ");
        List<User> userList = form.getUserGroup();
        List<User> responseUserList = new ArrayList<>();

        responseUserList = this.verifyUser(userList);
        form.setUserGroup(responseUserList);

        Utility util = new Utility();
        String groupId = util.generateRandomUUID();
        log.info("Generate Group ID: " + groupId);
        form.setId(groupId);

        User groupLeader = userRepository.findVerifiedUserByEmail(form.getGroupLeaderEmail());
        form.setGroupLeaderId(groupLeader.getId());
        try {
            log.info(form.getGroupLeaderId());
            eventRegisterRepository.registerGroup(form);
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                // Handle the specific duplicate key scenario
                throw new UserException("User is already a group leader of another Group of the same event!");
            } else {
                throw new UserException("Group Insertion Error");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new UserException("Group registration error!");
        }
        log.info("Group Registration Success");

        try {
            this.registerUsers(responseUserList, groupId, form.getEventId(), form.getGroupLeaderEmail());
            log.info("Group Users Registration Success");
        } catch (Exception e) {
            throw new UserException(e.getMessage());
        }
        return form; // Registration is successful

    }

    @Override
    public Boolean registerUsers(List<User> userList, String groupId, String eventId, String groupLeaderEmail) {
        Utility utility = new Utility();
        if (!utility.emailWhitelist(groupLeaderEmail) || !utility.isInputSafe(eventId) || !utility.isInputSafe(groupId)) {
            throw new InvalidArgsException("Invalid request");
        }
        try {
            for (User user : userList) {
                boolean isLeaderBool = user.getEmail().equals(groupLeaderEmail);
                int isLeader = isLeaderBool ? 1 : 0;
                eventRegisterRepository.registerUser(user, groupId, eventId, isLeader);
            }
            return true; // User registration is successful
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                // Handle the specific duplicate key scenario
                throw new UserException("User is already in another Group of the same event!");
            } else {
                throw new UserException("Group Users Registration Insertion Error");
            }
        } catch (Exception e) {
            throw new UserException("Group Users Registration Error");
        }
    }

    @Override
    public Boolean checkGroupRegistrationStatus(String groupId, String eventId) {
        Utility utility = new Utility();
        if (!utility.isInputSafe(eventId) || !utility.isInputSafe(groupId)) {
            throw new InvalidArgsException("Invalid request");
        }
        try {
            Boolean status = eventRegisterRepository.checkGroupStatus(groupId, eventId);

            if (status == null) {
                throw new EventRegisterException("Group ID / Event ID does not exist");
            }
            return status;
        } catch (EventRegisterException e) {
            throw new EventRegisterException(e.getMessage());
        } catch (Exception e) {
            // Handle any unexpected exceptions here
            e.printStackTrace(); // Log the exception for debugging
            throw new EventRegisterException("Unable to retrieve Group Data");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateEventGroupUserConfirmation(String userId, String eventId, String groupId) {
        Utility utility = new Utility();
        if (!utility.isInputSafe(userId) || !utility.isInputSafe(eventId) || !utility.isInputSafe(groupId)) {
            throw new InvalidArgsException("Invalid request");
        }
        try {
            eventRegisterRepository.updateUserStatus(groupId, eventId, userId);
            Boolean status = eventRegisterRepository.checkUserStatus(groupId, eventId, userId);
            if (status == null) {
                throw new EventRegisterException("User ID/Group ID/Event ID does not match");
            }
            return status;
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
            throw new EventRegisterException("Unable to update user confirmation Data");
        }
    }

    @Override
    public RegistrationInfo getRegistrationGroupInfo(String userId, String eventId) {
        Utility utility = new Utility();
        if (!utility.isInputSafe(eventId) || !utility.isInputSafe(userId)) {
            throw new InvalidArgsException("Invalid request");
        }
        try {
            String groupId = eventRegisterRepository.getRegistrationGroupId(userId, eventId);
            if (groupId == null) {
                throw new UserException("User ID / Event ID does not exist");
            }

            List<UserInfo> userInfoList = userRepository.getAllUserInfo(groupId);

            Boolean hasAllUsersConfirmed = eventRegisterRepository.checkGroupStatus(groupId, eventId);

            List<Queue> queueList = eventRepository.retrieveAllQueuesForSpecificGroup(groupId);

            return new RegistrationInfo(groupId, userInfoList, hasAllUsersConfirmed, queueList);
        } catch (UserException e) {
            throw new UserException(e.getMessage());
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new RuntimeException("Unable to get User's registration group information");
        }
    }

    @Override
    public List<ValStatus> validateGroup(List<String> emailList, List<String> mobileList, String eventId) {
        if (emailList == null || mobileList == null || emailList.size() != mobileList.size()) {
            throw new InvalidArgsException("email list or mobile list provided is invalid.");
        }
        List<ValStatus> output = new ArrayList<>();
        Iterator<String> emailIter = emailList.iterator(), mobileIter = mobileList.iterator();
        while (emailIter.hasNext() && mobileIter.hasNext()) {
            output.add(determineUserElligibility(emailIter.next(), mobileIter.next(), eventId));
        }
        return output;
    }

    private ValStatus determineUserElligibility(String email, String mobile, String eventId){
        Utility utility = new Utility();
        if (!utility.emailWhitelist(email) || !utility.isInputSafe(mobile) || !utility.isInputSafe(eventId)) {
            throw new InvalidArgsException("Invalid request");
        }
        User user = userRepository.findUserByEmailAndMobile(email, mobile);
        if (user == null) return ValStatus.USER_DOES_NOT_EXIST;

        if (!user.isVerified()) return ValStatus.USER_NOT_VERIFIED;

        // Searches the event
        int groupCount = eventRegisterRepository.userGroupForEventCount(user.getId(), eventId);

        if(groupCount > 0) return ValStatus.USER_IN_OTHER_GROUP;
        
        return ValStatus.VALID_MEMBER;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean addUsersToGroup(AddMember form) {
        if (!eventRegisterRepository.isGroupLeader(form.getGroupId(), form.getUserId())) {
            throw new EventRegisterException("User ID is not a group leader!");
        }
        List<User> verifiedUsers = verifyUser(form.getUserGroup());
        registerUsers(verifiedUsers, form.getGroupId(), form.getEventId(), form.getUserId());
        return true;
    }

    public List<User> verifyUser(List<User> userList) {
        List<User> verifiedUserList = new ArrayList<>();
        for (User user : userList) {
            try {
                User verifiedUser = userRepository.findUserByEmailAndMobile(user.getEmail(), user.getMobile());
                if (verifiedUser == null) {
                    throw new UserException("Email(s) in group are not registered as users");
                }

                if (verifiedUser != null && !verifiedUser.isVerified()) {
                    throw new UserException("User(s) in group are not verified"); // User is not authenticated
                }
                verifiedUserList.add(verifiedUser);
            } catch (UserException e) {
                throw new UserException(e.getMessage());
            } catch (Exception e) {
                throw new UserException("User Error!");
            }
        }
        return verifiedUserList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveSeatCategorySelectionForGroup(SeatCategorySelection form) {

        String userId = form.getUserId();
        String groupId = form.getGroupId();
        String eventId = form.getEventId();
        String showId = form.getShowId();

        Utility utility = new Utility();
        if (!utility.isInputSafe(userId) || !utility.isInputSafe(groupId) || !utility.isInputSafe(eventId) || !utility.isInputSafe(showId)) {
            throw new InvalidArgsException("Invalid Request");
        }

        if (!checkGroupRegistrationStatus(groupId, eventId)) {
            throw new EventRegisterException("Group is not registered for that event");
        }

        RegistrationInfo groupRegistrationInfo = getRegistrationGroupInfo(userId, eventId);
        if (groupRegistrationInfo == null) {
            throw new UserException("Invalid User ID / Event ID");
        }
        
        if (!eventRegisterRepository.isGroupLeader(groupId, userId)) {
            throw new EventRegisterException("User is not the group leader for that group");
        }

        if (!eventService.isValidShowForEvent(eventId, showId)) {
            throw new EventRegisterException("Invalid Show ID / Event ID");
        }
    
        List<UserInfo> userInfoList = groupRegistrationInfo.getUserInfoList();
        int groupSize = userInfoList.size();

        List<Seat> seatsInCategory = seatRepository.getSeatsInCategoryForSpecificShow(eventId, showId, form.getCategoryId());
        if (seatsInCategory == null || seatsInCategory.size() == 0) {
            throw new EventRegisterException("There are no seats available in that category for that show");
        }
        
        SeatAlgorithm seatAlgorithm = new SeatAlgorithm();

        List<Seat> seatAllocation = seatAlgorithm.getSeatAllocation(seatsInCategory, groupSize);
        if (seatAllocation == null) {
            throw new EventRegisterException("There are not enough seats in that category for that group size");
        }
        
        for (int i = 0; i < groupSize; i++) {
            int seatIdAllocation = seatAllocation.get(i).getSeatId();
            String userIdAllocation = userInfoList.get(i).getId();

            if (seatRepository.userHasSeatForSpecificShow(eventId, showId, userIdAllocation)) {
                throw new EventRegisterException("User is already allocated a seat");
            }
            seatRepository.saveSeatCategorySelection(eventId, showId, form.getCategoryId(), seatIdAllocation, userIdAllocation);
        }

        return true;
    }
}
