/*
 * Copyright (c) 2011 CCX(China) Co.,Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * CCX(China) Co.,Ltd. ("Confidential Information").
 * It may not be copied or reproduced in any manner without the express 
 * written permission of CCX(China) Co.,Ltd.
 * 
 *	Created on 2011-7-19 上午11:19:39
 */
package im.qingji.sqlutil.convert.impl;

import im.qingji.sqlutil.convert.ConvertException;

import java.math.BigDecimal;

/**
 * 
 * @author qingji
 * @version 0.1 <br>
 *          Created on 2011-7-19 上午11:19:39
 */
public class BigDecimalConverter extends AbstractConverter {

	@Override
	public Object convert(Class<?> targetType, Object obj) throws ConvertException {

		try {
			if (targetType.equals(obj.getClass())) {
				return obj;
			}
			if (isSame(BigDecimal.class, obj.getClass())) {
				BigDecimal data = (BigDecimal) obj;
				if (isSame(Float.class, targetType) || "float".equals(targetType.getName()) ) {
					return data.floatValue();
				} else if (isSame(Long.class, targetType) || "long".equals(targetType.getName())) {
					return data.longValue();
				} else if (isSame(Integer.class, targetType) || "int".equals(targetType.getName()) ) {
					return data.intValue();
				} else if (isSame(Double.class, targetType) || "double".equals(targetType.getName())) {
					return data.doubleValue();
				} else if (isSame(Short.class, targetType) || "shor".equals(targetType.getName())) {
					return data.shortValue();
				} else if (isSame(Byte.class, targetType) || "byte".equals(targetType.getName())) {
					return data.byteValue();
				} else if (isSame(String.class, targetType)) {
					return data.toString();
				}
			}
			throw unsupport(targetType, obj);
		} catch (Exception e) {
			throw caseThrowable(targetType, obj, e);
		}
	}

}
