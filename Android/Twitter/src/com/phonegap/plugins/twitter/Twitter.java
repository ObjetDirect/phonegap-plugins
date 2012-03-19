package com.phonegap.plugins.twitter;

import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;

/**
 * Twitter plugin for Android
 * Inspired of the iOS plugin: https://github.com/phonegap/phonegap-plugins/tree/master/iPhone/Twitter
 * 
 * @author Julien Roche
 * @version 1.0
 */
public class Twitter extends Plugin {
	// Constants
	/** ComposeTweet method's name */
	private static String METHOD_COMPOSE_TWEET = "composeTweet";
	
	/** IsTwitterAvailable method's name */
	private static String METHOD_IS_TWITTER_AVAILABLE = "isTwitterAvailable";
	
	/** Twitter's post activity */
	private static String TWITTER_POST_ACTIVITY = "com.twitter.android.PostActivity";
	
	/** List of available methods */
	private static String[] AVAILABLE_METHODS = new String[]{ METHOD_COMPOSE_TWEET, METHOD_IS_TWITTER_AVAILABLE };
	
	/**
	 * @param args
	 * @param callbackId
	 * @return A PluginResult object with a status and message.
	 */
	public PluginResult composeTweet(JSONArray args, String callbackId) {
		ResolveInfo resolveInfo = getTwitterResolveInfo();
		String message;
		
		if(resolveInfo == null){
			return new PluginResult(PluginResult.Status.ERROR, "Twitter is not available");
		}
		
		if(args.length() <= 0){
			return new PluginResult(PluginResult.Status.ERROR, "No parameter was specified");
		}
		
		
		try {
			message = args.getString(0);
			
		} catch (JSONException e) {
			return new PluginResult(PluginResult.Status.ERROR, "Error with the message");
		}
		
		final ActivityInfo activity = resolveInfo.activityInfo;
        final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        intent.setComponent(name);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        this.ctx.startActivity(intent);
		
        return new PluginResult(PluginResult.Status.OK);
	}

	/** 
     * {@inheritDoc}
	 * @see com.phonegap.api.Plugin#execute(java.lang.String, org.json.JSONArray, java.lang.String)
	 */
	@Override
	public PluginResult execute(String action, JSONArray args, String callbackId) {
		if(action != null && Arrays.binarySearch(AVAILABLE_METHODS, action) >= 0){
			if(METHOD_IS_TWITTER_AVAILABLE.equals(action)){
				return new PluginResult(PluginResult.Status.OK, isTwitterAvailable());
				
			}
			
			if(METHOD_COMPOSE_TWEET.equals(action)) {
				return composeTweet(args, callbackId);
			}
		}
		
		return new PluginResult(PluginResult.Status.ERROR, "This method is not available");
	}
	
	/**
	 * Get the Twitter's {@link ResolveInfo}
	 * @return the Twitter's {@link ResolveInfo}
	 */
	public ResolveInfo getTwitterResolveInfo() {
		try{
			Intent intent = new Intent(Intent.ACTION_SEND);
		    intent.putExtra(Intent.EXTRA_TEXT, "a simple message");
		    intent.setType("text/plain");
		    
		    final PackageManager pm = this.ctx.getPackageManager();
		    final List<ResolveInfo> activityList = pm.queryIntentActivities(intent, 0);
		    int len =  activityList.size();
		    
		    for (int i = 0; i < len; i++) {
		        final ResolveInfo app = activityList.get(i);
		        if (TWITTER_POST_ACTIVITY.equals(app.activityInfo.name)) {
		            return app;
		        }
		    }
		}
		finally {
			
		}
		
		
		return null;
	}

	/**
	 * Check if the Twitter is available
	 * @return true if a Twitter activity is detected
	 */
	public boolean isTwitterAvailable() {
		return getTwitterResolveInfo() != null;
	}
}
