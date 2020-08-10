package com.dms.ptp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.dms.ptp.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User>{
	
	User findByEmail(String name);
	User findByEmailAndPassword(String name, String password);
	User findByEmailAndRole(String name, int role);
	User deleteByEmail(String username);
	Page<User> findByStatus(int status, Pageable pageable);
	
	@Override
    Page<User> findAll(Pageable pageable);
}
