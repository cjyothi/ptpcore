package com.dms.ptp.config;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.StringUtils;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.GetItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.dms.ptp.exception.InvalidParameterException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@PropertySource("classpath:AwsCredentials.properties")
public class AWSDynamoConfig {

	@Value("${amazonDynamoDBEndpoint}")
	private String amazonDynamoDBEndpoint;

	@Value("${accesskey}")
	private String accesskey;

	@Value("${secretkey}")
	private String secretkey;
	
	static Logger logger = LoggerFactory.getLogger(AWSDynamoConfig.class);

	@Bean
	public AmazonDynamoDB amazonDynamoDB() {
		AmazonDynamoDB amazonDynamoDB = new AmazonDynamoDBClient();

		if (!StringUtils.isEmpty(amazonDynamoDBEndpoint)) {
			amazonDynamoDB.setEndpoint(amazonDynamoDBEndpoint);
		}
		return amazonDynamoDB;
	}

	public boolean readTable(int userid, int otp) {
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(accesskey, secretkey);
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(
						new AwsClientBuilder.EndpointConfiguration(amazonDynamoDBEndpoint, "ap-south-1"))
				.withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();

		DynamoDB dynamoDB = new DynamoDB(client);
		Table table = dynamoDB.getTable("otp");

		boolean flag = false;
		try {
		    logger.info("Attempting to read the item...");
			GetItemOutcome itemOutcome = table.getItemOutcome("userid", userid);
			GetItemResult itemResult = itemOutcome.getGetItemResult();

			/*
			 * for (Entry<String, AttributeValue> entry : itemResult.getItem().entrySet()) {
			 * log.info(entry.getKey() + "/" + entry.getValue()); if
			 * ((entry.getValue().getN().equals(String.valueOf(userid)))) { flag = true; } }
			 */
			
			Map<String, AttributeValue> entry = itemResult.getItem();
			String otpValue = entry.get("otpvalue").getN();
			String useridValue = entry.get("userid").getN();
			
			//TRP-1360 change starts
			String ttlValue = entry.get("ttl").getN();

			compareTTL(ttlValue);
			//TRP-1360 change ends

			if ((otpValue.equals(String.valueOf(otp))) && (useridValue.equals(String.valueOf(userid)))) {
				flag = true;
			}
			
		} catch (Exception e) {
		    logger.info("Unable to read item: " + e.getMessage());
		}
		return flag;
	}
	
	private void compareTTL(String ttlValue) throws InvalidParameterException {

		Long epoch1 = Long.parseLong(ttlValue);
		log.info("epoch1 value: " + epoch1);

		Long epoch2 = Instant.now().getEpochSecond();
		log.info("epoch2 value: " + epoch2);

		LocalDateTime ldt1 = Instant.ofEpochMilli(epoch1)
				.atZone(ZoneId.systemDefault()).toLocalDateTime();
		log.info("ldt1 value: " + ldt1);

		LocalDateTime ldt2 = Instant.ofEpochMilli(epoch2)
				.atZone(ZoneId.systemDefault()).toLocalDateTime();
		log.info("ldt2 value: " + ldt2);

		//Time to Live expiration value is greater than the current time in epoch format
		if(ldt1.isBefore(ldt2)) {
			throw new InvalidParameterException("OTP is expired");
		}
	}

	public boolean updateTable(int userid, String otp) {
		int otpValue = Integer.parseInt(otp);
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(accesskey, secretkey);

		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(
						new AwsClientBuilder.EndpointConfiguration(amazonDynamoDBEndpoint, "ap-south-1"))
				.withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();

		DynamoDB dynamoDB = new DynamoDB(client);
		Table table = dynamoDB.getTable("otp");

		// setting the otp expiration to 30 mins
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime expiresAt = now.plusMinutes(30);
		Long expiresAtEpoch = expiresAt.atZone(ZoneId.systemDefault()).toEpochSecond();

		// Build the item
		Item item = new Item().withPrimaryKey("userid", userid).withNumber("otpvalue", otpValue).withNumber("ttl",
				expiresAtEpoch);
		try {
		    logger.info("Updating the item...");
			PutItemOutcome outcome = table.putItem(item);
			logger.info("PutItem succeeded:\n" + outcome);
			return true;
		} catch (Exception e) {
		    logger.error("Unable to update item: " + e.getMessage());
		}
		return false;
	}
}