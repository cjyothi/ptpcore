package com.dms.ptp.serviceimplementation;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.net.ssl.SSLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.dms.ptp.config.AWSDynamoConfig;
import com.dms.ptp.controller.AffinityReachCalculator;
import com.dms.ptp.repository.RoleRepository;
import com.dms.ptp.entity.User;
import com.dms.ptp.repository.UserRepository;
import com.dms.ptp.exception.InvalidLoginCredentialsException;
import com.dms.ptp.exception.InvalidParameterException;
import com.dms.ptp.exception.InvalidPasswordException;
import com.dms.ptp.exception.UserNameExistsException;
import com.dms.ptp.exception.UserNotFoundException;
import com.dms.ptp.dto.JWTExtract;
import com.dms.ptp.entity.Role;
import com.dms.ptp.dto.SignUpRequestModel;
import com.dms.ptp.response.UserApprovalResponse;
import com.dms.ptp.dto.UserLoginRequest;
import com.dms.ptp.response.UserLoginResponse;
import com.dms.ptp.service.UserService;
import com.dms.ptp.util.Constant;
import com.dms.ptp.util.EmailUtil;
import com.dms.ptp.util.JWTTokenUtil;

import freemarker.template.TemplateException;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;

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
        logger.info("Inside UserServiceImplImpl: editUserDetails");

        /** status = 0: New user, 1: Active user **/

        User user = userRepo.findByEmail(signUpRequest.getEmail());
        JWTExtract jwtExtract = jwtUtil.getIdRoleFromToken(token);
        String message = "";
        try {
            if (jwtExtract.getUserRole().equals("Admin")) {
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
                }
               
                userRepo.save(user);
                message = Constant.USER_DETAILS_MSG;
            } else if ((!jwtExtract.getUserRole().equals("Admin")) && (status == 1)) {
                /** edit user details by active users **/
                user.setFirstName(signUpRequest.getFirstName());
                user.setLastName(signUpRequest.getLastName());
                user.setPhone(signUpRequest.getPhone());
                user.setDesignation(signUpRequest.getDesignation());
               
                userRepo.save(user);
                message = Constant.USER_DETAILS_MSG;
            }
        //    message = "request unsuccessful";
        } catch (Exception e) {
            message = e.getMessage();
        }
        return message;
    }

    public String userApproval(SignUpRequestModel signUpRequest, String flag) {
        String message = "";
        User user = userRepo.findByEmail(signUpRequest.getUsername());
        logger.info(signUpRequest.getUsername());
        try {

            if (flag.equals("Y")) {
                user.setStatus(1);
                user.setJobRole(signUpRequest.getJobRole());
                user.setUserType(signUpRequest.getUserType());
                userRepo.save(user);

                emailUtil.sendMail(signUpRequest.getUsername(), flag, null);
                message = Constant.USER_APPROVAL_ACCEPT;

            } else {
                user.setStatus(2);
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
        UserLoginResponse loggerinResponse = new UserLoginResponse();
        logger.info("Inside UserServiceImpl: signIn");

        User user1 = userRepo.findByEmail(signInRequest.getUsername());

        /** status-> 0:pending, 1:active, 2:suspended **/
        if (user1.getStatus() == 0) {
            loggerinResponse.setStatus(HttpStatus.UNAUTHORIZED);
            loggerinResponse.setMessage("Cannot loggerin. User id: " + user1.getEmail() + " is not approved");
        } else if (user1.getStatus() == 2) {
            loggerinResponse.setStatus(HttpStatus.UNAUTHORIZED);
            loggerinResponse.setMessage("Cannot loggerin. User id: " + user1.getEmail() + " is suspended");
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

                String otp = generateOTP();
                int userId = generateUserId();
                logger.info(String.valueOf(userId));

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

                loggerinResponse.setUserStatus(Constant.PENDING);
                loggerinResponse.setStatus(HttpStatus.OK);

                // for debug. to be removed later
                loggerinResponse.setOtp(otp);
                loggerinResponse.setUserid(userId);
                // return loggerinResponse;
            } else {
                throw new InvalidLoginCredentialsException(Constant.INVALID_LOGIN_CREDENTIALS);
            }
        }
        return loggerinResponse;
    }

    public UserLoginResponse verifyotp(int userid, int otp, UserLoginRequest signInRequest) {
        logger.info("Inside UserServiceImpl: verifyotp");
        UserLoginResponse loggerinResponse = new UserLoginResponse();

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

            loggerinResponse.setAccessToken(jsonWebToken);
            loggerinResponse.setAgencyCode(user1.getAgencyCode());
            loggerinResponse.setAgencyName(user1.getAgencyName());
            loggerinResponse.setStatus(HttpStatus.OK);
            loggerinResponse.setOtpMessage(Constant.VALID_OTP);
            loggerinResponse.setMessage(Constant.VALID_LOGIN);
        } else {
            loggerinResponse.setStatus(HttpStatus.BAD_REQUEST);
            loggerinResponse.setOtpMessage(Constant.INVALID_OTP);
            loggerinResponse.setMessage(Constant.INVALID_LOGIN);
        }
        return loggerinResponse;
    }

    public Flux<String> getAgencyList() {
        /*
         * String hostname = env.getProperty("lmkserver.address"); String port =
         * env.getProperty("lmkserver.port");
         * 
         * final String url = "http://" + hostname + ":" + port +
         * "/LMKServices/clients";
         */
        logger.info("Inside UserServiceImpl: getAgencyList ");
        final String url = "https://dmsbookingportaluat.multichoice.co.za/LMKServices/clients";

        return this.webClientForUser.get().uri(url).exchange()
                .flatMapMany(clientResponse -> clientResponse.bodyToFlux(String.class));
    }

    public Flux<String> getAdvertisersForAgency(String agencyCode) {
        /*
         * String hostname = env.getProperty("lmkserver.address"); String port =
         * env.getProperty("lmkserver.port");
         * 
         * final String url = "http://" + hostname + ":" + port +
         * "/CariaServices/agency/" + agencyCode + "/advertisers";
         */
        logger.info("Inside UserServiceImpl: getAdvertisersForAgency ");
        String url = "https://dmsbookingportaluat.multichoice.co.za/CariaServices/agency/" + agencyCode
                + "/advertisers";

        return this.webClientForUser.get().uri(url).exchange()
                .flatMapMany(clientResponse -> clientResponse.bodyToFlux(String.class));
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
