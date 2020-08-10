package com.dms.ptp.service;

import com.dms.ptp.entity.Jobs;
import com.dms.ptp.response.JobsResponse;

import java.util.List;

/**
 * JobService
 */
public interface JobsService {

    List<Jobs> saveJobs(List<String> fileNames,String type);
    List<Jobs> getAllJobsByType(String type);
    JobsResponse updateJobStatus(int id, int status);
}
