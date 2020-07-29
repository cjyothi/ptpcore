package com.dms.ptp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dms.ptp.entity.User;
import com.dms.ptp.exception.InvalidLoginCredentialsException;
import com.dms.ptp.exception.UserNotFoundException;
import com.dms.ptp.entity.Role;
import com.dms.ptp.dto.SignUpRequestModel;
import com.dms.ptp.dto.UserLoginRequest;
import com.dms.ptp.response.UserLoginResponse;

import reactor.core.publisher.Flux;

public interface UserService {

	public String signUp(SignUpRequestModel signUpRequest);
	public String userApproval(SignUpRequestModel signUpRequest, String flag);
	public UserLoginResponse signIn(UserLoginRequest signInRequest) throws InvalidLoginCredentialsException;
	public UserLoginResponse verifyotp( int userid,int otp, UserLoginRequest signInRequest);
	public Flux<String> getAgencyList();
	public Flux<String> getAdvertisersForAgency(String agencyCode);

	public Page<User> getUserInfo(int status, Pageable pageable) throws UserNotFoundException;
    public Page<User> getAllUsers(Pageable pageable);
	public Optional<User> getUserInfoById(int id);
	public String editUserDetails(SignUpRequestModel signUpRequest, int status, String token);
	public List<Role> getRoleList();
}
