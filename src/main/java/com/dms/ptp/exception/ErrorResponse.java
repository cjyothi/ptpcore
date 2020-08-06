package com.dms.ptp.exception;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "error")
public class ErrorResponse {
	
	    public ErrorResponse(String code, String message) {
	        super();
	        this.code = code;
	        this.message = message;
	        
	    }
	 
	    //General error message about nature of error
	    private String message;
	 
	    //Specific errors in API request processing
	    private String code;

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		
	 
	}

