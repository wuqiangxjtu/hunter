<%@ page language="java" contentType="application/json; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="org.springframework.web.util.HtmlUtils,
                com.alibaba.fastjson.serializer.JSONSerializer,
                com.alibaba.fastjson.serializer.SerializeWriter,
                com.alibaba.fastjson.serializer.ValueFilter,
                com.alibaba.fastjson.serializer.SerializerFeature,
                java.io.PrintWriter"
%>
<% 
		//对value进行html过滤，防止xss注入
        ValueFilter Htmlfilter = new ValueFilter(){
			public Object process(Object source, String name, Object value) {
				if(value instanceof String)
					return HtmlUtils.htmlEscape((String)value);
				else{
					return value;
				}
			}
		};
		 
		SerializeWriter sw = new SerializeWriter();
		JSONSerializer serializer = new JSONSerializer(sw);
		serializer.getValueFilters().add(Htmlfilter);
		SerializerFeature nullValueShowFeature = SerializerFeature.WriteMapNullValue;
		//禁止循环引用，对象直接使用值显示
		SerializerFeature disableCircularReferenceDetect = SerializerFeature.DisableCircularReferenceDetect;
		serializer.config(nullValueShowFeature,true);
		serializer.config(disableCircularReferenceDetect, true);
		 
		serializer.write(request.getAttribute("jsonObject"));
		String json = sw.toString();
		//System.out.println(json);
		PrintWriter o = null;
		try {
			response.setContentType("application/json;charset=UTF-8");
			response.setHeader("Cache-Control","no-cache");
			response.setHeader("Pragma","no-cache");
			response.setDateHeader("Expires",0);
			o = response.getWriter();
			o.print(json);
			o.flush();
		} finally {
			if (o != null) {
				o.close();
		    }
        }
%>