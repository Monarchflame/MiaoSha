package com.imooc.miaosha.validator;
import  javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import com.imooc.miaosha.util.ValidatorUtil;

public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {
	//值是否是必须的
	private boolean required = false;
	
	public void initialize(IsMobile constraintAnnotation) {
		required = constraintAnnotation.required();
	}

	/**
	 * 判断value是否合法
	 * @param value
	 * @param context
	 * @return
	 */
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(required) {
			return ValidatorUtil.isMobile(value);
		}else {
			//不必须且为空，合法
			if(StringUtils.isEmpty(value)) {
				return true;
			}else {
				//判断是否合法（为手机号）
				return ValidatorUtil.isMobile(value);
			}
		}
	}

}
