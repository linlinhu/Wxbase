package com.emin.platform.wxbase.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.emin.base.exception.EminException;
import com.emin.platform.wxbase.interfaces.PersonApiFeign;

public class UserFilter implements HandlerInterceptor {
	private Logger logger = LoggerFactory.getLogger(UserFilter.class);
	@Autowired
	public  PersonApiFeign personApiFeign;
	
	
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
        JSONObject res = new JSONObject();
		
		try {
			
			if (request.getHeader("token") != null) {
				String token = request.getHeader("token").toString();
				
				res = personApiFeign.userValidate(token);
				
				Boolean  isSuccess = res.getBoolean("success");
				if (!isSuccess || !res.getBoolean("data")) {
					response.sendRedirect("/login");
					return false;
				}

			}
			return true;
		} catch (EminException e) {
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		
		return false;
	}

}
