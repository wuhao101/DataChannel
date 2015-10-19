package com.pearson.jmeter.SubPub.Publisher;

import java.io.ByteArrayOutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import com.google.gson.Gson;

public class Producer extends AbstractJavaSamplerClient{    
    private String _url = "http://dev-use1b-pr-29-confluent-kfka-0001.prv-openclass.com:8080/publish";
    private String _messageType = "sub1";
	private String _messageBody = "test1";
	private HttpClient _client = new DefaultHttpClient();
	
    @Override
    public Arguments getDefaultParameters() {
    	Arguments params = new Arguments();

        params.addArgument("Url", "http://dev-use1b-pr-29-confluent-kfka-0001.prv-openclass.com:8080/publish");
        params.addArgument("MessageBody", "test1");
        params.addArgument("MessageType", "sub1");
        
        return params;
    }
    
    @Override
    public void setupTest(JavaSamplerContext ctx) {
        _url = ctx.getParameter("Url");
        _messageBody = ctx.getParameter("MessageBody");
        _messageType = ctx.getParameter("MessageType");
    }
    
    @Override
	public SampleResult runTest(JavaSamplerContext ctx) {
		// TODO Auto-generated method stub
		SampleResult result = new SampleResult();
        result.sampleStart();
        
        try {
        	result.setResponseMessage("Start to produce");
        	
        	Message message = new Message();
    		message.messageType = _messageType;
			message.body = "{" + "\"f1\" : \"" + _messageBody
					+ "\", \"sendTime\" : \"" + System.currentTimeMillis()
					+ "\", \"messageType\" : \"" + message.messageType + "\"}";
    		
			publish(_url, message);
			result.setResponseMessage("Produced a message");
        	
        	result.sampleEnd(); // stop stopwatch
            result.setSuccessful( true );
            result.setResponseMessage( "Successfully produced something" );
            result.setResponseCodeOK(); // 200 code
        } catch(Exception e) {
        	result.sampleEnd(); // stop stopwatch
            result.setSuccessful( false );
            result.setResponseMessage( "Exception: " + e );
        }
        
		return result;
	}
	
	public boolean publish (String url, Message message) throws Exception {
		HttpPost post = new HttpPost(url);

		Gson gson = new Gson();
		String json = gson.toJson(message);
		
		post.setEntity(new StringEntity(json));
		post.setHeader("Content-type", "application/json");
		
		HttpResponse response;
		synchronized (_client) {
			response = _client.execute(post);
		}

		boolean flag = false;
		if (response.getStatusLine().getStatusCode() != 200) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			response.getEntity().writeTo(baos);
			String s = new String(baos.toByteArray());
			System.out.println (s);
			throw new Exception ("non success code : " + response.getStatusLine().getStatusCode());
		} else {
			flag = true;
		}
		
		EntityUtils.consume(response.getEntity());
		
		return flag;
	}
}
