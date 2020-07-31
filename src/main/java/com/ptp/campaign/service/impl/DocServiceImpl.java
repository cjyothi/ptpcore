package com.ptp.campaign.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.ptp.campaign.dao.CampaignRepository;
import com.ptp.campaign.dao.DocRepository;
import com.ptp.campaign.model.DocDetail;
import com.ptp.campaign.model.DocResponse;
import com.ptp.campaign.model.JWTExtract;
import com.ptp.campaign.service.DocService;
import com.ptp.campaign.util.Constants;
import com.ptp.campaign.util.JWTUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DocServiceImpl implements DocService {

    private static final long maxUploadSizeInMb = 5 * 1024 * 1024; // 5 MB
    private static final String SUFFIX = "/";
    @Autowired
    private AmazonS3 amazonS3;

    @Value("${aws.bucket.name}")
    private String bucketName;

    @Value("${aws.default.folder}")
    String defaultBaseFolder;

    @Value("${aws.region}")
    private String region;

    @Autowired
    DocRepository docRepo;

    @Autowired
    CampaignRepository campaignRepo;

    @Autowired
    JWTUtil jwtUtil;

    DocResponse response = new DocResponse();

    /**
     * This method create a folder in s3 bucket
     * 
     * @return
     */
    private String createFolderInBucket(String token, String type) {

        JWTExtract jwtExtract = jwtUtil.getIdRoleFromToken(token);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(0);
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        String folderName = "";

        if (type.equalsIgnoreCase(Constants.FILE_TYPE_TELMAR) || type.equalsIgnoreCase(Constants.FILE_TYPE_ARIANNA)) {
            folderName = System.currentTimeMillis() + "-" + jwtExtract.getUserId() + SUFFIX;
        } else if (type.equalsIgnoreCase(Constants.FILE_TYPE_PROFILE)) {
            folderName = jwtExtract.getUserId() + SUFFIX;
        } else {
            folderName = System.currentTimeMillis() + "-" + jwtExtract.getUserId() + SUFFIX;
        }

        log.info("folderName: " + folderName);

        amazonS3.putObject(bucketName, folderName, inputStream, metadata);
        return folderName;
    }

    /**
     * upload file in s3 bucket
     */
    public DocResponse uploadInS3(MultipartFile file, String originalFilename, String contentType, String folder,
            String type, String token) throws Exception {
        log.info("inside DocServiceImpl: uploadInS3");
        String folderName = "";
        String fileName = file.getOriginalFilename();

        /*
         * Check the file size if greater than 5 MB through file is bigger size
         */

        if (file.getSize() > maxUploadSizeInMb) {
            log.info("File size should not be greater than 5MB!!!");
            throw new FileUploadException("FileSize is morethan 5MB");
        }

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        objectMetadata.setContentLength(file.getSize());

        if (type.equalsIgnoreCase(Constants.FILE_TYPE_PROFILE)) {
            try {
                fileName = formatFileName(file, originalFilename, type, token);
                amazonS3.putObject(bucketName + SUFFIX + defaultBaseFolder, fileName, file.getInputStream(),
                        objectMetadata);
                
             // Set the presigned URL to expire after one hour.
                java.util.Date expiration = new java.util.Date();
                long expTimeMillis = expiration.getTime();
                expTimeMillis += 1000 * 60 * 60;
                expiration.setTime(expTimeMillis);

                // Generate the presigned URL.
                log.info("Generating pre-signed URL.");
                GeneratePresignedUrlRequest generatePresignedUrlRequest =
                		new GeneratePresignedUrlRequest(bucketName, folderName+ SUFFIX + defaultBaseFolder)
                		.withMethod(HttpMethod.GET)
                		.withExpiration(expiration);
                URL preSignedUrl = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);

                log.info("Pre-Signed URL: " + preSignedUrl.toString());

                response.setUrl(preSignedUrl);

                response.setFileName(file.getOriginalFilename());
                response.setS3FileName(fileName);
                response.setFileType(type);
                response.setFolderName(defaultBaseFolder);
                response.setMessage(Constants.FILES_UPLOADED);
                return response;
            } catch (Exception e) {
                response.setMessage(e.getMessage());
                log.error(e.getMessage());
                return response;
            }

        }

        boolean exists = amazonS3.doesObjectExist(bucketName, folder + SUFFIX);
        if (exists) {
            if (amazonS3.doesObjectExist(bucketName, folder + SUFFIX + originalFilename)) {
                fileName = formatFileName(file, originalFilename, type, token);
                folderName = folder + SUFFIX;
            } else {
                folderName = folder + SUFFIX;
            }
        } else {
            folderName = createFolderInBucket(token, type);
        }

        try {
            amazonS3.putObject(bucketName, folderName + fileName, file.getInputStream(), objectMetadata);
            log.info("===================== Upload File - Done! =====================");
            DocDetail docDetail = new DocDetail();
            docDetail.setFileName(originalFilename);
            docDetail.setFileType(type);
            docDetail.setFolderName(folderName);
            docDetail.setS3FileName(fileName);

            docRepo.save(docDetail);
            log.info("===================== Save DocDetail - Done! =====================");
        } catch (AmazonServiceException ase) {
            log.info("Caught an AmazonServiceException from GET requests, rejected reasons:");
            log.info("Error Message:    " + ase.getMessage());
            log.info("HTTP Status Code: " + ase.getStatusCode());
            log.info("AWS Error Code:   " + ase.getErrorCode());
            log.info("Error Type:       " + ase.getErrorType());
            log.info("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            log.info("Caught an AmazonClientException: ");
            log.info("Error Message: " + ace.getMessage());
        } catch (IOException ioe) {
            log.info("IOE Error Message: " + ioe.getMessage());
        }
        response.setFileName(file.getOriginalFilename());
        response.setS3FileName(fileName);
        response.setFileType(type);
        response.setFolderName(folderName);
        return response;
    }

    /**
     * This method append timestamp in fileName and return
     * 
     * @param file
     * @param originalFilename
     * @return
     */
    public String formatFileName(MultipartFile file, String originalFilename, String type, String token) {
        String fileNameWithOutExt = FilenameUtils.removeExtension(originalFilename);
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String fileName = "";

        JWTExtract jwtExtract = jwtUtil.getIdRoleFromToken(token);
        if (type.equalsIgnoreCase(Constants.FILE_TYPE_TELMAR) || type.equalsIgnoreCase(Constants.FILE_TYPE_ARIANNA)) {
            fileName = fileNameWithOutExt + "-" + System.currentTimeMillis() + "$" + type + "." + extension;
        } else if (type.equalsIgnoreCase(Constants.FILE_TYPE_PROFILE)) {
            fileName = fileNameWithOutExt + "-" + jwtExtract.getUserId() + "." + extension;
        }
        log.info("fileName: " + fileName);
        return fileName;

    }

    /**
     * This method remove the given file from s3 bucket location.
     * 
     * @param folderName
     * @param awsFileName
     * @return
     */
    public String deleteObject(String folderName, String awsFileName) {

        boolean isFileExists = isFileExists(bucketName, folderName + SUFFIX + awsFileName);
        try {
            if (isFileExists) {
                amazonS3.deleteObject(bucketName, folderName + SUFFIX + awsFileName);
                return "file: " + awsFileName + " deleted successfully";
            } else {
                log.info("Given folder or fileName not exists in s3 bucket");
            }
        } catch (AmazonServiceException serviceException) {
            log.error(serviceException.getErrorMessage());
        } catch (AmazonClientException exception) {
            log.error("Error while deleting File.");
        }
        return "unable to delete " + awsFileName;
    }

    /**
     * This method checks the given filePath exist in s3 bucket or not
     * 
     * @param bucketName
     * @param folderName
     * @param awsFileName
     * @return
     */
    private boolean isFileExists(String bucketName, String filePath) {
        boolean doesItExists = false;

        try {
            doesItExists = amazonS3.doesObjectExist(bucketName, filePath);
        } catch (Exception error) {
            log.error("error: " + error.getMessage());
        }

        return doesItExists;
    }

    /**
     * This method is for download the given file from s3 location return file with
     * content from s3 location
     */

    public byte[] getObject(String folderName, String awsFileName) throws IOException {
        S3Object s3Object = null;
        byte[] content = null;
        try {
            s3Object = amazonS3.getObject(new GetObjectRequest(bucketName, folderName + SUFFIX + awsFileName));
            final S3ObjectInputStream stream = s3Object.getObjectContent();

            content = IOUtils.toByteArray(stream);
            log.info("*******File downloaded successfully*******");
            s3Object.close();

        } catch (AmazonServiceException ase) {
            log.error("Caught an AmazonServiceException from GET requests, rejected reasons:");
            log.error("Error Message:    " + ase.getMessage());
            log.error("HTTP Status Code: " + ase.getStatusCode());
            log.error("AWS Error Code:   " + ase.getErrorCode());
            log.error("Error Type:       " + ase.getErrorType());
            log.error("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            log.error("Caught an AmazonClientException: ");
            log.error("Error Message: " + ace.getMessage());
        } catch (Exception exc) {
            log.error(exc.getMessage());
        }

        return content;
    }

    /**
     * This method returns the list of files from db based on portalId return list
     * of files
     */
    @Override
    public List<DocResponse> getObjectslistFromFolder(int portalId) {
        String folderName = campaignRepo.findFolderNameByPortalId(portalId);
        List<DocDetail> listDoc = docRepo.findTypeByFolderName(folderName);
        List<DocResponse> listDocResponse = new ArrayList<DocResponse>();
        for (int i = 0; i < listDoc.size(); i++) {
            DocResponse docResponse = new DocResponse();
            docResponse.setFileName(listDoc.get(i).getFileName());
            docResponse.setFileType(listDoc.get(i).getFileType());
            docResponse.setFolderName(listDoc.get(i).getFolderName());
            docResponse.setS3FileName(listDoc.get(i).getS3FileName());
            listDocResponse.add(docResponse);
        }

        return listDocResponse;

    }

    /**
     * This method return the folderName based on portalId
     */
    public String getFolderNameByPortalId(int portalId) {
        String folderName = campaignRepo.findFolderNameByPortalId(portalId);
        return folderName;
    }
}
