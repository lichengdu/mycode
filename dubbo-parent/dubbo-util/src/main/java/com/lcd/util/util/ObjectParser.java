package com.lcd.util.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ObjectParser
{
	public static Date toDate(Object date) throws ParseException
	{
		if (date == null)
		{
			return null;
		}
		Date result = null;
		String str = date.toString();
		String parse = str;
		parse = parse.replaceFirst("^[0-9]{4}([^0-9])", "yyyy$1");
		parse = parse.replaceFirst("^[0-9]{2}([^0-9])", "yy$1");
		parse = parse.replaceFirst("([^0-9])[0-9]{1,2}([^0-9])", "$1MM$2");
		parse = parse.replaceFirst("([^0-9])[0-9]{1,2}( ?)", "$1dd$2");
		parse = parse.replaceFirst("( )[0-9]{1,2}([^0-9])", "$1HH$2");
		parse = parse.replaceFirst("([^0-9])[0-9]{1,2}([^0-9])", "$1mm$2");
		parse = parse.replaceFirst("([^0-9])[0-9]{1,2}([^0-9]?)", "$1ss$2");

		SimpleDateFormat format = new SimpleDateFormat(parse);

		result = format.parse(str);

		return result;
	}

	 public static Integer toInteger(Object data)
	{
		if (data == null)
		{
			return null;
		}
		return Integer.valueOf(data.toString());
	}

	 public static Long toLong(Object data)
	{
		if (data == null)
		{
			return null;
		}
		return Long.valueOf(data.toString());
	}

	 public static Short toShort(Object data)
	{
		if (data == null)
		{
			return null;
		}
		return Short.valueOf(data.toString());
	}

	 public static Double toDouble(Object data)
	{
		if (data == null)
		{
			return null;
		}
		return Double.valueOf(data.toString());
	}
	 
	 /**
	  * @author 黄永丰
	  * @createtime Jun 9, 2017
	  * @param data
	  * @param scale 小数点后保留几,最大是5位
	  * @return 四舍五入后的结果
	  */
	 public static String toDoubleString(Object data,Integer scale)
	{
		if (data == null)
		{
			return null;
		}
//		DecimalFormat df = new DecimalFormat("#.00000");
//		return df.format(data);
		NumberFormat nf = NumberFormat.getInstance();
        // 是否以逗号隔开, 默认true以逗号隔开,如[123,456,789.128]
        nf.setGroupingUsed(false);
        //小数点后保留几
        nf.setMaximumFractionDigits(scale); 
        return nf.format(data);
	}
	 

	public static String toString(Object data)
	{
		if (data == null)
		{
			return null;
		}
		return String.valueOf(data.toString());
	}

	public static Boolean toBoolean(Object data)
	{
		if (data == null)
		{
			return null;
		}
		return Boolean.valueOf(data.toString());
	}
	
	public static void main(String[] args) {
		System.out.println(ObjectParser.toDouble(1234567890123.2232323232));
		BigDecimal b1 = new BigDecimal(Double.toString(111111111.22)); 
		System.out.println(b1.add(new BigDecimal(Double.toString(111111111.22))));
		//System.out.println(ArithUtil.add(111111111.22,111111111.22));
		System.out.println(ObjectParser.toDoubleString(123456789012.221245666,6));
	}
}
