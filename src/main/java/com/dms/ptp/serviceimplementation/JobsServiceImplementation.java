package com.dms.ptp.serviceimplementation;

import com.dms.ptp.entity.Jobs;
import com.dms.ptp.exception.BaselineNotFoundException;
import com.dms.ptp.repository.JobRepository;
import com.dms.ptp.response.JobsResponse;
import com.dms.ptp.service.JobsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Method implementation of JobsService
 */
@Service
public class JobsServiceImplementation implements JobsService {

    @Autowired
    JobRepository jobRepository;

    private Logger log = LoggerFactory.getLogger(JobsServiceImplementation.class);

    /**
     * @param fileNames List of String
     * @return List of Jobs
     */
    @Override
    public List<Jobs> saveJobs(List<String> fileNames, String type) {
        List<Jobs> jobsList = new ArrayList<>();
        fileNames.forEach(fileName -> {
            if (jobRepository.findByFileName(fileName) == null)
                jobsList.add(new Jobs((short) 1,type, fileName, (short) 0, LocalDateTime.now()));
        });
        return jobRepository.saveAll(jobsList);
    }

    @Override
    public List<Jobs> getAllJobsByType(String type) {
        return jobRepository.findByType(type);
    }

    @Override
    public JobsResponse updateJobStatus(int id, int status) {
        Jobs jobs =jobRepository.findById(id).orElseThrow(
                () -> new BaselineNotFoundException("Job not found for this Id :: " + id));
        jobs.setStatus((short)status);
        try{
            jobRepository.save(jobs);
        }catch(Exception e){
            log.error("Error while updating Job status");
            return new JobsResponse(false);
        }
        return new JobsResponse(true);
    }

}
