package com.dms.ptp.serviceimplementation;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.dms.ptp.config.AWSDynamoConfig;
import com.dms.ptp.dto.JWTExtract;
import com.dms.ptp.dto.SignUpRequestModel;
import com.dms.ptp.dto.UserLoginRequest;
import com.dms.ptp.entity.Role;
import com.dms.ptp.entity.User;
import com.dms.ptp.exception.InvalidLoginCredentialsException;
import com.dms.ptp.exception.InvalidParameterException;
import com.dms.ptp.exception.InvalidPasswordException;
import com.dms.ptp.exception.UserNameExistsException;
import com.dms.ptp.exception.UserNotFoundException;
import com.dms.ptp.repository.RoleRepository;
import com.dms.ptp.repository.UserRepository;
import com.dms.ptp.response.UserApprovalResponse;
import com.dms.ptp.response.UserLoginResponse;
import com.dms.ptp.service.UserService;
import com.dms.ptp.util.Constant;
import com.dms.ptp.util.EmailUtil;
import com.dms.ptp.util.JWTTokenUtil;

import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepo;

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    WebClient webClientForUser;

    @Autowired
    EmailUtil emailUtil;

    @Autowired
    AWSDynamoConfig dynamoConfig;

    @Autowired
    Environment env;

    @Autowired
    JWTTokenUtil jwtUtil;

    static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public List<Role> getRoleList() {
        logger.info("Inside UserServiceImpl: getRoleList");
        List<Role> roleList = roleRepo.findAll();
        return roleList;
    }

    public String signUp(SignUpRequestModel signUpRequest) {

        logger.info("Inside UserServiceImplImpl: signUp");
        String sha1 = "";

        // signUpRequest validation for Username, Password, PhoneNum
        userRequestValidation(signUpRequest);

        sha1 = passwordEncryption(signUpRequest, sha1);

        // save to ptp DB User table
        User newUser = new User();
        newUser.setFirstName(signUpRequest.getFirstName());
        newUser.setLastName(signUpRequest.getLastName());
        newUser.setPassword(sha1);
        newUser.setEmail(signUpRequest.getUsername()); // TBD
        newUser.setCompany(signUpRequest.getCompany());
        newUser.setPhone(signUpRequest.getPhone());
        newUser.setDesignation(signUpRequest.getDesignation());
        newUser.setRegion(signUpRequest.getRegion());
        newUser.setStatus(0);
        newUser.setAgencyCode(signUpRequest.getAgencyCode());
        newUser.setAgencyName(signUpRequest.getAgencyName());
        newUser.setRole(signUpRequest.getRole());
        logger.info("saving user details: " + newUser);

        int respStatus = 0;
        String message = "";
        // notify the Admin & User
        try {
            emailUtil.sendMailForSignUp(signUpRequest.getUsername());
            emailUtil.sendMailToAdmin(signUpRequest.getEmail(), 0);
            userRepo.save(newUser);
            respStatus = 200;
            message = "Email sent to the Admin & User"; // return
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        String response = message;
        return response;
    }

    private String passwordEncryption(SignUpRequestModel signUpRequest, String sha1) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(signUpRequest.getPassword().getBytes("utf8"));
            sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
        } catch (NoSuchAlgorithmException e) {
            e.getMessage();
        } catch (UnsupportedEncodingException e) {
            e.getMessage();
        }
        return sha1;
    }

    @Override
    public String editUserDetails(SignUpRequestModel signUpRequest, int status, String token) {
    	log.info("Inside UserServiceImplImpl: editUserDetails");

    	/**status = 0: New user, 1: Active user**/

    	User user = userRepo.findByEmail(signUpRequest.getEmail());
    	JWTExtract jwtExtract = jwtUtil.getIdRoleFromToken(token);
    	String message = "";

    	if (jwtExtract.getUserRole().equals(Constant.ROLE_AGENCY_ADMIN)
    			|| jwtExtract.getUserRole().equals(Constant.ROLE_DMS_ADMIN) || 
    			jwtExtract.getUserRole().equals(Constant.ROLE_ADMIN)) {
    		/** edit user details by Admin for new & active users **/
    		if (status == 0) {
    			user.setPhone(signUpRequest.getPhone());
    			user.setDesignation(signUpRequest.getDesignation());
    			user.setRegion(signUpRequest.getRegion());
    			user.setAgencyCode(signUpRequest.getAgencyCode());
    			user.setAgencyName(signUpRequest.getAgencyName());
    		} else {
    			user.setPhone(signUpRequest.getPhone());
    			user.setJobRole(signUpRequest.getJobRole());
    			user.setAgencyCode(signUpRequest.getAgencyCode());
    			user.setAgencyName(signUpRequest.getAgencyName());
    		}
    	} else if ((!(jwtExtract.getUserRole().equals(Constant.ROLE_AGENCY_ADMIN)
    			|| jwtExtract.getUserRole().equals(Constant.ROLE_DMS_ADMIN) || 
    			jwtExtract.getUserRole().equals(Constant.ROLE_ADMIN))) && (status == 1)) {
    		/** edit personal details by active users **/
    		user.setFirstName(signUpRequest.getFirstName());
    		user.setLastName(signUpRequest.getLastName());
    		user.setPhone(signUpRequest.getPhone());
    		user.setDesignation(signUpRequest.getDesignation());
    	}

		userRepo.save(user);
		message = Constant.USER_DETAILS_MSG; 
		return message;
	}
    
    @Override
	public UserApprovalResponse editUserStatus(SignUpRequestModel signUpRequest, String action, String token) {
		log.info("Inside UserServiceImplImpl: editUserStatus");

		UserApprovalResponse response = new UserApprovalResponse();
		/** status = 0: New user, 1: Active user, 3: Suspended user **/

		User user = userRepo.findByEmail(signUpRequest.getEmail());
		JWTExtract jwtExtract = jwtUtil.getIdRoleFromToken(token);
		String message = "";

		try {
			if (jwtExtract.getUserRole().equals(Constant.ROLE_AGENCY_ADMIN)
					|| jwtExtract.getUserRole().equals(Constant.ROLE_DMS_ADMIN) 
					|| jwtExtract.getUserRole().equals(Constant.ROLE_ADMIN)) {

				if (user.getStatus() == 1) {
					if (action.equalsIgnoreCase("s")) {
						user.setStatus(3);
						message = "account suspended for " + user.getEmail();
						response.setMessage(message);
						response.setRespFlag(true);
						response.setStatus(HttpStatus.OK);
					}
				} else if (user.getStatus() == 3) {
					if (action.equalsIgnoreCase("a")) {
						user.setStatus(1);
						message = "account activated for " + user.getEmail();
						response.setMessage(message);
						response.setRespFlag(true);
						response.setStatus(HttpStatus.OK);
					}
				}
				userRepo.save(user);
			} else {
				message = "Request cannot be processed. Not an admin user.";
				response.setMessage(message);
				response.setRespFlag(false);
				response.setStatus(HttpStatus.BAD_GATEWAY);
			}
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;
	}

    public String userApproval(SignUpRequestModel signUpRequest, String flag) {
        String message = "";
        User user = userRepo.findByEmail(signUpRequest.getUsername());
        logger.info(signUpRequest.getUsername());
        try {

            if (flag.equals("Y")) {
                user.setStatus(1); // status: active
                user.setJobRole(signUpRequest.getJobRole());
                user.setUserType(signUpRequest.getUserType());
                userRepo.save(user);

                emailUtil.sendMail(signUpRequest.getUsername(), flag, null);
                message = Constant.USER_APPROVAL_ACCEPT;

            } else {
                user.setStatus(2); // status: rejected
                userRepo.save(user);

                emailUtil.sendMail(signUpRequest.getUsername(), flag, signUpRequest.getReason());
                message = Constant.USER_APPROVAL_REJECT + " Reason: " + signUpRequest.getReason();
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return message;
    }

    public UserLoginResponse signIn(UserLoginRequest signInRequest) throws InvalidLoginCredentialsException {
        UserLoginResponse loginResponse = new UserLoginResponse();
        logger.info("Inside UserServiceImpl: signIn");

        User user1 = userRepo.findByEmail(signInRequest.getUsername());

        /** status-> 0:pending, 1:active, 3:suspended **/
        if (user1.getStatus() == 0) {
            loginResponse.setStatus(HttpStatus.UNAUTHORIZED);
            loginResponse.setMessage("Sorry! Cannot login. User id: " + user1.getEmail() + " is not approved.");
        } else if (user1.getStatus() == 2) {
    		loginResponse.setStatus(HttpStatus.UNAUTHORIZED);
    		loginResponse.setMessage("Sorry! Cannot Login. User id: " + user1.getEmail() + " is rejected.");
    	} else if (user1.getStatus() == 3) {
            loginResponse.setStatus(HttpStatus.UNAUTHORIZED);
            loginResponse.setMessage("Sorry! Cannot login. User id: " + user1.getEmail() + " is suspended.");
        } else {
            String sha1 = "";
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-1");
                digest.reset();
                digest.update(signInRequest.getPassword().getBytes("utf8"));
                sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
            } catch (NoSuchAlgorithmException e) {
                e.getMessage();
            } catch (UnsupportedEncodingException e) {
                e.getMessage();
            }

            String password = "";
            if (sha1.equals(user1.getPassword())) {
                password = user1.getPassword();
            }

            final User user;
            if ((user = userRepo.findByEmailAndPassword(signInRequest.getUsername(), password)) != null) {
            	logger.info(user.toString());

            	//TRP-2211 change starts
            	String otp = "";
            	int userId = 0;
            	boolean verify = false;

            	long days = 0;
            	if (user.getLastVerifiedOn() == null) {
            		user.setLastVerifiedOn(LocalDateTime.MIN);
            		days = getDifferenceDays(user.getLastVerifiedOn(), LocalDateTime.now());
            	} else {
            		days = getDifferenceDays(user.getLastVerifiedOn(), LocalDateTime.now());
            	}

            	//long days = getDifferenceDays(user.getLastVerifiedOn(), LocalDateTime.now());

            	if(days <= 30) {
            		verify = false;

            		//generate token
            		String jsonWebToken = jwtUtil.getToken(user);
            		loginResponse.setAccessToken(jsonWebToken);
            		loginResponse.setVerify(verify);
            		loginResponse.setMessage("OTP not required");

            	} else {
            		otp = generateOTP();
            		userId = generateUserId();
            		verify = true;

            		user.setLastVerifiedOn(LocalDateTime.now());
            		userRepo.save(user);

            		// saving the generated otp in DynamoDB
            		boolean isValid = dynamoConfig.updateTable(userId, otp);

            		// send the generated otp to the user via email
            		try {
            			if (isValid) {
            				emailUtil.sendOTPMail(otp, signInRequest.getUsername());
            			}
            		} catch (MessagingException e) {
            			e.getMessage();
            		} catch (IOException e) {
            			e.getMessage();
            		} catch (TemplateException e) {
            			e.getMessage();
            		}

            		loginResponse.setVerify(verify);
            		// for debug. to be removed later
            		loginResponse.setOtp(otp);
            		loginResponse.setUserid(userId);
            		loginResponse.setUserStatus(Constant.PENDING);
            	}
            	//TRP-2211 change ends
            	loginResponse.setStatus(HttpStatus.OK);
            } else {
            	throw new InvalidLoginCredentialsException(Constant.INVALID_LOGIN_CREDENTIALS);
            }
        }
        return loginResponse;
    }
    
    public long getDifferenceDays(LocalDateTime from, LocalDateTime to) {
		Duration duration = Duration.between(from, to);
		log.info("duration: " + duration.toDays());
		return duration.toDays();
	}

    public UserLoginResponse verifyotp(int userid, int otp, UserLoginRequest signInRequest) {
        logger.info("Inside UserServiceImpl: verifyotp");
        UserLoginResponse loginResponse = new UserLoginResponse();

        User user1 = userRepo.findByEmail(signInRequest.getUsername());
        String sha1 = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(signInRequest.getPassword().getBytes("utf8"));
            sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
        } catch (NoSuchAlgorithmException e) {
            e.getMessage();
        } catch (UnsupportedEncodingException e) {
            e.getMessage();
        }
        String password = "";
        if (sha1.equals(user1.getPassword())) {
            password = user1.getPassword();
        }

        // read the OTP from DynamoDB
        boolean isValidOTP = dynamoConfig.readTable(userid, otp);

        if (isValidOTP) {
            final User user = userRepo.findByEmailAndPassword(signInRequest.getUsername(), password);
            String jsonWebToken = jwtUtil.getToken(user);
            JWTExtract jwtExtract = jwtUtil.getIdRoleFromToken(jsonWebToken);
            logger.info("userid: " + jwtExtract.getUserId() + " userRole: " + jwtExtract.getUserRole());

            loginResponse.setAccessToken(jsonWebToken);
            loginResponse.setAgencyCode(user1.getAgencyCode());
            loginResponse.setAgencyName(user1.getAgencyName());
            loginResponse.setStatus(HttpStatus.OK);
            loginResponse.setOtpMessage(Constant.VALID_OTP);
            loginResponse.setMessage(Constant.VALID_LOGIN);
        } else {
            loginResponse.setStatus(HttpStatus.BAD_REQUEST);
            loginResponse.setOtpMessage(Constant.INVALID_OTP);
            loginResponse.setMessage(Constant.INVALID_LOGIN);
        }
        return loginResponse;
    }

    public Flux<String> getAgencyList() {
    	/**LMK url is commented. keeping for reference. Plz do not remove**/
    	// final String url = "http://" + env.getProperty("lmkserver.address") + ":" +
    	// env.getProperty("lmkserver.port") + "/LMKServices/clients";

    	log.info("Inside UserServiceImpl: getAdvertisersForAgency ");
    	Flux<String> response = null;
    	try {
    		final String url = "https://dmsbookingportaluat.multichoice.co.za/LMKServices/clients";
    		response = this.webClientForUser.get().uri(url).exchange()
    				.flatMapMany(clientResponse -> clientResponse.bodyToFlux(String.class));
    	} catch (Exception e) {
    		response.onErrorReturn(e.getMessage());
    		response.log(e.getMessage());
    	}
    	return response;
    }

    public Flux<String> getAdvertisersForAgency(String agencyCode) {
    	/**LMK url is commented. keeping for reference. Plz do not remove**/
    	// final String url = "http://" + env.getProperty("lmkserver.address") + ":" +
    	// env.getProperty("lmkserver.port") + "/CariaServices/agency/" + agencyCode +
    	// "/advertisers";

    	log.info("Inside UserServiceImpl: getProductsForAdvertisers ");
    	Flux<String> response = null;
    	try {
    		final String url = "https://dmsbookingportaluat.multichoice.co.za/CariaServices/agency/" + agencyCode
    				+ "/advertisers";
    		response = this.webClientForUser.get().uri(url).exchange()
    				.flatMapMany(clientResponse -> clientResponse.bodyToFlux(String.class));
    	} catch (Exception e) {
    		response.onErrorReturn(e.getMessage());
    		response.log(e.getMessage());
    	}
    	return response;
    }

    public Page<User> getUserInfo(int status, Pageable pageable) throws UserNotFoundException {
        logger.info("Inside UserServiceImpl: getUserInfo");
        Page<User> userInfo = userRepo.findByStatus(status, pageable);
        if (userInfo.getTotalElements() != 0) {
            return userInfo;
        } else {
            throw new UserNotFoundException(Constant.USER_NOT_FOUND);
        }
    }

    public Optional<User> getUserInfoById(int userid) throws UserNotFoundException {
        logger.info("Inside UserServiceImpl: getUserInfoById");
        logger.info("userid: " + userid);
        Optional<User> userInfo = userRepo.findById(userid);
        if (userInfo.isPresent()) {
            return userInfo;
        } else {
            throw new UserNotFoundException(Constant.USER_NOT_FOUND);
        }
    }

    public Page<User> getAllUsers(Pageable pageable) {
        logger.info("Inside UserServiceImpl: getAllUsers");
        Page<User> userInfoList = userRepo.findAll(pageable);
        return userInfoList;
    }
    
    /**
     * This method return the User list filter by first_name and last_name
     */
    @Override
    public Page<User> getUserDetail(Specification t, Pageable p) {
    	Page<User> campPage = userRepo.findAll(t, p);
    	List<User> campList = campPage.getContent();
    	Page<User> page = new PageImpl(campList, p, 1L);
    	return page;
    }
    
    

    /**
     * User validation for Username: if signUpRequestname already exists, throw
     * custom exception UserNameExistsException Password: throw custom exception
     * InvalidPasswordException, if it doesn't matches the below format a. Atleast
     * one uppercase, one lowercase, one number and one special character b.Minimum
     * of 8 characters and Maximum of 20 characters Phone Number: throw custom
     * exception InvalidParameterException, if it doesn't match the format +27 011
     * 25975648
     **/
    private boolean userRequestValidation(SignUpRequestModel signUpRequest) {
        logger.info("inside signUpRequest validation");
        boolean flag = false;
        // password
        Pattern pattern = Pattern.compile(Constant.PASSWORD_PATTERN);
        Matcher passwordMatcher = pattern.matcher(signUpRequest.getPassword());

        // phone number
        Pattern p = Pattern.compile(Constant.PHONE_NUM_PATTERN);
        Matcher phoneNumMatcher = p.matcher(signUpRequest.getPhone());

        if (!passwordMatcher.matches()) {
            throw new InvalidPasswordException(Constant.INVALID_PASSWORD);
        }
        if (!phoneNumMatcher.matches()) {
            throw new InvalidParameterException(Constant.INVALID_PARAMATER);
        }

        User resp = userRepo.findByEmail(signUpRequest.getUsername());

        if (resp == null) {
            flag = true;
        } else if (signUpRequest.getUsername().equalsIgnoreCase(resp.getEmail())) {
            throw new UserNameExistsException(Constant.USER_NAME_EXISTS);
        }
        return flag;
    }

    private String generateOTP() {
        // generate 6 digit random Number from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // %6d will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

    private static int generateUserId() {
        Random random = new Random();
        int rand = 0;
        while (true) {
            rand = random.nextInt(11);
            if (rand != 0)
                break;
        }
        System.out.println(rand);
        return rand;
    }

    /*
     * @Bean public WebClient createWebClient() throws SSLException { SslContext
     * sslContext =
     * SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.
     * INSTANCE) .build();
     * 
     * ClientHttpConnector httpConnector = new ReactorClientHttpConnector(opt ->
     * opt.sslContext(sslContext)); return
     * WebClient.builder().clientConnector(httpConnector).build();
     * 
     * 
     * HttpClient httpClient = HttpClient.create().secure(sslContextSpec ->
     * sslContextSpec.sslContext(sslContext)); ClientHttpConnector httpConnector =
     * new ReactorClientHttpConnector(httpClient); return
     * WebClient.builder().clientConnector(httpConnector).build(); }
     */
}
