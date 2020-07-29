package com.dms.ptp.config;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.Map.Entry;

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
import com.dms.ptp.controller.AffinityReachCalculator;

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

			if ((otpValue.equals(String.valueOf(otp))) && (useridValue.equals(String.valueOf(userid)))) {
				flag = true;
			}
			
		} catch (Exception e) {
		    logger.info("Unable to read item: " + userid + e.getMessage());
		}
		return flag;
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