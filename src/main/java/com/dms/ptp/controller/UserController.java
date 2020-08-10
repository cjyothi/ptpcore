package com.dms.ptp.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dms.ptp.entity.User;
import com.dms.ptp.exception.InvalidLoginCredentialsException;
import com.dms.ptp.exception.UserNotFoundException;
import com.dms.ptp.util.PageDecorator;
import com.dms.ptp.entity.Role;
import com.dms.ptp.dto.SignUpRequestModel;
import com.dms.ptp.dto.UserList;
import com.dms.ptp.dto.UserLoginRequest;
import com.dms.ptp.response.UserApprovalResponse;
import com.dms.ptp.response.UserLoginResponse;
import com.dms.ptp.service.UserService;
import com.dms.ptp.util.Constant;

import io.github.perplexhub.rsql.RSQLJPASupport;
import io.swagger.annotations.ApiOperation;
import reactor.core.publisher.Flux;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;
	
	@CrossOrigin
    @ApiOperation(value = "REST API to fetch role list")
    @RequestMapping(value = "/roleList", method = RequestMethod.GET)
    public ResponseEntity<List<Role>> getRoleList() {
        log.info("inside UserController: getRoleList");
        return ResponseEntity.ok(userService.getRoleList());
    }

	@CrossOrigin
	@ApiOperation(value = "REST API for User Registration")
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<String> signUp(@RequestBody SignUpRequestModel signUpRequest) {
		log.info("inside UserController: signUp");
		return ResponseEntity.ok(userService.signUp(signUpRequest));
	}
	
	@CrossOrigin
    @ApiOperation(value = "REST API for editing user details by Admin & personal details by active User")
    @RequestMapping(value = "/profile", method = RequestMethod.PUT)
    public ResponseEntity<String> editUserDetails(@RequestBody SignUpRequestModel signUpRequest, 
    		@RequestParam("status") int status, @RequestHeader(name = "Authorization") String token) {
        log.info("inside UserController: signUp");
        return ResponseEntity.ok(userService.editUserDetails(signUpRequest, status, token));
    }
	
	@CrossOrigin
	@ApiOperation(value = "REST API for editing user status by Admin")
	@RequestMapping(value = "/status", method = RequestMethod.PUT)
	public ResponseEntity<UserApprovalResponse> editUserStatus(@RequestBody SignUpRequestModel signUpRequest,
			@RequestParam("action") String action, @RequestHeader(name = "Authorization") String token) {
		log.info("inside UserController: editUserDetails");
		UserApprovalResponse response = userService.editUserStatus(signUpRequest, action, token);
		if(response.isRespFlag() == true) {
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

	@CrossOrigin
	@ApiOperation(value = "REST API for User approval by Admin")
	@RequestMapping(value = "/approve/{flag}", method = RequestMethod.POST)
	public ResponseEntity<String> userApproval(@RequestBody SignUpRequestModel signUpRequest, @PathVariable String flag) {
		log.info("inside UserController: userApproval");
		return ResponseEntity.ok(userService.userApproval(signUpRequest, flag));
	}

	@CrossOrigin
	@ApiOperation(value = "REST API for User Login")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<UserLoginResponse> signIn(@RequestBody UserLoginRequest signInRequest)
			throws InvalidLoginCredentialsException {
		try {
			log.info("inside UserController: signIn");
			UserLoginResponse loginResponse = userService.signIn(signInRequest);
			if(loginResponse != null) {
				return ResponseEntity.status(loginResponse.getStatus()).body(loginResponse);	
			}
		} catch (InvalidLoginCredentialsException e) {
			throw new InvalidLoginCredentialsException(Constant.INVALID_LOGIN_CREDENTIALS);
		}
		return null;
	}

	@CrossOrigin
	@ApiOperation(value = "REST API for Email OTP validation for Login")
	@RequestMapping(value = "{userid}/verifyotp/{otp}", method = RequestMethod.POST)
	public ResponseEntity<UserLoginResponse> verifyotp(@PathVariable int userid, @PathVariable int otp, @RequestBody UserLoginRequest signInRequest) {
		log.info("inside UserController: verifyotp");
		UserLoginResponse loginResponse = userService.verifyotp(userid, otp, signInRequest); 
		return ResponseEntity.status(loginResponse.getStatus()).body(loginResponse);
	}

	@CrossOrigin
	@ApiOperation(value = "REST API to get Agency list from Landmark system")
	@RequestMapping(value = "/agencyList", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<Flux<String>> getAgencyList() {
		log.info("inside UserController: getAgencyList");
		Flux<String> response = userService.getAgencyList();
		return ResponseEntity.ok(response);
	}

	@CrossOrigin
	@ApiOperation(value = "REST API to get Advertisers for an Agency from Landmark system")
	@RequestMapping(value = "{agencyCode}/advertisers", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<Flux<String>> getAdvertisersForAgency(@PathVariable String agencyCode) {
		log.info("inside UserController: getAdvertisersForAgency");
		Flux<String> response = userService.getAdvertisersForAgency(agencyCode);
		return ResponseEntity.ok(response);
	}
	
	@CrossOrigin
    @ApiOperation(value = "REST API to fetch User Info by status")
    @RequestMapping(value = "/{status}", method = RequestMethod.GET)
    public ResponseEntity<PageDecorator<User>> getUserInfo(@PathVariable int status,
                                                           @PageableDefault(sort = "id",direction = Sort.Direction.DESC) Pageable pageable) throws UserNotFoundException {
        log.info("inside UserController: get users");
        try {
            PageDecorator<User> pageDecorator = new PageDecorator<>(userService.getUserInfo(status,pageable));
            return ResponseEntity.ok(pageDecorator);
        } catch (UserNotFoundException ex) {
            log.error("Error getting user", ex.getMessage());
            throw new UserNotFoundException("Invalid status: " + status);
        }
    }
	
	@CrossOrigin
    @ApiOperation(value = "REST API to fetch User Info by userid")
    @RequestMapping(value = "/user/{userid}", method = RequestMethod.GET)
    public ResponseEntity<Optional<User>> getUserInfoById(@PathVariable int userid) throws UserNotFoundException {
        log.info("inside UserController: get users");
        try {
            return ResponseEntity.ok(userService.getUserInfoById(userid));
        } catch (UserNotFoundException ex) {
            log.error("Error getting user: " + ex.getMessage());
            throw new UserNotFoundException("Invalid userid: " + userid);
        }
    }

	@CrossOrigin
    @ApiOperation(value = "REST API to fetch All User Info")
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResponseEntity<PageDecorator<User>> getAllUsers(@PageableDefault(sort = "id",direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("inside UserController: get all users");
        try {
            PageDecorator<User> pageDecorator = new PageDecorator<>(userService.getAllUsers(pageable));
            return ResponseEntity.ok(pageDecorator);
        } catch (Exception ex) {
            log.error("Error getting user {}", ex.getMessage());
        }
        return ResponseEntity.notFound().build();
    }
	
	@CrossOrigin
    @ApiOperation(value = "REST API to filter Campaign")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public PageDecorator<User> getLengthFactor(@RequestParam(name = "name", required = true) String name,
            Pageable pageable, HttpServletResponse response) {
		String filter = "firstName==" +name + "," +"lastName==" +name;
        Page<User> page = userService.getUserDetail(RSQLJPASupport.toSpecification(filter),
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()));
        return new PageDecorator<>(page);
    }
}
