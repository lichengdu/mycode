package com.masget.openapi;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.EndOfDataDecoderException;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import io.netty.util.CharsetUtil;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.masget.openapi.config.HttpServerConfig;
import com.masget.openapi.entity.RetResponse;
import com.masget.openapi.util.HttpServerUtil;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;

public class HttpServerHandler extends SimpleChannelInboundHandler<Object>
{

	static Log log = LogFactory.getLog(HttpServerHandler.class);
	private HttpRequest request;
	/** Buffer that stores the response content */
	private String buf = "";
	/** 保存callback的函数 */
	private String callback = "";
	private int redirectflag = 0;
	private boolean closehttp = false;
	private static final HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE); // Disk
	private HttpPostRequestDecoder decoder;
	private Map<String, String> origin_params = null;
	String contentParams = "";
	Gson gson = new Gson();
	private StringBuilder requestbuffer = new StringBuilder();

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx)
	{
		if (decoder != null)
		{
			decoder.cleanFiles();
		}
		ctx.flush();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		buf = "";
		HttpRequest request = this.request = (HttpRequest) msg;
		Gson gson = new Gson();
		/* 打印请求信息 */
		requestbuffer.append("\r\n############################BEGIN REQUEST##########################\r\n");
		requestbuffer.append("VERSION: ").append(request.getProtocolVersion()).append("\r\n");
		requestbuffer.append("HOSTNAME: ").append(HttpHeaders.getHost(request, "unknown")).append("\r\n");
		requestbuffer.append("REQUEST_URI: ").append(request.getUri()).append("\r\n");
		HttpHeaders headers = request.headers();
		if (!headers.isEmpty())
		{
			for (Map.Entry<String, String> h : headers)
			{
				String key = h.getKey();
				String value = h.getValue();
				requestbuffer.append("HEADER: ").append(key).append(" = ").append(value).append("\r\n");
			}
		}
		// 判断流程
		if (request.getDecoderResult().isFailure())
		{
			requestbuffer.append("\r\n##################################END REQUEST################################\r\n");
			buf = "解码失败.";
			requestbuffer.append("请求完返回结果信息: ");
			requestbuffer.append(buf + "\r\n");
			log.error(requestbuffer.toString());
			buf = gson.toJson(new RetResponse(1, buf));
			closehttp = true;
			writeResponse(request, ctx);
			return;
		}
		URI uri = new URI(request.getUri());
		// 每次请求都会有这个请求的，直接return就可以了
		if (uri.getPath().equals("/favicon.ico"))
		{
			ctx.close();
			return;
		}
		if (!uri.getPath().equals("/openapi/rest"))
		{
			requestbuffer.append("\r\n##################################END REQUEST################################\r\n");
			buf = "url路径错误.";
			requestbuffer.append("请求完返回结果信息: ");
			requestbuffer.append(buf + "\r\n");
			buf = gson.toJson(new RetResponse(2, buf));
			closehttp = true;
			writeResponse(request, ctx);
			return;
		}
		if (!closehttp)
		{
			if (request.getMethod().equals(HttpMethod.GET))
			{
				/** 分析请求字符串的数据，看是否包含了callback */
				origin_params = getHttpGetParams(request);
				if (!origin_params.isEmpty())
				{
					if (origin_params.containsKey("redirectflag")) 
						redirectflag = Integer.parseInt((String) origin_params.get("redirectflag"));
					String data = doPost(origin_params);
					buf = data;
				}
			}
			else if (request.getMethod().equals(HttpMethod.POST))
			{
				try
				{
					Map<String, String> url_params = getHttpGetParams(request);
					if (msg instanceof HttpContent)
					{
						HttpContent httpContent = (HttpContent) msg;
						ByteBuf content = httpContent.content();
						String params = content.toString(CharsetUtil.UTF_8);
						requestbuffer.append(params);
						origin_params = getJsonParams(params);
						if (origin_params == null)
						{
							origin_params = getHttpPostParams(request);
							if (origin_params == null)
							{
								contentParams = params;
							}
						}
					}
					else
					{
						origin_params = getHttpPostParams(request);
					}
					if (origin_params != null && url_params != null)
					{
						origin_params.putAll(url_params);
					}
				}
				catch (Exception e)
				{
					buf = "解释HTTP POST协议出错." + e.getMessage();
					requestbuffer.append("请求完返回结果信息: ");
					requestbuffer.append(buf + "\r\n");
					log.error(requestbuffer.toString());
					closehttp = true;
					buf = gson.toJson(new RetResponse(3, buf));
				}
				if (origin_params.containsKey("redirectflag")) 
					redirectflag = Integer.parseInt((String) origin_params.get("redirectflag"));
				String data = doPost(origin_params);
				buf = data;
			}
			else
			{
				closehttp = true;
				buf = gson.toJson(new RetResponse(3, "接口只支持HTTP/HTTPS GET/POST协议"));
			}
		}
		writeResponse(request, ctx);
		requestbuffer.append("\r\n##################################END REQUEST################################");
		log.info(requestbuffer.toString());
	}

	/**
	 * 将结果返回给用户
	 * @param currentObj
	 * @param ctx
	 * @return
	 */
	private void writeResponse(HttpObject currentObj, ChannelHandlerContext ctx)
	{
		/* 当不是主动关闭http通道的时候检测 */
		if (!closehttp)
		{
			if (request.headers().contains(CONNECTION, HttpHeaders.Values.CLOSE, true))
			{
				closehttp = true;
			}
			else if (request.getProtocolVersion().equals(HttpVersion.HTTP_1_0))
			{
				closehttp = true;
			}
		}
		/* 当closehttp为flase的时候 */
		boolean keepAlive = false;
		if (!closehttp)
		{
			keepAlive = HttpHeaders.isKeepAlive(request);
		}
		if (!callback.isEmpty())
		{
			buf = callback + "(" + buf + ")";
		}
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, currentObj.getDecoderResult().isSuccess() ? HttpResponseStatus.OK : HttpResponseStatus.BAD_REQUEST,
				Unpooled.copiedBuffer(buf.toString(), CharsetUtil.UTF_8));
		if (redirectflag == 1)
		{
			response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
		}
		else
		{
			response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
		}
		if (keepAlive && HttpServerConfig.getConfig().getSupportkeepalive())
		{
			response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
			response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
		}
		requestbuffer.append("请求完返回结果信息:" + buf.toString());
		/* 是否支持http的keep-alive */
		if (!HttpServerConfig.getConfig().getSupportkeepalive())
		{
			closehttp = true;
		}
		ChannelFuture future = ctx.writeAndFlush(response);
		
		future.addListener(ChannelFutureListener.CLOSE);
		requestbuffer.append("\n---------------服务器主动关闭远程链接.---------------------");
		
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	{
		cause.printStackTrace();
		log.info("Request Is Exception:" + cause.getMessage());
		ctx.close();
	}

	private Map<String, String> getHttpGetParams(HttpRequest request)
	{
		return getQueryParams(request.getUri());
	}

	private Map<String, String> getQueryParams(String params)
	{
		QueryStringDecoder queryStringDecoder = new QueryStringDecoder(params);
		Map<String, String> paramsMap = new HashMap<String, String>();
		for (Entry<String, List<String>> p : queryStringDecoder.parameters().entrySet())
		{
			String key = p.getKey().trim();
			List<String> vals = p.getValue();
			if (vals.size() > 0)
			{
				String value = vals.get(0);
				requestbuffer.append(key + ":" + value+"\n");
				paramsMap.put(key, value);
			}
		}
		return paramsMap;
	}

	private Map<String, String> getJsonParams(String params)
	{
		try
		{
			return gson.fromJson(params, new TypeToken<Map<String, String>>() {}.getType());
		}
		catch (Exception e)
		{
			return null;
		}
	}

	private Map<String, String> getHttpPostParams(HttpRequest request) throws Exception
	{
		Map<String, String> origin_params = new HashMap<String, String>();
		boolean decodeflag = false;
		decoder = new HttpPostRequestDecoder(factory, request);
		try
		{
			while (decoder.hasNext())
			{
				InterfaceHttpData interfaceHttpData = decoder.next();
				if (interfaceHttpData != null)
				{
					try
					{
						/**
						 * HttpDataType有三种类型 Attribute, FileUpload, InternalAttribute
						 */
						if (interfaceHttpData.getHttpDataType() == HttpDataType.Attribute)
						{
							Attribute attribute = (Attribute) interfaceHttpData;
							String value = attribute.getValue();
							String key = attribute.getName();
							requestbuffer.append(key + ":" + value+"\n");
							origin_params.put(key, value);
						}
					}
					catch (Exception e)
					{
						log.info("获取POST请求参数异常", e);
					}
					finally
					{
						interfaceHttpData.release();
					}
				}
			}
		}
		catch (EndOfDataDecoderException e1)
		{
			decodeflag = true;
		}
		catch (Exception e)
		{
			log.error("解释HTTP POST协议出错:" + e.getMessage(), e);
			throw e;
		}
		if (decodeflag) return origin_params;
		return null;
	}

	private String doPost(Map<String, String> postparams)
	{
		String data = "";
		if (postparams.isEmpty()) return gson.toJson(new RetResponse(4, "参数不能为空"));
		if (!postparams.containsKey("method"))
		{
			return gson.toJson(new RetResponse(6, "参数并不包含method，请检查提交的参数"));
		}
		String method = postparams.get("method").toString().toLowerCase();
		String serviceName = HttpServerUtil.getServiceName(method);
		if (HttpServerUtil.isEmpty(serviceName))
		{
			return gson.toJson(new RetResponse(7, "接口method参数出错，请检查提交的参数"));
		}
		Object client = HttpServerSpringContext.getContext().getBean(serviceName);
		if (client == null)
		{
			return gson.toJson(new RetResponse(8, "没有该方法的服务，请联系后台管理员"));
		}
		data = (String) HttpServerUtil.invokeMethodGernaral(client, "doPost", new Object[] { postparams });
		if (data == null)
		{
			data = gson.toJson(new RetResponse(9, "调用接口名称为[" + method + "]失败."));
		}
		try
		{
			JSONObject resultObj = JSONObject.fromObject(data);
			if (resultObj.get("logMessage") != null)
			{
				requestbuffer.append("错误信息:"+resultObj.get("logMessage")+"\r\n");
				resultObj.remove("logMessage");
				data = resultObj.toString();
			}
		}
		catch (Exception e)
		{
			return data;
		}
		return data;
	}
}
